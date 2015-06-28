<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
/**
 * @module name 工作流管理
 * @module desc 流程类型管理
 * @author  wxl
 * @create  date 2012-7-7
 * @modify  man ***
 * @modify  date ***
 */
 Ext.namespace('workflowMng', 'workflowMng.processTypeMngApp');


//create application
workflowMng.processTypeMngApp = function() {
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
    	  var aimobj = Ext.mainScreem.findById('docs-流程类型管理');//获取打开得页签对象
    	  var lyobj = new Ext.layout.BorderLayout();//定义面板
    	  aimobj.setLayout(lyobj);//给页签对象设置布局格式
    	  //aimobj.add(workflowMng.processTypeMngApp.deptTree);
    	  aimobj.add(workflowMng.processTypeMngApp.gridPanelObj);
    	  aimobj.doLayout();//强制布局
    	  
    	  

          
    	  //SysUserMng.user.organizeTreePanel.getRootNode().expand();    	  
    	  workflowMng.processTypeMngApp.gridStoreObj.load({params:{start:0, limit:10}});
    	  
    	  $JIT.loaded('module/editorContent/editor');
    	  $JIT.loaded('resources/js/ckeditor3.6.2/ckeditor');
    	  $JIT.script('module/editorContent/editor');
    	  $JIT.script('resources/js/ckeditor3.6.2/ckeditor');
      }
  };
}(); // end of app


// 复选框
workflowMng.processTypeMngApp.sm = new Ext.grid.CheckboxSelectionModel();

workflowMng.processTypeMngApp.columModelObj = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),workflowMng.processTypeMngApp.sm, {
	header : '类型编号',
	dataIndex : 'typeId',
	width : 100
}, {
	header : '类型名称',
	sortable : true,
	dataIndex : 'typeName',
	width : 150

}, {
	header : '父编码',
	dataIndex : 'parentTypeId',
	hidden:true,
	width : 100
}, {
	header : '备注',
	dataIndex : 'typeDesc',
	width : 200
}]);


workflowMng.processTypeMngApp.gridStoreObj = new Ext.data.Store({
	// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
	pruneModifiedRecords : true,
	proxy : new Ext.data.HttpProxy({
				url : 'getFromType'
			}),
	reader : new Ext.data.JsonReader({
				totalProperty : 'pageInfo.totalRowNum',
				root : 'data'
			}, [{
						name : 'typeId'
					}, {
						name : 'typeName'
					}, {
						name : 'parentTypeId'
					}, {
						name : 'typeDesc'
					}])
});







//定义加载数据传入后台的参数
workflowMng.processTypeMngApp.gridStoreObj.on('beforeload', function() {0
	this.baseParams = {
		//menuid : menuid,
		pdepartid:'0000'
	};
});


//定义一个下拉框
workflowMng.processTypeMngApp.gridComboBoxObj=new Ext.form.ComboBox({
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
workflowMng.processTypeMngApp.number = parseInt(workflowMng.processTypeMngApp.gridComboBoxObj.getValue());

//定义下拉框的选中事件
workflowMng.processTypeMngApp.gridComboBoxObj.on("select", function(comboBox) {
	workflowMng.processTypeMngApp.bbar.pageSize = parseInt(comboBox.getValue());
	workflowMng.processTypeMngApp.number = parseInt(comboBox.getValue());
	workflowMng.processTypeMngApp.gridStoreObj.reload({
				params : {
					start : 0,
					limit : workflowMng.processTypeMngApp.bbar.pageSize
				}
			});
});

//定义列表面板的底部面板的工具条
workflowMng.processTypeMngApp.bbar = new Ext.PagingToolbar({
	pageSize : workflowMng.processTypeMngApp.number,
	store : workflowMng.processTypeMngApp.gridStoreObj,
	displayInfo : true,
	displayMsg : '显示{0}条到{1}条,共{2}条',
	emptyMsg : "没有符合条件的记录",
	items : ['-', '&nbsp;&nbsp;', workflowMng.processTypeMngApp.gridComboBoxObj]
});



//创建列表面板
workflowMng.processTypeMngApp.gridPanelObj =new Ext.grid.EditorGridPanel({
	//title : '<span class="commoncss">表单列表</span>',
	iconCls : 'app_boxesIcon',
	height : 500,
	autoScroll : true,
	region : 'center',
	store : workflowMng.processTypeMngApp.gridStoreObj,
	loadMask : {
		msg : '正在加载表格数据,请稍等...'
	},
	stripeRows : true,
	frame : true,
	//autoExpandColumn : 'menuname',
	cm : workflowMng.processTypeMngApp.columModelObj,
	sm : workflowMng.processTypeMngApp.sm,
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
					workflowMng.processTypeMngApp.gridStoreObj.reload();
				}
			}, '-', {
				text : '删除',
				iconCls : 'arrow_refreshIcon',
				handler : function() {
					deleteForm();
				}
			}, '->', new Ext.form.TextField( {
						id : 'queryParam',
						fieldLabel : '类型编号',
						name : 'queryParam',
						emptyText : '请输入类型编号',
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
	bbar : workflowMng.processTypeMngApp.bbar
});

function queryForm(){
	var queryParam = Ext.getCmp('queryParam').getValue();
			workflowMng.processTypeMngApp.gridStoreObj.reload({
				params : {
					queryParam : queryParam,
					 start : 0, 
					 limit : workflowMng.processTypeMngApp.bbar.pageSize
				}
			});
	}
//流程新增
var addProcessFrom = new Ext.FormPanel({
    labelAlign: 'left',
    title: '新增流程类型',
    buttonAlign:'center',
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,
    frame:true,
    labelWidth:60,
      items: [{
        xtype:'fieldset',
        title: '新增流程类型',
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
                fieldLabel: '类型名称',
                allowBlank:false,
                emptyText:'类型名称不能为空', 
                name: 'typeName',
                anchor:'90%'
            },
				{                     
                cls : 'key',
                xtype:'textarea',
                fieldLabel: '用户描述',
                name: 'typeDesc',
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
                fieldLabel: '父类编号',
                name: 'parentTypeId',
                allowBlank:false,
                emptyText:'父类编号不能为空', 
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
            if(addProcessFrom.getForm().isValid()){
            	addProcessFrom.getForm().submit({
                    url: 'addProcessType',
                    waitMsg: '正在保存请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    	    formWin.hide();
                    		workflowMng.processTypeMngApp.gridStoreObj.reload();
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
    items: [addProcessFrom]
});	


//流程修改
var updateProcessFrom = new Ext.FormPanel({
    labelAlign: 'left',
    title: '修改流程类型',
    buttonAlign:'center',
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,
    frame:true,
    labelWidth:60,
      items: [{
        xtype:'fieldset',
        title: '修改流程类型',
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
                fieldLabel: '类型编号',
                name: 'typeId',
                allowBlank:false,
                emptyText:'类型编号不能为空', 
                anchor:'90%'
            },{                     
                cls : 'key',
                xtype:'textfield',
                fieldLabel: '类型名称',
                allowBlank:false,
                emptyText:'类型名称不能为空', 
                name: 'typeName',
                anchor:'90%'
            },
				{                     
                cls : 'key',
                xtype:'textarea',
                fieldLabel: '用户描述',
                name: 'typeDesc',
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
                fieldLabel: '父类编号',
                name: 'parentTypeId',
                allowBlank:false,
                emptyText:'父类编号不能为空', 
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
            if(updateProcessFrom.getForm().isValid()){
            	updateProcessFrom.getForm().submit({
                    url: 'updateProcessType',
                    waitMsg: '正在保存请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    	    showUpdateForm.hide();
                    		workflowMng.processTypeMngApp.gridStoreObj.reload();
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
	var rows = workflowMng.processTypeMngApp.gridPanelObj.getSelectionModel().getSelections();
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
    items: [updateProcessFrom]
});	

function showUpdateUserWin(){

	showUpdateForm.show();
	var rows =  workflowMng.processTypeMngApp.gridPanelObj.getSelectionModel().getSelections();
	updateProcessFrom.getForm().findField('typeId').setValue(rows[0].get('typeId'));
	updateProcessFrom.getForm().findField('typeName').setValue(rows[0].get('typeName'));
	updateProcessFrom.getForm().findField('typeDesc').setValue(rows[0].get('typeDesc'));
	updateProcessFrom.getForm().findField('parentTypeId').setValue(rows[0].get('parentTypeId'));
}
function showUpdateFormTab(){
if(getOneCheckboxValue()){
showUpdateUserWin();
}
}

// 获取选择行
function  getCheckboxValues() {
	// 返回一个行集合JS数组
	var rows = workflowMng.processTypeMngApp.gridPanelObj.getSelectionModel().getSelections();
	if (Ext.isEmpty(rows)) {
		Ext.MessageBox.alert('提示', '您没有选中任何数据!');
		return;
	}
	var checkIdsSelect="";
			for ( var i = 0; i < rows.length; i++) {
				  checkIdsSelect += "'";
                  checkIdsSelect += rows[i].get('typeId');
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
	var typeId = getCheckboxValues();
Ext.Msg.confirm('请确认', '你确认要删除当前对象吗?', function(btn,
									text) {
								if (btn == 'yes') {

									delItem(typeId);

								} else {
									return;
								}
							});
}
function delItem(typeId) {

	Ext.Ajax.request({
				url : 'delProcessType',
				success : function(response) {
					workflowMng.processTypeMngApp.gridStoreObj.reload();
					var resultArray = Ext.util.JSON
							.decode(response.responseText);
					Ext.Msg.alert('提示', resultArray.info);
				},
				failure : function(response) {
					Ext.MessageBox.alert('提示', '数据删除失败');
				},
				params : {
					typeId : typeId
				}
			});
			}
}
Ext.onReady(workflowMng.processTypeMngApp.init, workflowMng.processTypeMngApp);
</script>