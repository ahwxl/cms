<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript">
/**
 * @module name 菜单管理
 * @module desc 系统菜单的添加和删除
 * @author  wxl
 * @create  date 20120406
 * @modify  man ***
 * @modify  date ***
 */
 Ext.namespace('SysMenuMng', 'SysMenuMng.menu');


//create application
SysMenuMng.menu = function() {
  // do NOT access DOM from here; elements don't exist yet

  //此处定义私有属性变量
  var dragZone1, dragZone2;

  //此处定义私有方法

  //共有区
  return {
      //此处定义共有属性变量
      

      //共有方法
      init: function() {
    	  
          //定义一些初始化行为
    	  var aimobj = Ext.mainScreem.findById('docs-菜单管理');//获取打开得页签对象
    	  var lyobj = new Ext.layout.BorderLayout();//定义面板
    	  aimobj.setLayout(lyobj);//给页签对象设置布局格式
    	  //aimobj.add(SysUserMng.user.organizeTreePanel);
    	  aimobj.add(SysMenuMng.menu.gridPanelObj);
    	  aimobj.doLayout();//强制布局
    	  
          
    	  //SysUserMng.user.organizeTreePanel.getRootNode().expand();
    	  SysMenuMng.menu.gridStoreObj.load({params:{start:0, limit:10}});
      }
  };
}(); // end of app



SysMenuMng.menu.columModelObj = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
	header : '菜单编码',
	dataIndex : 'menuid',
	hidden:true,
	sortable : true,
	width : 250

}, {
	header : '菜单名称',
	dataIndex : 'menuname',
	width : 180,
          //renderer : CMPTYPERender,
	editor : new Ext.grid.GridEditor(new Ext.form.TextField({
				maxLength : 50
			}))
}, {
	header : '连接URL',
	dataIndex : 'menuurl',
	width : 500,
	editor : new Ext.grid.GridEditor(new Ext.form.TextField({
				maxLength : 50
			}))
}, {
	header : '所属模块',
	dataIndex : 'moduleid',
	id : 'menuname',
	width : 80,
	editor : new Ext.grid.GridEditor(new Ext.form.TextField({
				maxLength : 50
			}))
}, {
	header : '状态',
	dataIndex : 'enabled',
	width : 50,
	editor : new Ext.grid.GridEditor(new Ext.form.TextField({
				maxLength : 50
			}))
}, {
	header : '操作',
	dataIndex : 'optSysMenu',
	renderer : optSysMenu,
	sortable: true
}]);

function optSysMenu(value, p, record){
        return String.format('<u onclick=\"delSysMenuById(\'{0}\')\" >删除</u>&nbsp;&nbsp;<u onclick=\"goEditSysMenuPage(\'{0}\')\" >修改</u>',record.data['menuid']);
}

function delSysMenuById(id){
    	Ext.Ajax.request({
    		   url: 'doDelSysMenu?menuid='+id,
    		   method:'get',
    		   success: function(response, opts) {
    			      alert(response.responseText);
    		   },
    		   failure: function(response, opts) {
    			      alert('失败');
    		   }
    		});
    	
    	
}

function goEditSysMenuPage(id){
   	var url = 'showEditordoEditorSysMenuPage?menuid='+id;
   	/**
   	*打开一个iframe窗口
   	*/
   	Ext.mainScreem.addNewTab(Ext.getmifObj({id:'menu1',title:'修改系统菜单信息',src:url}),'menu1');
}


SysMenuMng.menu.gridStoreObj = new Ext.data.Store({
	// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
	pruneModifiedRecords : true,
	proxy : new Ext.data.HttpProxy({
				url : 'getSysMenuDataJsonNew'
			}),
	reader : new Ext.data.JsonReader({
				totalProperty : 'pageInfo.totalRowNum',
				root : 'data'
			}, [{
						name : 'menuid'
					}, {
						name : 'menuname'
					}, {
						name : 'menuurl'
					}, {
						name : 'moduleid'
					}, {
						name : 'enabled'
					}])
});







//定义加载数据传入后台的参数
SysMenuMng.menu.gridStoreObj.on('beforeload', function() {0
	this.baseParams = {
		menuid : '',
		pdepartid:'0000'
	};
});


//定义一个下拉框
SysMenuMng.menu.gridComboBoxObj=new Ext.form.ComboBox({
	name : 'pagesize',
	hiddenName : 'pagesize',
	typeAhead : true,
	triggerAction : 'all',
	lazyRender : true,
	mode : 'local',
	store : new Ext.data.ArrayStore({
				fields : ['value', 'text'],
				data : [[3, '3条/页'],[10, '10条/页'], [20, '20条/页'],
						[50, '50条/页'], [100, '100条/页'],
						[250, '250条/页'], [500, '500条/页']]
			}),
	valueField : 'value',
	displayField : 'text',
	value : '10',
	editable : false,
	width : 85
});

//定义全局变量 用于记录下拉框选中的变量
SysMenuMng.menu.number = parseInt(SysMenuMng.menu.gridComboBoxObj.getValue());

//定义下拉框的选中事件
SysMenuMng.menu.gridComboBoxObj.on("select", function(comboBox) {
	SysMenuMng.menu.bbar.pageSize = parseInt(comboBox.getValue());
	number = parseInt(comboBox.getValue());
	SysMenuMng.menu.gridStoreObj.reload({
				params : {
					start : 0,
					limit : SysMenuMng.menu.bbar.pageSize
				}
			});
});

//定义列表面板的底部面板的工具条
SysMenuMng.menu.bbar = new Ext.PagingToolbar({
	pageSize : SysMenuMng.menu.number,
	store : SysMenuMng.menu.gridStoreObj,
	displayInfo : true,
	displayMsg : '显示{0}条到{1}条,共{2}条',
	emptyMsg : "没有符合条件的记录",
	items : ['-', '&nbsp;&nbsp;', SysMenuMng.menu.gridComboBoxObj]
});



//创建列表面板
SysMenuMng.menu.gridPanelObj =new Ext.grid.EditorGridPanel({
	title : '<span class="commoncss">系统菜单列表</span>',
	iconCls : 'app_boxesIcon',
	height : 500,
	autoScroll : true,
	region : 'center',
	store : SysMenuMng.menu.gridStoreObj,
	loadMask : {
		msg : '正在加载表格数据,请稍等...'
	},
	stripeRows : true,
	frame : true,
	autoExpandColumn : 'menuname',
	cm : SysMenuMng.menu.columModelObj,
	clicksToEdit : 1,
	tbar : [{
				text : '新增',
				iconCls : 'addIcon',
				id : 'id_addRow',
				handler : function() {
					showSysMenuWin();
					//addInit();
				}
			}, '-', {
				text : '保存',
				iconCls : 'acceptIcon',
				id : 'id_save',
				handler : function() {
					if (runMode == '0') {
						Ext.Msg.alert('提示','系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!');
						return;
					}
					saveOrUpdateData();
				}
			}, '-', {
				text : '刷新',
				iconCls : 'arrow_refreshIcon',
				handler : function() {
					SysMenuMng.menu.gridStoreObj.reload();
				}
			}],
	bbar : SysMenuMng.menu.bbar
});


var addSysMenuForm = new Ext.FormPanel({
    labelWidth: 75, // label settings here cascade unless overridden
    url:'addTmplPage',
    frame:false,
    title: '添加系统菜单',
    fileUpload:true,
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,

    items: [{
        xtype:'fieldset',
        title: '添加系统菜单',
        collapsible: true,
        autoHeight:true,
        defaults: {
        	width: 210,
            allowBlank: false},
//        defaultType: 'textfield',
        items :[{
        	    xtype: 'textfield',
                fieldLabel: '菜单名称',
                name: 'menuname',
                value: ''
            },{
        	    xtype: 'textfield',
                fieldLabel: '连接URL',
                name: 'menuurl',
                value: ''
            },{
        	    xtype: 'textfield',
                fieldLabel: '所属模块',
                name: 'moduleid',
                value: ''
            },{
            xtype: 'radiogroup',
            fieldLabel: '状态',
            columns: 2,
            items: [
                {boxLabel: '启用', name: 'enabled', inputValue: '1', checked: true},
                {boxLabel: '禁用', name: 'enabled', inputValue: '0'}
            ]
        }
        ]
    }],

    buttons: [{
        text: '保存',
        handler: function(){
            if(addSysMenuForm.getForm().isValid()){
            	addSysMenuForm.getForm().submit({
                    url: 'addSysMenu',
                    waitMsg: '正在上传请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    },
                    failure :function(fm,rp){	                    	
                    	//alert(rp.result);
                    	Ext.Msg.alert('Status', '操作异常，请联系管理员.');
                    }
                });
            }
        }
    },{
        text: '关闭',
        handler: function(){
        	//Ext.myloadMask.show();
        	//Ext.myloadMask.hide();
        	//addDepartmentForm.getForm().reset();
        	addSysMenuWin.hide();
        }
    }]
});

var addSysMenuWin = new Ext.Window({
    width:500, height: 500,
    layout: 'fit', // explicitly set layout manager: override the default (layout:'auto')
    title:'添加菜单',
    closeAction:'hide',
    items: [addSysMenuForm]
});

//显示添加菜单窗口
function showSysMenuWin(){

	addSysMenuWin.show();
//	addSysMenuForm.getForm().findField('pdepartid').setValue(menuTree.getSelectionModel().getSelectedNode().attributes.id);
//	addSysMenuForm.getForm().findField('pdepartname').setValue(menuTree.getSelectionModel().getSelectedNode().attributes.text);
	
}



Ext.onReady(SysMenuMng.menu.init, SysMenuMng.menu);

</script>