<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
/**
 * @module name 报表管理
 * @module desc 报表列表管理
 * @author  wxl
 * @create  date 2012-7-7
 * @modify  man ***
 * @modify  date ***
 */
 Ext.namespace('reportinfo', 'reportinfo.reportInfoMngApp');


//create application
reportinfo.reportInfoMngApp = function() {
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
    	  var aimobj = Ext.mainScreem.findById('docs-报表列表管理');//获取打开得页签对象
    	  var lyobj = new Ext.layout.BorderLayout();//定义面板
    	  aimobj.setLayout(lyobj);//给页签对象设置布局格式
    	  aimobj.add(reportinfo.reportInfoMngApp.deptTree);
    	  aimobj.add(reportinfo.reportInfoMngApp.gridPanelObj);
    	  aimobj.doLayout();//强制布局
    	  
    	  

          
    	  //SysUserMng.user.organizeTreePanel.getRootNode().expand();    	  
    	  reportinfo.reportInfoMngApp.gridStoreObj.load({params:{start:0, limit:10}});
    	  
    	  $JIT.loaded('module/editorContent/editor');
    	  $JIT.loaded('resources/js/ckeditor3.6.2/ckeditor');
    	  $JIT.script('module/editorContent/editor');
    	  $JIT.script('resources/js/ckeditor3.6.2/ckeditor');
      }
  };
}(); // end of app

// 树 
reportinfo.reportInfoMngApp.root = new Ext.tree.AsyncTreeNode({
	text : '报表类别',
	expanded : true,
	id : '0000'
});
reportinfo.reportInfoMngApp.deptTree = new Ext.tree.TreePanel({
	loader : new Ext.tree.TreeLoader({
				baseAttrs : {},
				dataUrl : 'showReportTypdIdTree'
			}),
	root : reportinfo.reportInfoMngApp.root,
	title : '<span style="font-weight:normal">报表类别</span>',
	iconCls : 'chart_organisationIcon',
	tools : [{
				id : 'refresh',
				handler : function() {
					reportinfo.reportInfoMngApp.deptTree.root.reload();
				}
			}],
	contextMenu: new Ext.menu.Menu({
        items: [{
            id: 'add-node',
            text: '添加节点'
        },{
            id: 'editer-node',
            text: '修改节点'
        },{
            id: 'del-node',
            text: '删除节点'
        }],
        listeners: {
            itemclick: function(item) {
                switch (item.id) {
                    case 'add-node':
                        var n = item.parentMenu.contextNode;
                        alert(n.text);
                        break;
                    case 'editer-node':
                        var n = item.parentMenu.contextNode;
                        if (n.parentNode) {
                            n.remove();
                        }
                        break;
                    case 'del-node':
                        var n = item.parentMenu.contextNode;
                        if (n.parentNode) {
                            n.remove();
                        }
                        break;
                }
            }
        }
    }),
    listeners: {
        contextmenu: function(node, e) {
//          Register the context node with the menu so that a Menu Item's handler function can access
//          it via its parentMenu property.
            node.select();
            var c = node.getOwnerTree().contextMenu;
            c.contextNode = node;
            c.showAt(e.getXY());
        }
    },
	collapsible : true,
	//width : 210,
	//height:500,
	//minSize : 160,
	//maxSize : 280,
	split : true,
	region : 'west',
	autoScroll : true,
	
	
	width : 200,
	
	animate : false,
	useArrows : false,
	border : false
});
reportinfo.reportInfoMngApp.deptTree.on('click', function(node,e) {
	e.stopEvent();
	deptid = node.attributes.id;
	store.load({
				params : {
					start : 0,
					limit : bbar.pageSize,
					node : deptid,
					deptid : deptid
				}
			});
});




// 复选框
reportinfo.reportInfoMngApp.sm = new Ext.grid.CheckboxSelectionModel();

reportinfo.reportInfoMngApp.columModelObj = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),reportinfo.reportInfoMngApp.sm, {
	header : '报表编码',
	dataIndex : 'reportId',
	width : 100
}, {
	header : '报表名称',
	sortable : true,
	dataIndex : 'reportName',
	width : 150

}, {
	header : '报表描述',
	dataIndex : 'reportDesc',
	width : 100
}, {
	header : '报表目录编码',
	dataIndex : 'catalogId',
	width : 200
}]);


reportinfo.reportInfoMngApp.gridStoreObj = new Ext.data.Store({
	// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
	pruneModifiedRecords : true,
	proxy : new Ext.data.HttpProxy({
				url : 'getBritReportInfo'
			}),
	reader : new Ext.data.JsonReader({
				totalProperty : 'pageInfo.totalRowNum',
				root : 'data'
			}, [{
						name : 'reportId'
					}, {
						name : 'reportName'
					}, {
						name : 'reportDesc'
					}, {
						name : 'catalogId'
					}])
});







//定义加载数据传入后台的参数
reportinfo.reportInfoMngApp.gridStoreObj.on('beforeload', function() {0
	this.baseParams = {
		//menuid : menuid,
		pdepartid:'0000'
	};
});


//定义一个下拉框
reportinfo.reportInfoMngApp.gridComboBoxObj=new Ext.form.ComboBox({
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
reportinfo.reportInfoMngApp.number = parseInt(reportinfo.reportInfoMngApp.gridComboBoxObj.getValue());

//定义下拉框的选中事件
reportinfo.reportInfoMngApp.gridComboBoxObj.on("select", function(comboBox) {
	reportinfo.reportInfoMngApp.bbar.pageSize = parseInt(comboBox.getValue());
	reportinfo.reportInfoMngApp.number = parseInt(comboBox.getValue());
	reportinfo.reportInfoMngApp.gridStoreObj.reload({
				params : {
					start : 0,
					limit : reportinfo.reportInfoMngApp.bbar.pageSize
				}
			});
});

//定义列表面板的底部面板的工具条
reportinfo.reportInfoMngApp.bbar = new Ext.PagingToolbar({
	pageSize : reportinfo.reportInfoMngApp.number,
	store : reportinfo.reportInfoMngApp.gridStoreObj,
	displayInfo : true,
	displayMsg : '显示{0}条到{1}条,共{2}条',
	emptyMsg : "没有符合条件的记录",
	items : ['-', '&nbsp;&nbsp;', reportinfo.reportInfoMngApp.gridComboBoxObj]
});



//创建列表面板
reportinfo.reportInfoMngApp.gridPanelObj =new Ext.grid.EditorGridPanel({
	//title : '<span class="commoncss">表单列表</span>',
	iconCls : 'app_boxesIcon',
	height : 500,
	autoScroll : true,
	region : 'center',
	store : reportinfo.reportInfoMngApp.gridStoreObj,
	loadMask : {
		msg : '正在加载表格数据,请稍等...'
	},
	stripeRows : true,
	frame : true,
	//autoExpandColumn : 'menuname',
	cm : reportinfo.reportInfoMngApp.columModelObj,
	sm : reportinfo.reportInfoMngApp.sm,
	clicksToEdit : 1,
	tbar : [{
				text : '新增',
				iconCls : 'addIcon',
				id : 'id_addRow',
				handler : function() {
					formWin.show();//showAddFormTab();
				}
			}, '-', {
				text : '修改',
				iconCls : 'acceptIcon',
				id : 'id_save',
				handler : function() {
					showUpdateFormTab();
				}
			}, '-', {
				text : '刷新',
				iconCls : 'arrow_refreshIcon',
				handler : function() {
					reportinfo.reportInfoMngApp.gridStoreObj.reload();
				}
			}, '-', {
				text : '删除',
				iconCls : 'arrow_refreshIcon',
				handler : function() {
					deleteForm();
				}
			}, '->', new Ext.form.TextField( {
						id : 'queryParam',
						fieldLabel : '编码',
						name : 'queryParam',
						emptyText : '请输入类别编码',
						enableKeyEvents : true,
						listeners : {
							specialkey : function(field, e) {
								if (e.getKey() == Ext.EventObject.ENTER) {
									queryForm();
								}
							}
						},
						width : 130
					}), {
						text : '查询',
						iconCls : 'previewIcon',
						handler : function() {
							queryForm();
						}
					},'-',
					    {
						text : '重置',
						iconCls : 'btn-reset',
						scope : this,
						handler : function() {
							Ext.getCmp('queryParam').setValue('');
						}
						}],
	bbar : reportinfo.reportInfoMngApp.bbar
});

function queryForm(){
	var queryParam = Ext.getCmp('queryParam').getValue();
			reportinfo.reportInfoMngApp.gridStoreObj.reload({
				params : {
					queryParam : queryParam,
					 start : 0, 
					 limit : reportinfo.reportInfoMngApp.bbar.pageSize
				}
			});
	}
//报表列表新增
var addReportFrom = new Ext.FormPanel({
    labelAlign: 'left',
    title: '新增报表列表',
    buttonAlign:'center',
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,
    frame:true,
    labelWidth:60,
      items: [{
        xtype:'fieldset',
        title: '新增报表列表',
        collapsible: true,
        autoHeight:true,
        defaults: {width: 600},
    items: [{
        layout:'column',   
        border:false,
        labelSeparator:'：',
        items:[{
            columnWidth:.5,  
            layout: 'form',
            border:false,
            items: [{                     
                cls : 'key',
                xtype:'textfield',
                fieldLabel: '报表名称',
                allowBlank:false,
                emptyText:'报表名称不能为空', 
                name: 'reportName',
                anchor:'90%'
            },
				{                     
                cls : 'key',
                xtype:'textarea',
                fieldLabel: '报表描述',
                name: 'reportDesc',
                width:650,
                anchor:'90%'
            }
            ] },
            {
            columnWidth:.5,  
            layout: 'form',
            border:false,
            items: [{                    
                cls : 'key',
                xtype:'textfield',
                fieldLabel: '报表目录编码',
                name: 'catalogId',
                allowBlank:false,
                emptyText:'报表目录编码不能为空', 
                anchor:'90%'
            }
            ] 
         }]
    }] 
  }]
,
    buttons: [{
        text: '保存',
        handler: function(){
            if(addReportFrom.getForm().isValid()){
            	addReportFrom.getForm().submit({
                    url: 'addReportInfo',
                    waitMsg: '正在保存请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    	    formWin.hide();
                    		reportinfo.reportInfoMngApp.gridStoreObj.reload();
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
        	formWin.hide();
        }
    }]
});

var formWin = new Ext.Window({
    width:650, height: 340,
    layout: 'fit', // explicitly set layout manager: override the default (layout:'auto')
    closeAction:'hide',
    items: [addReportFrom]
});	


//报表列表修改
var updatereportinfoFrom = new Ext.FormPanel({
    labelAlign: 'left',
    title: '修改报表列表',
    buttonAlign:'center',
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,
    frame:true,
    labelWidth:60,
      items: [{
        xtype:'fieldset',
        title: '修改报表列表',
        collapsible: true,
        autoHeight:true,
        defaults: {width: 600},
    items: [{
        layout:'column',   
        border:false,
        labelSeparator:'：',
        items:[{
            columnWidth:.5,  
            layout: 'form',
            border:false,
            items: [{                    
                cls : 'key',
                xtype:'textfield',
                fieldLabel: '报表编码',
                name: 'reportId',
                allowBlank:false,
                emptyText:'报表不能为空', 
                anchor:'90%'
            },{                     
                cls : 'key',
                xtype:'textfield',
                fieldLabel: '报表名称',
                allowBlank:false,
                emptyText:'报表名称不能为空', 
                name: 'reportName',
                anchor:'90%'
            },
				{                     
                cls : 'key',
                xtype:'textarea',
                fieldLabel: '报表描述',
                name: 'reportDesc',
                width:650,
                anchor:'90%'
            }
            ] },
            {
            columnWidth:.5,  
            layout: 'form',
            border:false,
            items: [{                    
                cls : 'key',
                xtype:'textfield',
                fieldLabel: '报表目录编码',
                name: 'catalogId',
                allowBlank:false,
                emptyText:'报表目录编码', 
                anchor:'90%'
            }
            ] 
         }]
    }] 
  }]
,
    buttons: [{
        text: '保存',
        handler: function(){
            if(updatereportinfoFrom.getForm().isValid()){
            	updatereportinfoFrom.getForm().submit({
                    url: 'updateReportInfo',
                    waitMsg: '正在保存请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    	    showUpdateForm.hide();
                    		reportinfo.reportInfoMngApp.gridStoreObj.reload();
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
        	showUpdateForm.hide();
        }
    }]
});
// 只获取选择一行
function  getOneCheckboxValue() {
	// 返回一个行集合JS数组
	var rows = reportinfo.reportInfoMngApp.gridPanelObj.getSelectionModel().getSelections();
	if (Ext.isEmpty(rows)) {
		Ext.MessageBox.alert('提示', '您没有选中任何数据!');
		return;
		}
	    if(rows.length!=1){
	       Ext.MessageBox.alert('提示', '您只可以选取一条数据!');
	       return;
	    }
	    return true;
	}

var showUpdateForm = new Ext.Window({
    width:650, height: 340,
    layout: 'fit', // explicitly set layout manager: override the default (layout:'auto')
    closeAction:'hide',
    items: [updatereportinfoFrom]
});	

function showUpdateUserWin(){

	showUpdateForm.show();
	var rows =  reportinfo.reportInfoMngApp.gridPanelObj.getSelectionModel().getSelections();
	updatereportinfoFrom.getForm().findField('reportId').setValue(rows[0].get('reportId'));
	updatereportinfoFrom.getForm().findField('reportName').setValue(rows[0].get('reportName'));
	updatereportinfoFrom.getForm().findField('reportDesc').setValue(rows[0].get('reportDesc'));
	updatereportinfoFrom.getForm().findField('catalogId').setValue(rows[0].get('catalogId'));
}
function showUpdateFormTab(){
if(getOneCheckboxValue()){
showUpdateUserWin();
}
}

// 获取选择行
function  getCheckboxValues() {
	// 返回一个行集合JS数组
	var rows = reportinfo.reportInfoMngApp.gridPanelObj.getSelectionModel().getSelections();
	if (Ext.isEmpty(rows)) {
		Ext.MessageBox.alert('提示', '您没有选中任何数据!');
		return;
	}
	var checkIdsSelect="";
			for ( var i = 0; i < rows.length; i++) {
				  checkIdsSelect += "'";
                  checkIdsSelect += rows[i].get('reportId');
                  checkIdsSelect += "',";
			}
	return 	checkIdsSelect.substr(0,checkIdsSelect.length-1);
	// 将JS数组中的行级主键，生成以,分隔的字符串
	//var strChecked = jsArray2JsString(rows, 'departid');
	//Ext.MessageBox.alert('提示', strChecked);
	// 获得选中数据后则可以传入后台继续处理
	}
	
//流程删除	
function deleteForm(){
    if(getCheckboxValues()){
	var reportId = getCheckboxValues();
Ext.Msg.confirm('请确认', '你确认要删除当前对象吗?', function(btn,
									text) {
								if (btn == 'yes') {

									delItem(reportId);

								} else {
									return;
								}
							});
}
function delItem(reportId) {

	Ext.Ajax.request({
				url : 'delReportInfo',
				success : function(response) {
					reportinfo.reportInfoMngApp.gridStoreObj.reload();
					var resultArray = Ext.util.JSON
							.decode(response.responseText);
					Ext.Msg.alert('提示', resultArray.info);
				},
				failure : function(response) {
					Ext.MessageBox.alert('提示', '数据删除失败');
				},
				params : {
					reportId : reportId
				}
			});
			}
}



Ext.onReady(reportinfo.reportInfoMngApp.init, reportinfo.reportInfoMngApp);
</script>
