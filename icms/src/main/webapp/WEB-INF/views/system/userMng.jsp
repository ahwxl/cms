<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<script type="text/javascript">

//自定义验证密码

Ext.apply(Ext.form.VTypes,{ 
password : function(val, field) 
{ 
        if (field.initialPassField) 
{ 
            var pwd = Ext.getCmp(field.initialPassField); 
            return (val == pwd.getValue()); 
        } 
        return true; 
    }, 
    passwordText : '确认密码有误，请重新输入！' 
});

/**
 * 用户管理
 */
 Ext.namespace('SysUserMng', 'SysUserMng.user');

//create application
SysUserMng.user = function() {
  // do NOT access DOM from here; elements don't exist yet

  // private variables
  var dragZone1, dragZone2;

  // private functions

  // public space
  return {
      // public properties, e.g. strings to translate

      // public methods
      init: function() {
    	  
    	  var aimobj = Ext.mainScreem.findById('docs-用户管理');
    	  var lyobj = new Ext.layout.BorderLayout();
    	  aimobj.setLayout(lyobj);
    	  aimobj.add(SysUserMng.user.organizeTreePanel);
    	  aimobj.add(SysUserMng.user.userGridPanel);
    	  aimobj.doLayout();
    	  
    	  
    	  SysUserMng.user.organizeTreePanel.getRootNode().expand();
    	  
    	  SysUserMng.user.userGridStore.load({params:{start:0, limit:10}});
      }
  };
}(); // end of app

//organize tree panel
SysUserMng.user.organizeTreePanel = new Ext.tree.TreePanel({
		
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
							SysUserMng.user.organizeTreePanel.root.reload()
						}
					}],
		    collapsible : true,
			autoScroll : false,
			animate : false,
			useArrows : false,
			border : false
		
		
		
		
}); 
SysUserMng.user.organizeTreePanel1 = new Ext.tree.TreePanel({
		
		useArrows: true,
	    //autoScroll: true,
	    width:240,
	    height:280,
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
							SysUserMng.user.organizeTreePanel.root.reload()
						}
					}],
		    collapsible : true,
			autoScroll : false,
			animate : false,
			useArrows : false,
			border : false
		
		
		
		
}); 
SysUserMng.user.organizeTreePanel.on('beforeload', function(node) {
	this.loader.baseParams = {
		pdepartid:node.id
	};
});

SysUserMng.user.organizeTreePanel.on('click', function(node,e) {
	        e.stopEvent();
	        SysUserMng.user.userGridStore.removeAll();
	        /**/
			if (!node.isLeaf()) {
				SysUserMng.user.userGridStore.load(
							 {
							 params :{deptId:node.id,start : 0,limit : SysUserMng.user.bbar.pageSize}
							 }
						  );
				return;
			}
			
		});
SysUserMng.user.organizeTreePanel1.on('beforeload', function(node) {
	this.loader.baseParams = {
		pdepartid:node.id
	};
});

SysUserMng.user.organizeTreePanel1.on('click', function(node,e) {
	        e.stopEvent();

				Ext.getCmp("deptName").setValue(node.text);
				Ext.getCmp("deptId").setValue(node.id);
				Ext.getCmp("updatedeptName").setValue(node.text);
				Ext.getCmp("updatedeptid").setValue(node.text);
				return;
		
			
		});

// 复选框
SysUserMng.user.sm = new Ext.grid.CheckboxSelectionModel();

// 定义自动当前页行号
SysUserMng.user.rownum = new Ext.grid.RowNumberer({
			header : 'NO',
			width : 28
		});
SysUserMng.user.clm = new Ext.grid.ColumnModel([SysUserMng.user.rownum, SysUserMng.user.sm,
    {
		header : '用户编号',
		dataIndex : 'userId',
		hidden : true,
		width : 120
	}, {
		header : '登陆账号',
		dataIndex : 'userName',
		sortable : true,
		width : 150
	}, {
		header : '用户名',
		dataIndex : 'loginName',
		width : 120
	}, {
		header : '所在部门编号',
		dataIndex : 'deptId',
		//id : 'menuname',
		hidden : true,
		width : 150
	},{
		header : '所在部门',
		dataIndex : 'deptName',
		width : 150
	}, {
		header : '性别',
		dataIndex : 'sex',
		width : 60,
		renderer : function(value) {
		if (value == '1')
			return '男';
		else if (value == '2')
			return '女';
		else
			return value;
	}
	}, {
		header : '用户状态',
		dataIndex : 'enabled',
		width : 60,
		renderer : function(value) {
		if (value == '2')
			return '锁定';
		else if (value == '1')
			return '正常';
		else
			return value;
	}
	}, {
		header : '是否锁定',
		dataIndex : 'locked',
		hidden : true,
		width : 60
	}, {
		header : '用户描述',
		dataIndex : 'remark',
		width : 200
	}
	]);

SysUserMng.user.userGridStore = new Ext.data.Store({
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


SysUserMng.user.pagesize_combo = new Ext.form.ComboBox({
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

SysUserMng.user.number = parseInt(SysUserMng.user.pagesize_combo.getValue());

SysUserMng.user.pagesize_combo.on("select", function(comboBox) {
	SysUserMng.user.bbar.pageSize = parseInt(comboBox.getValue());
	SysUserMng.user.number = parseInt(comboBox.getValue());
	SysUserMng.user.userGridStore.reload({
				params : {
					start : 0,
					limit : SysUserMng.user.bbar.pageSize
				}
			});
});

SysUserMng.user.bbar = new Ext.PagingToolbar({
	pageSize : SysUserMng.user.number,
	store : SysUserMng.user.userGridStore,
	displayInfo : true,
	displayMsg : '显示{0}条到{1}条,共{2}条',
	emptyMsg : "没有符合条件的记录",
	items : ['-', '&nbsp;&nbsp;', SysUserMng.user.pagesize_combo]
});


//user grid panel
SysUserMng.user.userGridPanel = new Ext.grid.GridPanel({
		
		title : '<span class="commoncss">用户列表</span>',
		//iconCls : 'app_boxesIcon',
		//height : 500,
		autoScroll : true,
		region : 'center',
		store : SysUserMng.user.userGridStore,
		loadMask : {
			msg : '正在加载表格数据,请稍等...'
		},
		stripeRows : true,
		frame : true,
		//autoExpandColumn : 'menuname',
		cm : SysUserMng.user.clm,
		sm : SysUserMng.user.sm, 
		clicksToEdit : 1,
		tbar : [{
					text : '新增',
					iconCls : 'addIcon',
					id : 'id_addRow',
					handler : function() {
						userWin.show();
			            Ext.getCmp("deptName").setValue("");
						Ext.getCmp("deptId").setValue("");
						Ext.getCmp("updatedeptName").setValue("");
						Ext.getCmp("updatedeptid").setValue("");
					}
				}, '-', {
					text : '修改',
					iconCls : 'acceptIcon',
					id : 'id_save',
					handler : function() {
						Ext.getCmp("deptName").setValue("");
						Ext.getCmp("deptId").setValue("");
						Ext.getCmp("updatedeptName").setValue("");
						Ext.getCmp("updatedeptid").setValue("");
						updateUser();
					}
				}, '-', {
					text : '刷新',
					iconCls : 'arrow_refreshIcon',
					handler : function() {
						SysUserMng.user.userGridStore.reload();
					}
				}, '-', {
					text : '删除',
					iconCls : 'arrow_refreshIcon',
					handler : function() {
						delUser();
					}
				}, '-', {
					text : '重置密码',
					iconCls : 'arrow_refreshIcon',
					handler : function() {
						resetPassword();
					}
				}, '->', new Ext.form.TextField( {
						id : 'queryParam',
						fieldLabel : '用户名',
						name : 'queryParam',
						emptyText : '请输入用户名',
						enableKeyEvents : true,
						listeners : {
							specialkey : function(field, e) {
								if (e.getKey() == Ext.EventObject.ENTER) {
									queryDeptItem();
								}
							}
						},
						width : 130
					}), {
						text : '查询',
						iconCls : 'previewIcon',
						handler : function() {
							queryUserItem();
						}
					},'-',
					    {
						text : '重置',
						iconCls : 'btn-reset',
						scope : this,
						handler : function() {
							Ext.getCmp('queryParam').setValue('');
						}
						},
                       '-'
				],
		bbar : SysUserMng.user.bbar
		
		
});

	function queryUserItem(){
	var queryParam = Ext.getCmp('queryParam').getValue();
			SysUserMng.user.userGridStore.reload({
				params : {
					queryParam : queryParam,
					 start : 0, 
					 limit : SysUserMng.user.bbar.pageSize
				}
			});
	}	

var addUserForm = new Ext.FormPanel({
    labelAlign: 'left',
    title: '新增用户',
    buttonAlign:'center',
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,
    frame:true,
    labelWidth:60,
      items: [{
        xtype:'fieldset',
        title: '新增用户',
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
                fieldLabel: '登陆账号',
                name: 'userName',
                allowBlank:false,
                emptyText:'登陆账号不能为空', 
                anchor:'90%'
            },{                     
                cls : 'key',
                xtype:'textfield',
                fieldLabel: '用户名',
                allowBlank:false,
                emptyText:'用户名不能为空', 
                name: 'loginName',
                anchor:'90%'
            },{
				fieldLabel: '用户状态',
				xtype:"panel",//panel
				layout:"column",
				isFormField:true,
				items: [{
			         boxLabel: '启用',
			         name: 'locked',
			         inputValue: '1',
			         checked: true,//默认选中
			           xtype:"radio"//单选扭类型
			         },{
			         boxLabel: '锁定',
			         name: 'locked',
			         inputValue: '2',
			         xtype:"radio"
			         }]
				},
				{                     
                cls : 'key',
                xtype:'textarea',
                fieldLabel: '用户描述',
                name: 'remark',
                width:650,
                anchor:'90%'
            }
            ]
        },{
            columnWidth:.5,
            layout: 'form',
            border:false,
            items: [{
                cls : 'key',
                xtype:'textfield',
                       inputType : "password",
                fieldLabel: '口令',
                allowBlank:false,
                name: 'userPwd',
                id: 'userPwd1',
                anchor:'90%'
            },{
                cls : 'key',
                inputType : 'password',
                vtype : "password",
                xtype : "textfield",
                fieldLabel: '确认口令',
                allowBlank:false,
                name: 'passwd',
                vtypeText:"两次密码不一致！", 
                initialPassField : 'userPwd1', // id of the initial         
                id: 'passwd',
                anchor:'90%'
            },{                     
                cls : 'key',
                xtype:'textfield',
                readOnly:false,
                fieldLabel: '所属部门',
                id:'deptName',
                allowBlank:false,
                emptyText:'所属部门不能为空', 
                name: 'deptName',
                anchor:'90%',
                listeners:{
                 'focus':function(){
                     show_win.show();                    	  
    	             SysUserMng.user.organizeTreePanel1.getRootNode().expand();
                }}
            },{                     
                cls : 'key',
                xtype:'textfield',
                fieldLabel: '所属部门编号',
                name: 'deptId',
                id: 'deptId',
                hidden: true,
                anchor:'90%'
            },{
				fieldLabel: '性别',
				xtype:"panel",//panel
				layout:"column",
				isFormField:true,
				items: [{
			         boxLabel: '男',
			         name: 'sex',
			         inputValue: '1',
			         checked: true,//默认选中
			           xtype:"radio"//单选扭类型
			         },{
			         boxLabel: '女',
			         name: 'sex',
			         inputValue: '2',
			         xtype:"radio"
			         }]
				}
            ]
        }]
    }] 
  }]
,
    buttons: [{
        text: '保存',
        handler: function(){
            if(addUserForm.getForm().isValid()){
            	addUserForm.getForm().submit({
                    url: 'addSysUser',
                    waitMsg: '正在保存请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    	    userWin.hide();
                    		SysUserMng.user.userGridStore.reload();
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
        	userWin.hide();
        }
    }]
});

var updateUserForm = new Ext.FormPanel({
    labelAlign: 'left',
    title: '修改用户',
    buttonAlign:'center',
    width: 500,
    bodyStyle:'padding:5px 5px 5px 5px',
    frame:true,
    labelWidth:60,
      items: [{
        xtype:'fieldset',
        title: '修改用户',
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
                fieldLabel: '登陆账号',
                allowBlank:false,
                emptyText:'登陆账号不能为空', 
                name: 'userName',
                anchor:'90%'
            },{
                xtype:'hidden',
                name:'userId'
            },{
                xtype:'hidden',
                name:'userType'
            },{
                xtype:'hidden',
                name:'enabled'
            },{                     
                cls : 'key',
                xtype:'textfield',
                fieldLabel: '用户名',
                allowBlank:false,
                emptyText:'用户名不能为空', 
                name: 'loginName',
                anchor:'90%'
            },{
				fieldLabel: '用户状态',
				xtype:"panel",//panel
				layout:"column",
				isFormField:true,
				items: [{
			         boxLabel: '启用',
			         name: 'locked',
			         inputValue: '1',
			         checked: true,//默认选中
			           xtype:"radio"//单选扭类型
			         },{
			         boxLabel: '锁定',
			         name: 'locked',
			         inputValue: '2',
			         xtype:"radio"
			         }]
				}
            ]
        },{
            columnWidth:.5,
            layout: 'form',
            border:false,
            items: [{
                cls : 'key',
                xtype:'textfield',
                inputType:'password',
                fieldLabel: '口令',
                allowBlank:false,
                name: 'userPwd',
                id:'userPwd2',
                hidden: true,
                anchor:'90%'
            },{                     
                cls : 'key',
                xtype:'textfield',
                readOnly:false,
                fieldLabel: '所属部门',
                allowBlank:false,
                emptyText:'所属部门不能为空', 
                id:'updatedeptName',
                name: 'deptName',
                anchor:'90%',
                listeners:{
                 'focus':function(){
                     show_win.show();                    	  
    	             SysUserMng.user.organizeTreePanel1.getRootNode().expand();
                }}
            },{                     
                cls : 'key',
                xtype:'textfield',
                fieldLabel: '所属部门编号',
                name: 'deptId',
                id:'updatedeptid',
                hidden: true,
                
                anchor:'90%'
            },{
				fieldLabel: '性别',
				xtype:"panel",//panel
				layout:"column",
				isFormField:true,
				items: [{
			         boxLabel: '男',
			         name: 'sex',
			         inputValue: '1',
			         checked: true,//默认选中
			           xtype:"radio"//单选扭类型
			         },{
			         boxLabel: '女',
			         name: 'sex',
			         inputValue: '2',
			         xtype:"radio"
			         }]
				},
				{                     
                cls : 'key',
                xtype:'textarea',
                fieldLabel: '用户描述',
                name: 'remark',
                width:650,
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
            if(updateUserForm.getForm().isValid()){
            	updateUserForm.getForm().submit({
                    url: 'updateSysUserList',
                    waitMsg: '正在保存请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    		updateUserWin.hide();
                    		SysUserMng.user.userGridStore.reload();
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
        	updateUserWin.hide();
        }
    }]
});
// 只获取选择一行
function  getOneCheckboxValue() {
	// 返回一个行集合JS数组
	var rows = SysUserMng.user.userGridPanel.getSelectionModel().getSelections();
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
	var rows = SysUserMng.user.userGridPanel.getSelectionModel().getSelections();
	if (Ext.isEmpty(rows)) {
		Ext.MessageBox.alert('提示', '您没有选中任何数据!');
		return;
	}
	var checkIdsSelect="";
			for ( var i = 0; i < rows.length; i++) {
				  checkIdsSelect += "'";
                  checkIdsSelect += rows[i].get('userId');
                  checkIdsSelect += "',";
			}
	return 	checkIdsSelect.substr(0,checkIdsSelect.length-1);
	// 将JS数组中的行级主键，生成以,分隔的字符串
	//var strChecked = jsArray2JsString(rows, 'departid');
	//Ext.MessageBox.alert('提示', strChecked);
	// 获得选中数据后则可以传入后台继续处理
}
function delUser(){
    if(getCheckboxValues()){
	var userId = getCheckboxValues();
Ext.Msg.confirm('请确认', '你确认要删除当前对象吗?', function(btn,
									text) {
								if (btn == 'yes') {

									delItem(userId);

								} else {
									return;
								}
							});
}
function delItem(userId) {

	Ext.Ajax.request({
				url : 'updateSysUser',
				success : function(response) {
					SysUserMng.user.userGridStore.reload();
					var resultArray = Ext.util.JSON
							.decode(response.responseText);
					Ext.Msg.alert('提示', resultArray.info);
				},
				failure : function(response) {
					Ext.MessageBox.alert('提示', '数据删除失败');
				},
				params : {
					userId : userId
				}
			});
			}
}
function resetPassword(){
    if(getCheckboxValues()){
	var userId = getCheckboxValues();
Ext.Msg.confirm('请确认', '你确认要重置当前用户密码吗?', function(btn,
									text) {
								if (btn == 'yes') {

									resetPwdItem(userId);

								} else {
									return;
								}
							});
}
function resetPwdItem(userId) {

	Ext.Ajax.request({
				url : 'setPwdSysUser',
				success : function(response) {
					SysUserMng.user.userGridStore.reload();
					var resultArray = Ext.util.JSON
							.decode(response.responseText);
					Ext.Msg.alert('提示', resultArray.info);
				},
				failure : function(response) {
					Ext.MessageBox.alert('提示', '重置密码失败');
				},
				params : {
					userId : userId
				}
			});
			}
}
var userWin = new Ext.Window({
    width:650, height: 340,
    layout: 'fit', // explicitly set layout manager: override the default (layout:'auto')
    closeAction:'hide',
    items: [addUserForm]
});	
var treePanelForm = new Ext.FormPanel({
    labelWidth: 75, // label settings here cascade unless overridden
    //url:'addDepartmentNew',
    frame:false,
    title: '添加部门',
    fileUpload:true,
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,
    items: [SysUserMng.user.organizeTreePanel]
    });

/** ****************弹出部门树框************************ */ 
var show_win = new Ext.Window({ 
    plain : true, 
    layout : 'form', 
    resizable : true, // 改变大小 
    draggable : true, // 不允许拖动 
    closeAction : 'hide',// 可被关闭 close or hide 
    modal : true, // 模态窗口 
    width : 260, 
    height : 350, 
    title : '所属部门', 
    items : [SysUserMng.user.organizeTreePanel1], 
    buttonAlign : 'right', 
    loadMask : true, 
    buttons : [{ 
       xtype : 'button', 
       align : 'right', 
       text : '确定', 
       handler : function() { 
        show_win.hide(); 
       } 
      }, { 
       xtype : 'button', 
       text : '取消', 
       handler : function() { 
        show_win.hide(); 
       } 
      }] 
   });
var updateUserWin = new Ext.Window({
    width:650, height: 350,
    layout: 'fit', // explicitly set layout manager: override the default (layout:'auto')
    closeAction:'hide',
    items: [updateUserForm]
});
//显示修改部门窗口
function showUpdateUserWin(){

	updateUserWin.show();
	var rows =  SysUserMng.user.userGridPanel.getSelectionModel().getSelections();
	updateUserForm.getForm().findField('userId').setValue(rows[0].get('userId'));
	updateUserForm.getForm().findField('userName').setValue(rows[0].get('userName'));
	updateUserForm.getForm().findField('userPwd').setValue(rows[0].get('userPwd'));
	updateUserForm.getForm().findField('sex').setValue(rows[0].get('sex'));
	updateUserForm.getForm().findField('deptId').setValue(rows[0].get('deptId'));
	updateUserForm.getForm().findField('deptName').setValue(rows[0].get('deptName'));
	updateUserForm.getForm().findField('locked').setValue(rows[0].get('locked'));
	updateUserForm.getForm().findField('remark').setValue(rows[0].get('remark'));
	updateUserForm.getForm().findField('enabled').setValue(rows[0].get('enabled'));
	updateUserForm.getForm().findField('userType').setValue(rows[0].get('userType'));
	updateUserForm.getForm().findField('loginName').setValue(rows[0].get('loginName'));
}
function updateUser(){
if(getOneCheckboxValue()){
showUpdateUserWin();
}
}
Ext.onReady(SysUserMng.user.init, SysUserMng.user);

</script>