<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
/**
 * @module name 报表管理
 * @module desc 报表类别管理
 * @author  wxl
 * @create  date 2012-7-7
 * @modify  man ***
 * @modify  date ***
 */
 Ext.namespace('reporttype', 'reporttype.reportTypeMngApp');


//create application
reporttype.reportTypeMngApp = function() {
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
    	  var aimobj = Ext.mainScreem.findById('docs-报表类别管理');//获取打开得页签对象
    	  var lyobj = new Ext.layout.BorderLayout();//定义面板
    	  aimobj.setLayout(lyobj);//给页签对象设置布局格式
    	  //aimobj.add(reporttype.reportTypeMngApp.deptTree);
    	  aimobj.add(reporttype.reportTypeMngApp.gridPanelObj);
    	  aimobj.doLayout();//强制布局
    	  
    	  

          
    	  //SysUserMng.user.organizeTreePanel.getRootNode().expand();    	  
    	  reporttype.reportTypeMngApp.gridStoreObj.load({params:{start:0, limit:10}});
    	  
    	  $JIT.loaded('module/editorContent/editor');
    	  $JIT.loaded('resources/js/ckeditor3.6.2/ckeditor');
    	  $JIT.script('module/editorContent/editor');
    	  $JIT.script('resources/js/ckeditor3.6.2/ckeditor');
      }
  };
}(); // end of app


// 复选框
reporttype.reportTypeMngApp.sm = new Ext.grid.CheckboxSelectionModel();

reporttype.reportTypeMngApp.columModelObj = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),reporttype.reportTypeMngApp.sm, {
	header : '编码',
	dataIndex : 'reportTypeId',
	width : 100
}, {
	header : '类别名称',
	sortable : true,
	dataIndex : 'reportTypeName',
	width : 150

}, {
	header : '类别描述',
	dataIndex : 'reportTypeDesc',
	width : 100
}, {
	header : '父类别',
	dataIndex : 'parentTypeId',
	width : 200
}]);


reporttype.reportTypeMngApp.gridStoreObj = new Ext.data.Store({
	// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
	pruneModifiedRecords : true,
	proxy : new Ext.data.HttpProxy({
				url : 'getBritReportType'
			}),
	reader : new Ext.data.JsonReader({
				totalProperty : 'pageInfo.totalRowNum',
				root : 'data'
			}, [{
						name : 'reportTypeId'
					}, {
						name : 'reportTypeName'
					}, {
						name : 'reportTypeDesc'
					}, {
						name : 'parentTypeId'
					}])
});







//定义加载数据传入后台的参数
reporttype.reportTypeMngApp.gridStoreObj.on('beforeload', function() {0
	this.baseParams = {
		//menuid : menuid,
		pdepartid:'0000'
	};
});


//定义一个下拉框
reporttype.reportTypeMngApp.gridComboBoxObj=new Ext.form.ComboBox({
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
reporttype.reportTypeMngApp.number = parseInt(reporttype.reportTypeMngApp.gridComboBoxObj.getValue());

//定义下拉框的选中事件
reporttype.reportTypeMngApp.gridComboBoxObj.on("select", function(comboBox) {
	reporttype.reportTypeMngApp.bbar.pageSize = parseInt(comboBox.getValue());
	reporttype.reportTypeMngApp.number = parseInt(comboBox.getValue());
	reporttype.reportTypeMngApp.gridStoreObj.reload({
				params : {
					start : 0,
					limit : reporttype.reportTypeMngApp.bbar.pageSize
				}
			});
});

//定义列表面板的底部面板的工具条
reporttype.reportTypeMngApp.bbar = new Ext.PagingToolbar({
	pageSize : reporttype.reportTypeMngApp.number,
	store : reporttype.reportTypeMngApp.gridStoreObj,
	displayInfo : true,
	displayMsg : '显示{0}条到{1}条,共{2}条',
	emptyMsg : "没有符合条件的记录",
	items : ['-', '&nbsp;&nbsp;', reporttype.reportTypeMngApp.gridComboBoxObj]
});



//创建列表面板
reporttype.reportTypeMngApp.gridPanelObj =new Ext.grid.EditorGridPanel({
	//title : '<span class="commoncss">表单列表</span>',
	iconCls : 'app_boxesIcon',
	height : 500,
	autoScroll : true,
	region : 'center',
	store : reporttype.reportTypeMngApp.gridStoreObj,
	loadMask : {
		msg : '正在加载表格数据,请稍等...'
	},
	stripeRows : true,
	frame : true,
	//autoExpandColumn : 'menuname',
	cm : reporttype.reportTypeMngApp.columModelObj,
	sm : reporttype.reportTypeMngApp.sm,
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
					reporttype.reportTypeMngApp.gridStoreObj.reload();
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
	bbar : reporttype.reportTypeMngApp.bbar
});

function queryForm(){
	var queryParam = Ext.getCmp('queryParam').getValue();
			reporttype.reportTypeMngApp.gridStoreObj.reload({
				params : {
					queryParam : queryParam,
					 start : 0, 
					 limit : reporttype.reportTypeMngApp.bbar.pageSize
				}
			});
	}
	
	
//--------------------------------------------------------------
var addRoot = new Ext.tree.AsyncTreeNode( {
			text : "父类编号",
			expanded : true,
			id: '0000'
		});
		var addDeptTree = new Ext.tree.TreePanel( {
			loader : new Ext.tree.TreeLoader({
				baseAttrs : {},
				baseParams:{parentTypeId:'0000'},
				dataUrl : 'showReportTypdIdTree'
			}),
			root : addRoot,
			autoScroll : true,
			animate : false,
			useArrows : false,
			border : false
		});
		// 监听下拉树的节点单击事件
		addDeptTree.on('click', function(node) {
			comboxWithTree.setValue(node.text);
			//Ext.getCmp("addForm").findById('formType').setValue(node.attributes.id);
			comboxWithTree.collapse();
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
					fieldLabel : '父类编号',
					anchor : '90%',
					mode : 'local',
					triggerAction : 'all',
					maxHeight : 300,
					// 下拉框的显示模板,addDeptTreeDiv作为显示下拉树的容器
					tpl : "<tpl for='.'><div style='height:390px'><div id='addDeptTreeDiv'></div></div></tpl>",
					allowBlank : false,
					onSelect : Ext.emptyFn
				});
		// 监听下拉框的下拉展开事件
		comboxWithTree.on('expand', function() {
			// 将UI树挂到treeDiv容器
				addDeptTree.render('addDeptTreeDiv');
				 //addDeptTree.root.expand(); //只是第一次下拉会加载数据
				addDeptTree.root.reload(); // 每次下拉都会加载数据
			});
			
//报表类别新增
var addReportFrom = new Ext.FormPanel({
    labelAlign: 'left',
    title: '新增报表类别',
    buttonAlign:'center',
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,
    frame:true,
    labelWidth:60,
      items: [{
        xtype:'fieldset',
        title: '新增报表类别',
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
                fieldLabel: '类别名称',
                allowBlank:false,
                emptyText:'类别名称不能为空', 
                name: 'reportTypeName',
                anchor:'90%'
            },
				{                     
                cls : 'key',
                xtype:'textarea',
                fieldLabel: '类别描述',
                name: 'reportTypeDesc',
                width:650,
                anchor:'90%'
            }
            ] },
            {
            columnWidth:.5,  
            layout: 'form',
            border:false,
            items: [comboxWithTree,{               
                    xtype:'hidden',
                    id:'formType',
                    name: 'formType'}   
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
                    url: 'addReportType',
                    waitMsg: '正在保存请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    	    formWin.hide();
                    		reporttype.reportTypeMngApp.gridStoreObj.reload();
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


//报表类别修改
var updateReportTypeFrom = new Ext.FormPanel({
    labelAlign: 'left',
    title: '修改报表类别',
    buttonAlign:'center',
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,
    frame:true,
    labelWidth:60,
      items: [{
        xtype:'fieldset',
        title: '修改报表类别',
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
                fieldLabel: '编码',
                name: 'reportTypeId',
                allowBlank:false,
                emptyText:'编码不能为空', 
                anchor:'90%'
            },{                     
                cls : 'key',
                xtype:'textfield',
                fieldLabel: '类别名称',
                allowBlank:false,
                emptyText:'类别名称不能为空', 
                name: 'reportTypeName',
                anchor:'90%'
            },
				{                     
                cls : 'key',
                xtype:'textarea',
                fieldLabel: '类别描述',
                name: 'reportTypeDesc',
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
            if(updateReportTypeFrom.getForm().isValid()){
            	updateReportTypeFrom.getForm().submit({
                    url: 'updateReportType',
                    waitMsg: '正在保存请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    	    showUpdateForm.hide();
                    		reporttype.reportTypeMngApp.gridStoreObj.reload();
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
	var rows = reporttype.reportTypeMngApp.gridPanelObj.getSelectionModel().getSelections();
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
    items: [updateReportTypeFrom]
});	

function showUpdateUserWin(){

	showUpdateForm.show();
	var rows =  reporttype.reportTypeMngApp.gridPanelObj.getSelectionModel().getSelections();
	updateReportTypeFrom.getForm().findField('reportTypeId').setValue(rows[0].get('reportTypeId'));
	updateReportTypeFrom.getForm().findField('reportTypeName').setValue(rows[0].get('reportTypeName'));
	updateReportTypeFrom.getForm().findField('reportTypeDesc').setValue(rows[0].get('reportTypeDesc'));
	updateReportTypeFrom.getForm().findField('parentTypeId').setValue(rows[0].get('parentTypeId'));
}
function showUpdateFormTab(){
if(getOneCheckboxValue()){
showUpdateUserWin();
}
}

// 获取选择行
function  getCheckboxValues() {
	// 返回一个行集合JS数组
	var rows = reporttype.reportTypeMngApp.gridPanelObj.getSelectionModel().getSelections();
	if (Ext.isEmpty(rows)) {
		Ext.MessageBox.alert('提示', '您没有选中任何数据!');
		return;
	}
	var checkIdsSelect="";
			for ( var i = 0; i < rows.length; i++) {
				  checkIdsSelect += "'";
                  checkIdsSelect += rows[i].get('reportTypeId');
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
	var reportTypeId = getCheckboxValues();
Ext.Msg.confirm('请确认', '你确认要删除当前对象吗?', function(btn,
									text) {
								if (btn == 'yes') {

									delItem(reportTypeId);

								} else {
									return;
								}
							});
}
function delItem(reportTypeId) {

	Ext.Ajax.request({
				url : 'delReportType',
				success : function(response) {
					reporttype.reportTypeMngApp.gridStoreObj.reload();
					var resultArray = Ext.util.JSON
							.decode(response.responseText);
					Ext.Msg.alert('提示', resultArray.info);
				},
				failure : function(response) {
					Ext.MessageBox.alert('提示', '数据删除失败');
				},
				params : {
					reportTypeId : reportTypeId
				}
			});
			}
}
Ext.onReady(reporttype.reportTypeMngApp.init, reporttype.reportTypeMngApp);
</script>