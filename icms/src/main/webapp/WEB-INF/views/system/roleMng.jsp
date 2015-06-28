<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>

<script type="text/javascript">
var root = new Ext.tree.AsyncTreeNode({
	text : '组织机构',
	expanded : true,
	id : '0000'
});
var deptTree = new Ext.tree.TreePanel({
	loader : new Ext.tree.TreeLoader({
				baseAttrs : {},
				dataUrl : 'showCatalogTree'
			}),
	root : root,
	title : '<span style="font-weight:normal">组织机构</span>',
	iconCls : 'chart_organisationIcon',
	tools : [{
				id : 'refresh',
				handler : function() {
					deptTree.root.reload()
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
deptTree.on('click', function(node,e) {
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



var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
	header : '授权',
	dataIndex : 'roleId',
	renderer : function(value, cellmeta, record) {
		return '<a href="javascript:void(0);"><img src="./resources/images/fam/cog_edit.png"/></a>';
	},
	width : 35
}, {
	header : '角色名称',
	dataIndex : 'roleName',
	width : 150
}, {
	id : 'deptName',
	header : '所属部门',
	dataIndex : 'deptname',
	width : 150
}, {
	header : '角色类型',
	dataIndex : 'roleType',
	width : 80,
	renderer : function(value) {
		if (value == '1')
			return '业务角色';
		else if (value == '2')
			return '管理角色';
		else if (value == '3')
			return '系统内置角色';
		else
			return value;
	}
}, {
	header : '角色状态',
	dataIndex : 'locked',
	width : 60,
	renderer : function(value) {
		if (value == '1')
			return '锁定';
		else if (value == '0')
			return '正常';
		else
			return value;
	}
}, {
	header : '角色编号',
	dataIndex : 'roleId',
	hidden : false,
	width : 80,
	sortable : true
}, {
	id : 'remark',
	header : '备注',
	dataIndex : 'remark'
}, 
{
	id : 'remark',
	header : '操作',
	renderer: renderOpt,//操作调用
	dataIndex : 'remark1'
},
{
	id : 'deptid',
	header : '所属部门编号',
	dataIndex : 'deptId',
	hidden : true
}]);

var store = new Ext.data.Store({
			proxy : new Ext.data.HttpProxy({
						url : 'getRoleGridDataJson'//http://localhost:9696/G4Studio/role.ered?reqCode=queryRolesForManage
					}),
			reader : new Ext.data.JsonReader({
						totalProperty : 'pageInfo.totalRowNum',
						root : 'data'
					}, [{
								name : 'roleId'
							}, {
								name : 'roleName'
							}, {
								name : 'locked'
							}, {
								name : 'roleType'
							}, {
								name : 'deptId'
							}, {
								name : 'deptName'
							}, {
								name : 'remark'
							}])
		});

// 翻页排序时带上查询条件
store.on('beforeload', function() {
			this.baseParams = {
				queryParam : Ext.getCmp('queryParam').getValue()
			};
		});

var pagesize_combo = new Ext.form.ComboBox({
			name : 'pagesize',
			hiddenName : 'pagesize',
			typeAhead : true,
			triggerAction : 'all',
			lazyRender : true,
			mode : 'local',
			store : new Ext.data.ArrayStore({
						fields : ['value', 'text'],
						data : [[10, '10条/页'], [20, '20条/页'],
								[50, '50条/页'], [100, '100条/页'],
								[250, '250条/页'], [500, '500条/页']]
					}),
			valueField : 'value',
			displayField : 'text',
			value : '50',
			editable : false,
			width : 85
		});
var number = parseInt(pagesize_combo.getValue());
pagesize_combo.on("select", function(comboBox) {
			bbar.pageSize = parseInt(comboBox.getValue());
			number = parseInt(comboBox.getValue());
			store.reload({
						params : {
							start : 0,
							limit : bbar.pageSize
						}
					});
		});

var bbar = new Ext.PagingToolbar({
			pageSize : number,
			store : store,
			displayInfo : true,
			displayMsg : '显示{0}条到{1}条,共{2}条',
			emptyMsg : "没有符合条件的记录",
			items : ['-', '&nbsp;&nbsp;', pagesize_combo]
		});

var grid = new Ext.grid.GridPanel({
			title : '<span style="font-weight:normal">角色信息</span>',
			iconCls : 'award_star_silver_3Icon',
			height : 500,
			// width:600,
			autoScroll : true,
			region : 'center',
			store : store,
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			},
			stripeRows : true,
			frame : true,
			autoExpandColumn : 'remark',
			cm : cm,
			tbar : [new Ext.form.TextField({
								id : 'queryParam',
								name : 'queryParam',
								emptyText : '请输入角色名称',
								enableKeyEvents : true,
								listeners : {
									specialkey : function(field, e) {
										if (e.getKey() == Ext.EventObject.ENTER) {
											queryRoleItem();
										}
									}
								},
								width : 130
							}), {
						text : '查询',
						iconCls : 'previewIcon',
						handler : function() {
							queryRoleItem();
						}
							
					},'-',{
						text : '新增',
						iconCls : 'previewIcon',
						id : 'id_addRole',
						handler : function() {
							showRoleWin();
						}	
					}, '-', {
						text : '刷新',
						iconCls : 'arrow_refreshIcon',
						handler : function() {
							store.reload();
						}
					}],
			bbar : bbar
		});
store.load({
			params : {
				start : 0,
				limit : bbar.pageSize,
				node :'11',
				firstload : 'true'
			}
		});

grid.on("cellclick", function(grid, rowIndex, columnIndex, e) {
			var store = grid.getStore();
			var record = store.getAt(rowIndex);
			var fieldName = grid.getColumnModel().getDataIndex(columnIndex);
			if (fieldName == 'roleid' && columnIndex == 1) {
				roleid = record.get('roleid');
				userGrantWindow.show();
			}
		});

var viewpanel = new Ext.Panel({
	layout : 'border',
	items : [{
				title : '<span style="font-weight:normal">组织机构</span>',
				iconCls : 'chart_organisationIcon',
				tools : [{
							id : 'refresh',
							handler : function() {
								deptTree.root.reload()
							}
						}],
				collapsible : true,
				//width : 210,
				height:500,
				//minSize : 160,
				//maxSize : 280,
				split : true,
				region : 'west',
				autoScroll : true,
				// collapseMode:'mini',
				items : [deptTree]
			}, grid]
});


function queryRoleItem() {
	var selectModel = deptTree.getSelectionModel();
	var selectNode = selectModel.getSelectedNode();
	var deptid = selectNode.attributes.id;
	store.load({
				params : {
					start : 0,
					limit : bbar.pageSize,
					queryParam : Ext.getCmp('queryParam').getValue(),
					deptid : deptid
				}
			});
}
//操作,r.data['cnt_id']
function renderOpt(value, p, r){
    	return String.format('<u  onclick=\"doShowRoleInfo(\'{0}\')\" >修改</u>  <u onclick=\"delItem(\'{0}\')\">删除 </u>',r.data['roleId']);
 
    }
//删除
function delItem(roleId) {
	
	//alert(roleId);
	Ext.Ajax.request({
				url : 'delRole',
				success : function(response) {
					store.reload();
					var resultArray = Ext.util.JSON
							.decode(response.responseText);
					Ext.Msg.alert('提示', resultArray.info);
				},
				failure : function(response) {
					Ext.MessageBox.alert('提示', '数据删除失败');
				},
				params : {
					roleId : roleId
				}
			});
}




//修改    
function doShowRoleInfo(roleId){
    	
    	var url = 'doShowRoleInfo?roleId='+roleId;

    	/**
    	*打开一个iframe窗口
    	*/
    	Ext.mainScreem.addNewTab(Ext.getmifObj({id:'getRoleGridDataJson',title:'角色修改',src:url}),'getRoleGridDataJson');
    }

//添加角色的table
var addRoleForm = new Ext.FormPanel({
    labelWidth: 75, // label settings here cascade unless overridden
    url:'addRoles',
    frame:false,
    title: '添加角色',
    fileUpload:true,
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,

    items: [{
        xtype:'fieldset',
        title: 'Phone Number',
        collapsible: true,
        autoHeight:true,
        defaults: {width: 210},
//        defaultType: 'textfield',
        items :[{
        	    xtype: 'textfield',
                fieldLabel: '角色名称',
                name: 'roleName',
                value: ''
            },{
            	xtype: 'textfield',
                fieldLabel: '所属部门',
                name: 'deptId'
            },{
            	xtype: 'textfield',
                fieldLabel: '角色类型',
                name: 'roleType'
            },{
            	xtype: 'textfield',
                fieldLabel: '角色状态',
                name: 'locked'
            },{
            	xtype: 'textfield',
                fieldLabel: '备注',
                name: 'remark'
            }
        ]
    }],

    buttons: [{
        text: '保存',
        handler: function(){
            if(addRoleForm.getForm().isValid()){
            	addRoleForm.getForm().submit({
                    url: 'addRole',
                    waitMsg: '正在上传请稍等...',
                    success: function(form, action){
                        store.reload();//保存后自动刷新
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
        	roleWin.hide();
        }
    }]
});

var roleWin = new Ext.Window({
    width:500, height: 500,
    layout: 'fit', // explicitly set layout manager: override the default (layout:'auto')
    title:'添加部门',
    closeAction:'hide',
    items: [addRoleForm]
});
//显示添加部门窗口
function showRoleWin(){

	roleWin.show();
	addRoleForm.getForm().findField('id').setValue(menuTree.getSelectionModel().getSelectedNode().attributes.id);
	addRoleForm.getForm().findField('name').setValue(menuTree.getSelectionModel().getSelectedNode().attributes.text);
	
}

















//Ext.mainScreem.getActiveTab().add(viewpanel);
//Ext.mainScreem.getActiveTab().removeAll();

//alert(Ext.mainScreem.getActiveTab().ownerCt);
//alert(Ext.mainScreem.getActiveTab().ownerCt.layout.type);
var aimobj = Ext.mainScreem.findById('docs-角色管理');
var objly = Ext.mainScreem.getActiveTab().ownerCt.layout;
//aimobj.removeAll();
var ct = aimobj.ownerCt;
//ct = aimobj.refOwner;
//ct = aimobj.getBubbleTarget();
ct = aimobj.container;//container

//alert(ct);
//alert(ct.items);
//alert(ct.items.getCount());

//alert(objly.override);
//objly.override({type:'border'});

var lyobj = new Ext.layout.BorderLayout();
aimobj.setLayout(lyobj);



aimobj.add(deptTree);
aimobj.add(grid);
aimobj.doLayout();

//alert(aimobj.items.getCount());
/**/
var pystr = '';
for(var obj in ct){
	//alert(obj);
	//alert(ct[obj]);
	pystr =  pystr+'  ,'+ obj;
	//alert(obj);
}
//alert(pystr);

/*
new Ext.Window({
    width:500, height: 600,
    layout: 'fit', // explicitly set layout manager: override the default (layout:'auto')
    items: [{
        title: 'Panel inside a Window',
        autoScroll : true,
        html:pystr
    }]
}).show();
*/


/*
for(var obj in objly){
	alert(obj);
	alert(objly[obj]);
	//alert(obj);
}*/
//type 
//overide({type:'border'});




</script>



    
    
    
    
    