<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
/**
 * @module name 系统管理
 * @module desc 系统调度管理
 * @author  wxl
 * @create  date 2012-7-7
 * @modify  man ***
 * @modify  date ***
 */
 Ext.namespace('scheduling', 'scheduling.schedulingMngApp');


//create application
scheduling.schedulingMngApp = function() {
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
    	  var aimobj = Ext.mainScreem.findById('docs-系统调度管理');//获取打开得页签对象
    	  var lyobj = new Ext.layout.BorderLayout();//定义面板
    	  aimobj.setLayout(lyobj);//给页签对象设置布局格式
    	  //aimobj.add(scheduling.schedulingMngApp.deptTree);
    	  aimobj.add(scheduling.schedulingMngApp.gridPanelObj);
    	  aimobj.doLayout();//强制布局
    	  
    	  

          
    	  //SysUserMng.user.organizeTreePanel.getRootNode().expand();    	  
    	  scheduling.schedulingMngApp.gridStoreObj.load({params:{start:0, limit:10}});
    	  
    	  $JIT.loaded('module/editorContent/editor');
    	  $JIT.loaded('resources/js/ckeditor3.6.2/ckeditor');
    	  $JIT.script('module/editorContent/editor');
    	  $JIT.script('resources/js/ckeditor3.6.2/ckeditor');
      }
  };
}(); // end of app


// 复选框
scheduling.schedulingMngApp.sm = new Ext.grid.CheckboxSelectionModel();

scheduling.schedulingMngApp.columModelObj = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),scheduling.schedulingMngApp.sm, {
	header : '编码',
	dataIndex : 'id',
	width : 100
}]);


scheduling.schedulingMngApp.gridStoreObj = new Ext.data.Store({
	// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
	pruneModifiedRecords : true,
	proxy : new Ext.data.HttpProxy({
				url : 'getSaveInfo'
			}),
	reader : new Ext.data.JsonReader({
				totalProperty : 'pageInfo.totalRowNum',
				root : 'data'
			}, [{
						name : 'id'
					}])
});







//定义加载数据传入后台的参数
scheduling.schedulingMngApp.gridStoreObj.on('beforeload', function() {0
	this.baseParams = {
		//menuid : menuid,
		pdepartid:'0000'
	};
});


//定义一个下拉框
scheduling.schedulingMngApp.gridComboBoxObj=new Ext.form.ComboBox({
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
scheduling.schedulingMngApp.number = parseInt(scheduling.schedulingMngApp.gridComboBoxObj.getValue());

//定义下拉框的选中事件
scheduling.schedulingMngApp.gridComboBoxObj.on("select", function(comboBox) {
	scheduling.schedulingMngApp.bbar.pageSize = parseInt(comboBox.getValue());
	scheduling.schedulingMngApp.number = parseInt(comboBox.getValue());
	scheduling.schedulingMngApp.gridStoreObj.reload({
				params : {
					start : 0,
					limit : scheduling.schedulingMngApp.bbar.pageSize
				}
			});
});

//定义列表面板的底部面板的工具条
scheduling.schedulingMngApp.bbar = new Ext.PagingToolbar({
	pageSize : scheduling.schedulingMngApp.number,
	store : scheduling.schedulingMngApp.gridStoreObj,
	displayInfo : true,
	displayMsg : '显示{0}条到{1}条,共{2}条',
	emptyMsg : "没有符合条件的记录",
	items : ['-', '&nbsp;&nbsp;', scheduling.schedulingMngApp.gridComboBoxObj]
});



//创建列表面板
scheduling.schedulingMngApp.gridPanelObj =new Ext.grid.EditorGridPanel({
	//title : '<span class="commoncss">表单列表</span>',
	iconCls : 'app_boxesIcon',
	height : 500,
	autoScroll : true,
	region : 'center',
	store : scheduling.schedulingMngApp.gridStoreObj,
	loadMask : {
		msg : '正在加载表格数据,请稍等...'
	},
	stripeRows : true,
	frame : true,
	//autoExpandColumn : 'menuname',
	cm : scheduling.schedulingMngApp.columModelObj,
	sm : scheduling.schedulingMngApp.sm,
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
					scheduling.schedulingMngApp.gridStoreObj.reload();
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
	bbar : scheduling.schedulingMngApp.bbar
});

function queryForm(){
	var queryParam = Ext.getCmp('queryParam').getValue();
			scheduling.schedulingMngApp.gridStoreObj.reload({
				params : {
					queryParam : queryParam,
					 start : 0, 
					 limit : scheduling.schedulingMngApp.bbar.pageSize
				}
			});
	}
	
Ext.onReady(scheduling.schedulingMngApp.init, scheduling.schedulingMngApp);
</script>