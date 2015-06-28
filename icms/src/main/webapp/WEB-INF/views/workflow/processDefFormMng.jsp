<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
/**
 * @module name 工作流管理
 * @module desc 流程表单管理
 * @author  wxl
 * @create  date 2012-7-7
 * @modify  man ***
 * @modify  date ***
 */
 Ext.namespace('workflowMng', 'workflowMng.processInstanceMngApp');


//create application
workflowMng.processInstanceMngApp = function() {
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
    	  var aimobj = Ext.mainScreem.findById('docs-流程表单管理');//获取打开得页签对象
    	  var lyobj = new Ext.layout.BorderLayout();//定义面板
    	  aimobj.setLayout(lyobj);//给页签对象设置布局格式

    	  //aimobj.add(workflowMng.processInstanceMngApp.deptTree);
    	  aimobj.add(workflowMng.processInstanceMngApp.gridPanelObj);
    	  aimobj.doLayout();//强制布局
    	  
    	  

          
    	  //SysUserMng.user.organizeTreePanel.getRootNode().expand();    	  
    	  workflowMng.processInstanceMngApp.gridStoreObj.load({params:{start:0, limit:10}});
    	  
    	  $JIT.loaded('module/editorContent/editor');
    	  $JIT.loaded('resources/js/ckeditor3.6.2/ckeditor');
    	  $JIT.script('module/editorContent/editor');
    	  $JIT.script('resources/js/ckeditor3.6.2/ckeditor');
      }
  };
}(); // end of app


// 复选框
workflowMng.processInstanceMngApp.sm = new Ext.grid.CheckboxSelectionModel();

workflowMng.processInstanceMngApp.columModelObj = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),workflowMng.processInstanceMngApp.sm, {
	header : '表单编号',
	dataIndex : 'formId',
	width : 200
}, {
	header : '表单名称',
	sortable : true,
	dataIndex : 'formName',
	width : 150

}, {
	header : '表单类型ID',
	dataIndex : 'formType',
	hidden : true,
	width : 100
}, {
	header : '表单类型',
	dataIndex : 'formTypeName',
	width : 100
}, {
	header : '备注',
	dataIndex : 'formDesc',
	width : 200
}]);


workflowMng.processInstanceMngApp.gridStoreObj = new Ext.data.Store({
	// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
	pruneModifiedRecords : true,
	proxy : new Ext.data.HttpProxy({
				url : 'viewDefForm'//getProcessInstanceItemJson 
			}),
	reader : new Ext.data.JsonReader({
				totalProperty : 'pageInfo.totalRowNum',
				root : 'data'
			}, [{
						name : 'formId'
					}, {
						name : 'formName'
					}, {
						name : 'formType'
					}, {
						name : 'formTypeName'
					}, {
						name : 'formDesc'
					}, {
						name : 'formInfo'
					}])
});


//定义加载数据传入后台的参数
workflowMng.processInstanceMngApp.gridStoreObj.on('beforeload', function() {
    var queryParam = Ext.getCmp('queryParam').getValue();
	this.baseParams = {
		queryParam : queryParam,
		pdepartid:'0000'
	};
});


//定义一个下拉框
workflowMng.processInstanceMngApp.gridComboBoxObj=new Ext.form.ComboBox({
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
workflowMng.processInstanceMngApp.number = parseInt(workflowMng.processInstanceMngApp.gridComboBoxObj.getValue());

//定义下拉框的选中事件
workflowMng.processInstanceMngApp.gridComboBoxObj.on("select", function(comboBox) {
	workflowMng.processInstanceMngApp.bbar.pageSize = parseInt(comboBox.getValue());
	workflowMng.processInstanceMngApp.number = parseInt(comboBox.getValue());
	workflowMng.processInstanceMngApp.gridStoreObj.reload({
				params : {
					start : 0,
					limit : workflowMng.processInstanceMngApp.bbar.pageSize
				}
			});
});

//定义列表面板的底部面板的工具条
workflowMng.processInstanceMngApp.bbar = new Ext.PagingToolbar({
	pageSize : workflowMng.processInstanceMngApp.number,
	store : workflowMng.processInstanceMngApp.gridStoreObj,
	displayInfo : true,
	displayMsg : '显示{0}条到{1}条,共{2}条',
	emptyMsg : "没有符合条件的记录",
	items : ['-', '&nbsp;&nbsp;', workflowMng.processInstanceMngApp.gridComboBoxObj]
});



//创建列表面板
workflowMng.processInstanceMngApp.gridPanelObj =new Ext.grid.EditorGridPanel({
	//title : '<span class="commoncss">表单列表</span>',
	iconCls : 'app_boxesIcon',
	height : 500,
	autoScroll : true,
	region : 'center',
	store : workflowMng.processInstanceMngApp.gridStoreObj,
	loadMask : {
		msg : '正在加载表格数据,请稍等...'
	},
	stripeRows : true,
	frame : true,
	//autoExpandColumn : 'menuname',
	cm : workflowMng.processInstanceMngApp.columModelObj,
	sm : workflowMng.processInstanceMngApp.sm,
	clicksToEdit : 1,
	tbar : [{
				text : '新增1',
				iconCls : 'addIcon',
				id : 'id_addRow1',
				handler : function() {
					formWin.show();//showAddFormTab();
					comboxWithTree.setDisabled(false);
					Ext.getCmp("formName").setValue("");
					Ext.getCmp("formType").setValue("");
					Ext.getCmp("formTypeName").setValue("");
					Ext.getCmp("formDesc").setValue("");
					Ext.getCmp("formInfo").setValue("");
					var oEditor = CKEDITOR.instances.editor1;
	                oEditor.setData(rows[0].get(""));
				}
			}, '-',{
				text : '新增2',
				iconCls : 'addIcon',
				id : 'id_addRow2',
				handler : function() {
					Ext.mainScreem.addNewTab(returnmif({id:'自定义表单',title:'自定义表单',src:'createDefineFormPage'}),'自定义表单');
				}
			}, '-', {
				text : '修改',
				iconCls : 'acceptIcon',
				id : 'id_save',
				handler : function() {
					showUpdateForm();
				}
			}, '-', {
				text : '刷新',
				iconCls : 'arrow_refreshIcon',
				handler : function() {
					workflowMng.processInstanceMngApp.gridStoreObj.reload();
				}
			}, '-', {
				text : '删除',
				iconCls : 'arrow_refreshIcon',
				handler : function() {
					delForm();
				}
			}, '->', new Ext.form.TextField( {
						id : 'queryParam',
						fieldLabel : '表单名称',
						name : 'queryParam',
						emptyText : '请输入表单名称',
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
	bbar : workflowMng.processInstanceMngApp.bbar
});

function queryForm(){
			workflowMng.processInstanceMngApp.gridStoreObj.reload({
				params : {
					 start : 0, 
					 limit : workflowMng.processInstanceMngApp.bbar.pageSize
				}
			});
	}


// 只获取选择一行
function  getOneCheckboxValue() {
	// 返回一个行集合JS数组
	var rows = workflowMng.processInstanceMngApp.gridPanelObj.getSelectionModel().getSelections();
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
// 获取选择行
function  getCheckboxValues() {
	// 返回一个行集合JS数组
	var rows = workflowMng.processInstanceMngApp.gridPanelObj.getSelectionModel().getSelections();
	if (Ext.isEmpty(rows)) {
		Ext.MessageBox.alert('提示', '您没有选中任何数据!');
		return;
	}
	var checkIdsSelect="";
			for ( var i = 0; i < rows.length; i++) {
				  checkIdsSelect += "'";
                  checkIdsSelect += rows[i].get('formId');
                  checkIdsSelect += "',";
			}
	return 	checkIdsSelect.substr(0,checkIdsSelect.length-1);
	// 将JS数组中的行级主键，生成以,分隔的字符串
	//var strChecked = jsArray2JsString(rows, 'departid');
	//Ext.MessageBox.alert('提示', strChecked);
	// 获得选中数据后则可以传入后台继续处理
}

var addRoot = new Ext.tree.AsyncTreeNode( {
			text : "表单类型",
			expanded : true,
			id: '0000'
		});
		var addDeptTree = new Ext.tree.TreePanel( {
			loader : new Ext.tree.TreeLoader({
				baseAttrs : {},
				baseParams:{pdepartid:'0000'},
				dataUrl : 'showFormTypdIdTree'
			}),
			root : addRoot,
			height:390,
			autoScroll : true,
			animate : false,
			useArrows : false,
			border : false
		});
		// 监听下拉树的节点单击事件
		addDeptTree.on('click', function(node) {
			comboxWithTree.setValue(node.text);
			addForm.getForm().findField('formType').setValue(node.attributes.id);
			//comboxWithTree.collapse();
		});
		var comboxWithTree = new Ext.form.ComboBox(
				{
					id : 'formTypeName',
					store : new Ext.data.SimpleStore( {
						fields : [],
						data : [ [] ]
					}),
					editable : false,
					value : ' ',
					emptyText : '请选择...',
					fieldLabel : '表单类型',
					anchor : '80%',
					mode : 'local',
					triggerAction : 'all',
					maxHeight : 390,
					// 下拉框的显示模板,addDeptTreeDiv作为显示下拉树的容器
					tpl : "<tpl for='.'><div style='height:390px'><div id='addDeptTreeDiv'></div></div></tpl>",
					allowBlank : false,
					onSelect : Ext.emptyFn
				});
		// 监听下拉框的下拉展开事件
		comboxWithTree.on('expand', function() {
			// 将UI树挂到treeDiv容器
				addDeptTree.render('addDeptTreeDiv');
				addDeptTree.root.expand(); //只是第一次下拉会加载数据
				//addDeptTree.root.reload(); // 每次下拉都会加载数据

			});

var addForm = new Ext.FormPanel({
    labelAlign: 'left',
    title: '新增表单',
    buttonAlign:'center',
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 550,
    frame:true,
    labelWidth:60,
      items: [{
        xtype:'fieldset',
        title: '新增表单',
        collapsible: false,
        autoHeight:true,
        defaults: {width: 530},
        items: [{
            layout:'column',   
            border:false,
            labelSeparator:'：',
            items:[{ 
                layout: 'form',
                border:false,
                items: [{                    
                    xtype:'hidden',
                    id: 'formId',
                    name: 'formId'
                },{                    
                    cls : 'key',
                    xtype:'textfield',
                    fieldLabel: '表单名称',
                    id:'formName',
                    name: 'formName',
                    allowBlank:false,
                    emptyText:'表单名称不能为空', 
                    anchor:'80%'
                },comboxWithTree,{                    
                    xtype:'hidden',
                    id:'formType',
                    name: 'formType'   
                },{                    
                    xtype:'hidden',
                    id:'formInfo',
                    name: 'formInfo'   
                },{                    
                    cls : 'key',
                    xtype:'textfield',
                    fieldLabel: '备注',
                    id:'formDesc',
                    name: 'formDesc',
                    allowBlank:true,
                    anchor:'80%'
                },{
    	            html:'<textarea class="ckeditor" cols="60" id="editor1" name="editor1" rows="10"></textarea>',
    	            listeners: {
    	                'afterlayout': {
    	                	fn: function(p){
    	                	    CKEDITOR.replace( 'editor1',
    	                	       {
    	                	           filebrowserBrowseUrl : 'ckfinderPop',
    	                	           filebrowserUploadUrl : 'uploader/upload.php',
    	                	           filebrowserImageWindowWidth : '500',
    	                	           filebrowserImageWindowHeight : '480'                	           
    	                	       });
    	                	},
    	                	single: true // important, as many layouts can occur
    	                }
    	            }
    	         }]
             }]
         }]
       }],
    buttons: [{
        text: '保存',
        handler: function(){
            if(addForm.getForm().isValid()){
                var oEditor = CKEDITOR.instances.editor1;
                addForm.getForm().findField('formInfo').setValue(oEditor.getData());
            	addForm.getForm().submit({
                    url: 'saveDefForm',
                    waitMsg: '正在保存请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    	    formWin.hide();
                    		workflowMng.processInstanceMngApp.gridStoreObj.reload();
                    },
                    failure :function(fm,rp){	                    	
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

addDeptTree.on('beforeload', function(node) {
	this.loader.baseParams = {
		pdepartid:node.id
	};
});

var formWin = new Ext.Window({
    width:600, height: 600,
    layout: 'fit', // explicitly set layout manager: override the default (layout:'auto')
    closeAction:'hide',
    items: [addForm]
});	
   
//显示修改部门窗口
function showUpdateFormWin(){

	formWin.show();
	var rows =  workflowMng.processInstanceMngApp.gridPanelObj.getSelectionModel().getSelections();
	addForm.getForm().findField('formId').setValue(rows[0].get('formId'));
	addForm.getForm().findField('formName').setValue(rows[0].get('formName'));
	addForm.getForm().findField('formType').setValue(rows[0].get('formType'));
	addForm.getForm().findField('formTypeName').setValue(rows[0].get('formTypeName'));
	addForm.getForm().findField('formDesc').setValue(rows[0].get('formDesc'));
	addForm.getForm().findField('formInfo').setValue(rows[0].get('formInfo'));
	var oEditor = CKEDITOR.instances.editor1;
	oEditor.setData(rows[0].get('formInfo'));
}
function showUpdateForm(){
if(getOneCheckboxValue()){
showUpdateFormWin();
}
}

function delForm(){
    if(getCheckboxValues()){
	var formId = getCheckboxValues();
Ext.Msg.confirm('请确认', '你确认要删除当前对象吗?', function(btn,
									text) {
								if (btn == 'yes') {

									delItem(formId);

								} else {
									return;
								}
							});
}
function delItem(formId) {

	Ext.Ajax.request({
				url : 'delDefForm',
				success : function(response) {
					workflowMng.processInstanceMngApp.gridStoreObj.reload();
					var resultArray = Ext.util.JSON
							.decode(response.responseText);
					Ext.Msg.alert('提示', resultArray.info);
				},
				failure : function(response) {
					Ext.MessageBox.alert('提示', '数据删除失败');
				},
				params : {
					formId : formId
				}
			});
			}
}



Ext.onReady(workflowMng.processInstanceMngApp.init, workflowMng.processInstanceMngApp);
</script>
<div id="deptTreeDiv"></div>