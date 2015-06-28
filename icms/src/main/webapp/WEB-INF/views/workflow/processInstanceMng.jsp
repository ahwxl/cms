<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
/**
 * @module name 工作流管理
 * @module desc 流程实例管理
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
    	  var aimobj = Ext.mainScreem.findById('docs-流程实例管理');//获取打开得页签对象
    	  var lyobj = new Ext.layout.BorderLayout();//定义面板
    	  aimobj.setLayout(lyobj);//给页签对象设置布局格式
    	  aimobj.add(workflowMng.processInstanceMngApp.deptTree);
    	  //aimobj.add(workflowMng.processInstanceMngApp.gridPanelObj);
    	  aimobj.add(workflowMng.processInstanceMngApp.mainPanel);
    	  aimobj.doLayout();//强制布局
    	  
    	  

          
    	  //SysUserMng.user.organizeTreePanel.getRootNode().expand();    	  
    	  workflowMng.processInstanceMngApp.gridStoreObj.load({params:{start:0, limit:10}});
      }
  };
}(); // end of app

workflowMng.processInstanceMngApp.root = new Ext.tree.AsyncTreeNode({
	text : '流程类别',
	expanded : true,
	id : '0000'
});
workflowMng.processInstanceMngApp.deptTree = new Ext.tree.TreePanel({
	loader : new Ext.tree.TreeLoader({
				baseAttrs : {},
				dataUrl : 'showFormTypdIdTree'
			}),
	root : workflowMng.processInstanceMngApp.root,
	title : '<span style="font-weight:normal">流程定义类别</span>',
	iconCls : 'chart_organisationIcon',
	tools : [{
				id : 'refresh',
				handler : function() {
					workflowMng.processInstanceMngApp.deptTree.root.reload();
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
workflowMng.processInstanceMngApp.deptTree.on('click', function(node,e) {
	e.stopEvent();
	deptid = node.attributes.id;
	workflowMng.processInstanceMngApp.gridStoreObj.load({
				params : {
					start : 0,
					limit : workflowMng.processInstanceMngApp.bbar.pageSize,
					node : deptid,
					deptid : deptid
				}
			});
});

function renderOpt(value, p, r){
	return String.format('<u onclick=\"doDelProc(\'{0}\')\" class=ubtn>删除</u>&nbsp;&nbsp;<u onclick=\"doShowDiagram(\'{0}\')\" class=ubtn>查看</u>',r.data['id']);
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



workflowMng.processInstanceMngApp.columModelObj = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
	header : '流程编号',
	dataIndex : 'id',
	width : 65
	//renderer : iconColumnRender
}, {
	header : '流程名称',
	dataIndex : 'name',
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
	dataIndex : 'curentAgentUserName',
	width : 100,
	editor : new Ext.grid.GridEditor(new Ext.form.TextField({
				maxLength : 50
			}))
}, {
	header : '当前步骤',
	dataIndex : 'curentStep',
	id : 'menuname',
	width : 150
}, {
	header : '操作',
	dataIndex : 'departdesc111',
	width : 200,
	renderer:renderOpt
	
}]);


workflowMng.processInstanceMngApp.gridStoreObj = new Ext.data.Store({
	// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
	pruneModifiedRecords : true,
	proxy : new Ext.data.HttpProxy({
				url : 'getProcessInstanceItemJson'
			}),
	reader : new Ext.data.JsonReader({
				totalProperty : 'pageInfo.totalRowNum',
				root : 'data'
			}, [{
						name : 'id'
					}, {
						name : 'name'
					}, {
						name : 'createDate'
					}, {
						name : 'processDefinedId'
					}, {
						name : 'curentStep'
					}, {
						name : 'curentAgentUserName'
					}, {
						name : 'processInsStartUserName'
					}, {
						name : 'version'
					}])
});







//定义加载数据传入后台的参数
workflowMng.processInstanceMngApp.gridStoreObj.on('beforeload', function() {0
	this.baseParams = {
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
	number = parseInt(comboBox.getValue());
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

workflowMng.processInstanceMngApp.searchPanel = new Ext.FormPanel({
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
workflowMng.processInstanceMngApp.gridPanelObj =new Ext.grid.EditorGridPanel({
	title : '<span class="commoncss">流程实例列表</span>',
	iconCls : 'app_boxesIcon',
	height : 450,
	autoHeight:false,
	autoScroll : true,
	region : 'center',
	store : workflowMng.processInstanceMngApp.gridStoreObj,
	loadMask : {
		msg : '正在加载表格数据,请稍等...'
	},
	stripeRows : true,
	frame : true,
	autoExpandColumn : 'menuname',
	cm : workflowMng.processInstanceMngApp.columModelObj,
	clicksToEdit : 1,
	//tbar : [],
	bbar : workflowMng.processInstanceMngApp.bbar
});

workflowMng.processInstanceMngApp.mainPanel = new Ext.Panel({
	region : 'center',
	autoHeight:true,
	items:[workflowMng.processInstanceMngApp.searchPanel,workflowMng.processInstanceMngApp.gridPanelObj]
});


var AppUserChooserWin = function(config){
	this.config = config;
}

AppUserChooserWin.prototype = {
	show : function(el, callback){
		if(!this.win){			
			this.treepanel = new Ext.tree.TreePanel({
				title:'组织机构',
				useArrows: true,
			    //autoScroll: true,
			    width:200,
			    animate: true,
			    enableDD: true,
			    containerScroll: true,
			    region : 'west',
			    border: false,
			    // auto create TreeLoader
			    loader : new Ext.tree.TreeLoader({
								baseAttrs : {},
								baseParams:{pdepartid:'0000'},
								dataUrl : 'getDepartmentTreeDataJsonNew'
				}),

			    root: {
			        nodeType: 'async',
			        text: '组织机构',
			        draggable: false,
			        id: '0000'
			    },
			    tools : [{
								id : 'refresh',
								handler : function() {
									AppUserChooserWin.treepanel.root.reload()
								}
							}],
				    collapsible : true,
					autoScroll : false,
					animate : false,
					useArrows : false,
					border : false
		    });
			
			
			this.sm = new Ext.grid.CheckboxSelectionModel();
			this.clm = new Ext.grid.ColumnModel([this.sm,
			                                {
			                            		header : '用户名',
			                            		dataIndex : 'userName',
			                            		width : 120
			                            	}
			                            	]);
			
			this.userGridStore = new Ext.data.Store({
				// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
				pruneModifiedRecords : true,
				proxy : new Ext.data.HttpProxy({
							url : 'getSysUserGridJson'
						}),
				reader : new Ext.data.JsonReader({
							totalProperty : 'pageInfo.totalRowNum',
							root : 'data'
						}, [    {
									name : 'userId'
								}, {
									name : 'userName'
								}, {
									name : 'loginName'
								}, {
									name : 'deptId'
								}, {
									name : 'deptName'
								}, {
									name : 'sex'
								}, {
									name : 'enabled'
								}, {
									name : 'locked'
								}, {
									name : 'remark'
								}, {
									name : 'userPwd'
								}
								])
			});
			
			this.bbar = new Ext.PagingToolbar({
				pageSize : 12,
				store : this.userGridStore,
				displayInfo : true,
				displayMsg : '显示{0}条到{1}条,共{2}条',
				emptyMsg : "没有符合条件的记录",
				items : ['-', '&nbsp;&nbsp;']
			});
			
			this.userGridPanel = new Ext.grid.GridPanel({
				
				title : '<span class="commoncss">用户列表</span>',
				//iconCls : 'app_boxesIcon',
				//height : 500,
				width: 300,
				autoScroll : true,
				region : 'center',
				store : this.userGridStore,
				loadMask : {
					msg : '正在加载表格数据,请稍等...'
				},
				stripeRows : true,
				frame : true,
				cm : this.clm,
				sm : this.sm, 
				clicksToEdit : 1,
				listeners:{
						afterrender:function (thisgrid){
							//alert();
							thisgrid.getStore().reload({params:{start:0,limit:10}});
						}
					},
				bbar : this.bbar
		     });
			 
			var cfg = {
			    	title: '选择用户',
			    	id: 'user-chooser-dlg',
			    	layout: 'border',
			    	plain : true, 
					draggable : true,
					//layout : 'form',
					//layout: 'fit',
					width: 600,
					height: 500,
					modal: true,
					closeAction: 'hide',
					border: false,
					items:[this.treepanel,this.userGridPanel],
					
					buttons: [{
						id: 'ok-btn',
						text: 'OK',
						handler: this.doCallback,
						scope: this
					},{
						text: 'Cancel',
						handler: function(){ this.win.hide(); },
						scope: this
					}],
					keys: {
						key: 27, // Esc key
						handler: function(){ this.win.hide(); },
						scope: this
					}
				};
				Ext.apply(cfg, this.config);
			    this.win = new Ext.Window(cfg);
		}
		
		this.win.show();
		this.callback = callback;
		this.animateTarget = el;
	},
	doCallback : function(){
        var recArray = this.sm.getSelections();
		if(recArray.length ==0){
			return alert("请选择");
		}
		this.callback(recArray);
		this.win.hide();
    }
	
}

workflowMng.processInstanceMngApp.procDiagramPanel = function(config){
	this.config = config;
}

workflowMng.processInstanceMngApp.procDiagramPanel.prototype = {
	showP:function(procid){
		if(!this.win){
			/*
			var cfg = {
			    	title: '显示流程图',
			    	id:'processInsDiagram',
			    	plain : true,
			    	layout: 'border',
			    	width: 500,
					height: 400,
					modal: true,
					closeAction: 'hide',
					items:[{title:'',html:''}],
				   	border: false
				    
		    };
			*/
			var cfg = {
					title: '显示流程图',
				    height: 400,
				    width: 500,
				    autoScroll:true,
				    items:[
				    	new Ext.BoxComponent({
				    	    autoEl: {
				    	        tag: 'img',
				    	        //height:600,
				    	        //width:800,
				    	        src: 'loadDiagramStream?processInstId='+procid
				    	    }
				    	})
				    	]
				    
				};
			
			
			//Ext.apply(cfg, this.config);
			this.win = new Ext.Window(cfg);
			//var w = new Ext.Window().show();

			
			//diagramwin.show();
	    }
		this.win.show();
    }
};

//显示流程图
function doShowDiagram(id){
	var obj = new workflowMng.processInstanceMngApp.procDiagramPanel();
	obj.showP(id);
	//obj.win.show();
}







Ext.onReady(workflowMng.processInstanceMngApp.init, workflowMng.processInstanceMngApp);
</script>