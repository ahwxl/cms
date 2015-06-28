<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
/**
 * @module name 工作流管理
 * @module desc 流程任务管理
 * @author  wxl
 * @create  date 2012-7-7
 * @modify  man ***
 * @modify  date ***
 */
 Ext.namespace('workflowMng', 'workflowMng.processTaskMngApp');


//create application
workflowMng.processTaskMngApp = function() {
  // do NOT access DOM from here; elements don't exist yet

  //此处定义私有属性变量
  var dragZone1, dragZone2;

  //此处定义私有方法
var addArticleForm ={title:'abc'};
  //共有区
  return {
      //此处定义共有属性变量
      

      //共有方法
      init: function() {
    	  
          //定义一些初始化行为
    	  var aimobj = Ext.mainScreem.findById('docs-流程任务管理');//获取打开得页签对象
    	  var lyobj = new Ext.layout.BorderLayout();//定义面板
    	  aimobj.setLayout(lyobj);//给页签对象设置布局格式
    	  aimobj.add(workflowMng.processTaskMngApp.deptTree);
    	  //aimobj.add(workflowMng.processTaskMngApp.gridPanelObj);
    	  aimobj.add(workflowMng.processTaskMngApp.mainPanel);
    	  aimobj.doLayout();//强制布局
    	  
    	  

          
    	  //SysUserMng.user.organizeTreePanel.getRootNode().expand();
    	  workflowMng.processTaskMngApp.gridStoreObj.load({params:{start:0, limit:10}});
      }
  };
}(); // end of app

workflowMng.processTaskMngApp.root = new Ext.tree.AsyncTreeNode({
	text : '流程类别',
	expanded : true,
	id : '0000'
});
workflowMng.processTaskMngApp.deptTree = new Ext.tree.TreePanel({
	loader : new Ext.tree.TreeLoader({
				baseAttrs : {},
				dataUrl : 'showFormTypdIdTree'
			}),
	root : workflowMng.processTaskMngApp.root,
	title : '<span style="font-weight:normal">流程分类</span>',
	iconCls : 'chart_organisationIcon',
	tools : [{
				id : 'refresh',
				handler : function() {
					workflowMng.processTaskMngApp.deptTree.root.reload();
				}
			}],
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
workflowMng.processTaskMngApp.deptTree.on('click', function(node,e) {
	e.stopEvent();
	deptid = node.attributes.id;
	workflowMng.processTaskMngApp.gridStoreObj.load({
				params : {
					start : 0,
					limit : workflowMng.processTaskMngApp.bbar.pageSize,
					node : deptid,
					deptid : deptid
				}
			});
});

function renderOpt(value, p, r){
	return String.format('<u  onclick=\"showApproveProcessPage(\'{0}\')\" class=ubtn>审批</u>&nbsp;&nbsp;<u onclick=\"doDelProc(\'{0}\')\" class=ubtn>删除</u>&nbsp;&nbsp;<u onclick=\"doCommission(\'{0}\')\" class=ubtn>代办</u>&nbsp;&nbsp;',r.data['id']);
}

function showApproveProcessPage(id){
	
	Ext.mainScreem.loadClass("viewProcessInsApprovePage?id="+id,"审批流程",null);
}

function doCommission(id){
	Ext.example.msg('代办', 'You chose {0}.', id);
}

function doDelProc(id){
	Ext.example.msg('删除实例', 'You chose {0}.', id);
}



workflowMng.processTaskMngApp.columModelObj = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
	header : '任务编号',
	dataIndex : 'id',
	width : 90
	//renderer : iconColumnRender
}, {
	header : '流程名称',
	dataIndex : 'taskDefKey',
	sortable : true,
	width : 150,
	editor : new Ext.grid.GridEditor(new Ext.form.TextField({
				allowBlank : false,
				maxLength : 20
			}))

}, {
	header : '创建日期',
	dataIndex : 'createDate',
	width : 150,
          //renderer : CMPTYPERender,
	editor : new Ext.grid.GridEditor(new Ext.form.ComboBox({
				//store : CMPTYPEStore,
				mode : 'local',
				listWidth : 408,
				triggerAction : 'all',
				valueField : 'value',
				displayField : 'text',
				allowBlank : false,
				resizable : true,
				forceSelection : true,
				editable : false,
				typeAhead : true
			}))
}, {
	header : '当前操作人',
	dataIndex : 'assignee',
	width : 100,
	editor : new Ext.grid.GridEditor(new Ext.form.TextField({
				maxLength : 50
			}))
}, {
	header : '当前步骤',
	dataIndex : 'name',
	id : 'menuname',
	width : 190
}, {
	header : '操作',
	dataIndex : 'departdesc111',
	width : 150,
	renderer:renderOpt
	
}]);


workflowMng.processTaskMngApp.gridStoreObj = new Ext.data.Store({
	// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
	pruneModifiedRecords : true,
	proxy : new Ext.data.HttpProxy({
				url : 'getProcessTaskListByJson'
			}),
	reader : new Ext.data.JsonReader({
				totalProperty : 'totalCount',
				root : 'data'
			}, [{
						name : 'id'
					}, {
						name : 'name'
					}, {
						name : 'createDate'
					}, {
						name : 'assignee'
					}])
});







//定义加载数据传入后台的参数
workflowMng.processTaskMngApp.gridStoreObj.on('beforeload', function() {0
	this.baseParams = {
		//menuid : menuid,
		pdepartid:'0000'
	};
});


//定义一个下拉框
workflowMng.processTaskMngApp.gridComboBoxObj=new Ext.form.ComboBox({
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
	value : '50',
	editable : false,
	width : 85
});

//定义全局变量 用于记录下拉框选中的变量
workflowMng.processTaskMngApp.number = parseInt(workflowMng.processTaskMngApp.gridComboBoxObj.getValue());

//定义下拉框的选中事件
workflowMng.processTaskMngApp.gridComboBoxObj.on("select", function(comboBox) {
	workflowMng.processTaskMngApp.bbar.pageSize = parseInt(comboBox.getValue());
	number = parseInt(comboBox.getValue());
	workflowMng.processTaskMngApp.gridStoreObj.reload({
				params : {
					start : 0,
					limit : workflowMng.processTaskMngApp.bbar.pageSize
				}
			});
});

//定义列表面板的底部面板的工具条
workflowMng.processTaskMngApp.bbar = new Ext.PagingToolbar({
	pageSize : workflowMng.processTaskMngApp.number,
	store : workflowMng.processTaskMngApp.gridStoreObj,
	displayInfo : true,
	displayMsg : '显示{0}条到{1}条,共{2}条',
	emptyMsg : "没有符合条件的记录",
	items : ['-', '&nbsp;&nbsp;', workflowMng.processTaskMngApp.gridComboBoxObj]
});

workflowMng.processTaskMngApp.searchPanel = new Ext.FormPanel({
        region:"north"
        ,height:35
        ,frame:false
        ,border:false
        ,id:"AppUserSearchForm"
        ,layout:"hbox" //hbox
        ,layoutConfig: {
            padding:"5"
            ,align:"middle"
        }
        ,defaults: {
            xtype:"label"
            ,border:false
            ,margins: {
                top:0
                ,right:4
                ,bottom:4
                ,left:4
            }
        }
        ,items:[ {
            text:"流程名称"
        }
        , {
            xtype:"textfield"
            ,name:"Q_username_S_LK"
        }
        , {
            text:"当前操作人"
        }
        , {
            xtype:"textfield"
            ,name:"Q_fullname_S_LK"
        }
        , {
            text:"创建日期:从"
        }
        , {
            xtype:"datefield"
            ,format:"Y-m-d"
            ,name:"Q_accessionTime_D_GT"
        }
        , {
            text:"至"
        }
        , {
            xtype:"datefield"
            ,format:"Y-m-d"
            ,name:"Q_accessionTime_D_LT"
        }
        , {
            xtype:"button"
            ,text:"查询"
            ,iconCls:"search"
            ,scope:this
        }
        ]
    });


//创建列表面板
workflowMng.processTaskMngApp.gridPanelObj =new Ext.grid.EditorGridPanel({
	title : '<span class="commoncss">任务列表</span>',
	iconCls : 'app_boxesIcon',
	height : 450,
	autoHeight:false,
	autoScroll : true,
	region : 'center',
	store : workflowMng.processTaskMngApp.gridStoreObj,
	loadMask : {
		msg : '正在加载表格数据,请稍等...'
	},
	stripeRows : true,
	frame : true,
	autoExpandColumn : 'menuname',
	cm : workflowMng.processTaskMngApp.columModelObj,
	clicksToEdit : 1,
	//tbar : [],
	bbar : workflowMng.processTaskMngApp.bbar
});

workflowMng.processTaskMngApp.mainPanel = new Ext.Panel({
	region : 'center',
	autoHeight:true,
	items:[workflowMng.processTaskMngApp.searchPanel,workflowMng.processTaskMngApp.gridPanelObj]
});

Ext.onReady(workflowMng.processTaskMngApp.init, workflowMng.processTaskMngApp);
 //ert(addArticleForm.title);
</script>