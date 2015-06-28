<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
/**
 * @module name 工作流管理
 * @module desc 流程定义管理
 * @author  wxl
 * @create  date 2012-7-7
 * @modify  man ***
 * @modify  date ***
 */
 Ext.namespace('workflowMng', 'workflowMng.processDefinedApp');

var defineId;
//create application
workflowMng.processDefinedApp = function() {
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
    	  var aimobj = Ext.mainScreem.findById('docs-流程定义管理');//获取打开得页签对象
    	  var lyobj = new Ext.layout.BorderLayout();//定义面板
    	  aimobj.setLayout(lyobj);//给页签对象设置布局格式
    	  aimobj.add(workflowMng.processDefinedApp.deptTree);
    	  aimobj.add(workflowMng.processDefinedApp.gridPanelObj);
    	  aimobj.doLayout();//强制布局
    	  
    	  

          
    	  //SysUserMng.user.organizeTreePanel.getRootNode().expand();    	  
    	  workflowMng.processDefinedApp.gridStoreObj.load({params:{start:0, limit:10}});
      }
  };
}(); // end of app

workflowMng.processDefinedApp.root = new Ext.tree.AsyncTreeNode({
	text : '流程定义类别',
	expanded : true,
	id : '0000'
});
workflowMng.processDefinedApp.deptTree = new Ext.tree.TreePanel({
	loader : new Ext.tree.TreeLoader({
				baseAttrs : {},
				dataUrl : 'showFormTypdIdTree'
			}),
	root : workflowMng.processDefinedApp.root,
	title : '<span style="font-weight:normal">流程类别</span>',
	iconCls : 'silk-grid',
	tools : [{
				id : 'refresh',
				handler : function() {
					workflowMng.processDefinedApp.deptTree.root.reload();
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
workflowMng.processDefinedApp.deptTree.on('click', function(node,e) {
	e.stopEvent();
	var deptid = node.attributes.id;
	workflowMng.processDefinedApp.gridStoreObj.load({
				params : {
					start : 0,
					limit : workflowMng.processDefinedApp.bbar.pageSize,
					node : deptid,
					groupId : deptid
				}
			});
});

function renderOpt(value, p, r){
	return String.format('<u  onclick=\"showCreateProcessPage(\'{0}\',\'{1}\')\" class=ubtn>创建</u>&nbsp;&nbsp;<u class=ubtn onclick=\"delProcessDefinedDeployment(\'{0}\',\'{1}\')\">删除</u><u onclick=\"doPulicCnt(\'{0}\')\" class=ubtn></u>&nbsp;&nbsp<u onclick=\"showAddFormTab(\'{1}\')\" class=ubtn>设置</u>',r.data['key_'],r.data['processDefId']);
}

function showCreateProcessPage(processDefKey,id){
	
	Ext.mainScreem.loadClass("createProcessInstancePage?id="+id+"&key_="+processDefKey,"创建流程实例",null);
}

function delProcessDefinedDeployment(key,deployId){
	Ext.Ajax.request({
		   url: 'deleteProcessDefinedFile',
		   method:'GET',
		   success: function(response, opts){
			   var obj = Ext.decode(response.responseText);
			   Ext.Msg.alert('系统提示', '');
		   },
		   failure: function(response, opts){
			   //var obj = Ext.decode(response.responseText);
			   Ext.Msg.alert('系统提示', '操作异常，请联系管理员.');
		   },
		   headers: {
		       'my-header': 'foo'
		   },
		   params: { processDefId: deployId }
		});
}

workflowMng.processDefinedApp.columModelObj = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
	header : '编号',
	dataIndex : 'id',
	width : 35
	//renderer : iconColumnRender
}, {
	header : '名称',
	dataIndex : 'name',
	sortable : true,
	width : 150,
	editor : new Ext.grid.GridEditor(new Ext.form.TextField({
				allowBlank : false,
				maxLength : 20
			}))

}, {
	header : 'KEY编码',
	dataIndex : 'key_',
	width : 180,
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
	header : '发布日期',
	dataIndex : 'date',
	width : 100,
	editor : new Ext.grid.GridEditor(new Ext.form.TextField({
				maxLength : 50
			}))
}, {
	header : '版本',
	dataIndex : 'version',
	id : 'menuname',
	width : 10,
	editor : new Ext.grid.GridEditor(new Ext.form.TextField({
				maxLength : 50
			}))
}, {
	header : '操作',
	dataIndex : 'departdesc111',
	width : 150,
	renderer :renderOpt
},{
	header : 'definedid',
	hidden:true,
	dataIndex : 'process_def_id'
}]);


workflowMng.processDefinedApp.gridStoreObj = new Ext.data.Store({
	// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
	pruneModifiedRecords : true,
	proxy : new Ext.data.HttpProxy({
				url : 'getProcessDefinedItemGridPost'
			}),
	reader : new Ext.data.JsonReader({
				totalProperty : 'pageInfo.totalRowNum',
				root : 'data'
			}, [{
						name : 'id'
					}, {
						name : 'name'
					}, {
						name : 'key_'
					}, {
						name : 'date'
					}, {
						name : 'processDefId'
					}, {
						name : 'version'
					}])
});







//定义加载数据传入后台的参数
workflowMng.processDefinedApp.gridStoreObj.on('beforeload', function() {0
	this.baseParams = {
		//menuid : menuid,
		pdepartid:'0000'
	};
});


//定义一个下拉框
workflowMng.processDefinedApp.gridComboBoxObj=new Ext.form.ComboBox({
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
workflowMng.processDefinedApp.number = parseInt(workflowMng.processDefinedApp.gridComboBoxObj.getValue());

//定义下拉框的选中事件
workflowMng.processDefinedApp.gridComboBoxObj.on("select", function(comboBox) {
	workflowMng.processDefinedApp.bbar.pageSize = parseInt(comboBox.getValue());
	number = parseInt(comboBox.getValue());
	workflowMng.processDefinedApp.gridStoreObj.reload({
				params : {
					start : 0,
					limit : workflowMng.processDefinedApp.bbar.pageSize
				}
			});
});

//定义列表面板的底部面板的工具条
workflowMng.processDefinedApp.bbar = new Ext.PagingToolbar({
	pageSize : workflowMng.processDefinedApp.number,
	store : workflowMng.processDefinedApp.gridStoreObj,
	displayInfo : true,
	displayMsg : '显示{0}条到{1}条,共{2}条',
	emptyMsg : "没有符合条件的记录",
	items : ['-', '&nbsp;&nbsp;', workflowMng.processDefinedApp.gridComboBoxObj]
});



//创建列表面板
workflowMng.processDefinedApp.gridPanelObj =new Ext.grid.EditorGridPanel({
	title : '<span class="commoncss">流程定义列表</span>',
	iconCls : 'silk-grid',
	height : 500,
	autoScroll : true,
	region : 'center',
	store : workflowMng.processDefinedApp.gridStoreObj,
	loadMask : {
		msg : '正在加载表格数据,请稍等...'
	},
	stripeRows : true,
	frame : true,
	autoExpandColumn : 'menuname',
	cm : workflowMng.processDefinedApp.columModelObj,
	clicksToEdit : 1,
	tbar : [{
				text : '新增',
				iconCls : 'silk-add',
				id : 'id_addRow',
				handler : function() {
					showDepartmentWin();
					//addInit();
				}
			}, '-', {
				text : '删除',
				iconCls : 'silk-delete',
				id : 'id_save',
				handler : function() {
					if (0 == 0) {
						Ext.Msg.alert('提示','系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!');
						return;
					}
					//saveOrUpdateData();
				}
			}, '-', {
				text : '刷新',
				iconCls : 'silk-table-refresh',
				handler : function() {
					workflowMng.processDefinedApp.gridStoreObj.reload();
				}
			}],
			bbar :workflowMng.processDefinedApp.bbar
});



Ext.onReady(workflowMng.processDefinedApp.init, workflowMng.processDefinedApp);
</script>