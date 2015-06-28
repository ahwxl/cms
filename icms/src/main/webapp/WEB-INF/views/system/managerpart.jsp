<%@ page language="java" pageEncoding="utf-8" %>

<script type="text/javascript">

/**
组织结构管理
*/

var webRoot ="/travel";
var menuid;
var root = new Ext.tree.AsyncTreeNode({
			text : '组织机构',
			expanded : true,
			id : '0000'
		});
var menuTree = new Ext.tree.TreePanel({
			loader : new Ext.tree.TreeLoader({
						baseAttrs :{},
						//nodeParameter: 'pdepartid',
						baseParams:{pdepartid:'0000'},//默认参数是node
						dataUrl : 'getDepartmentTreeDataJsonNew'
					}),
			root : root,
			title : '组织机构',
			//applyTo : 'menuTreeDiv',
			//autoHeight:true,
			region : 'west',
			width:190,
			//height :350,
			tools : [{
						id : 'refresh',
						handler : function() {
							menuTree.root.reload()
						}
					}],
		    collapsible : true,
			autoScroll : false,
			animate : false,
			useArrows : false,
			border : false
		});

menuTree.on('beforeload', function(node) {
	this.loader.baseParams = {
		pdepartid:node.id
	};
});

menuTree.on('click', function(node,e) {
	        e.stopEvent();
	        /**/
			if (!node.isLeaf()) {
				store.load(
							 {
							 params :{pdepartid:node.id,start : 0,limit : bbar.pageSize}
							 }
						  );
				return;
			}
			Ext.getCmp('id_addRow').setDisabled(false);
			menuid = node.id;
			store.load({
						params : {
							start : 0,
							limit : bbar.pageSize,
							menuid : menuid
						}
					});
			
		});

// 复选框
var sm = new Ext.grid.CheckboxSelectionModel();

// 定义自动当前页行号
var rownum = new Ext.grid.RowNumberer({
			header : 'NO',
			width : 28
		});
var cm = new Ext.grid.ColumnModel([rownum, sm, {
			header : '部门编码',
			dataIndex : 'departid',
			width : 180
		}, {
			header : '部门名称',
			dataIndex : 'departname',
			width : 200
		},{
			header : '部门描述',
			dataIndex : 'departdesc',
			//id : 'menuname',
			width : 150
		},{
			header : '排序编号',
			dataIndex : 'sortid',
			id : 'sortid',
			width : 150,
			hidden : true
		}
		]);

var rec_part = new Ext.data.Record.create([{
			name : 'departid',
			type : 'string'
		}, {
			name : 'departname',
			type : 'string'
		}, {
			name : 'departdesc',
			type : 'string'
		}, {
			name : 'pdepartid',
			type : 'string'
		}, {
			name : 'sortid',
			type : 'string'
		}]);

var store = new Ext.data.Store({
			// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
			pruneModifiedRecords : true,
			proxy : new Ext.data.HttpProxy({
						url : 'getDepartmentGridDataJsonNew'
					}),
			reader : new Ext.data.JsonReader({
						totalProperty : 'pageInfo.totalRowNum',
						root : 'data'
					}, [
					        {
								name : 'departid'
							}, {
								name : 'departname'
							}, {
								name : 'departdesc'
							}, {
								name : 'pdepartid'
							}, {
								name : 'sortid'
							}])
		});

store.on('beforeload', function() {0
			this.baseParams = {
				menuid : menuid,
				pdepartid:'0000'
			};
		});

store.on('load', function() {
			if (store.getTotalCount() == 0) {
				// Ext.getCmp('id_addRow').setDisabled(true);
				Ext.getCmp('id_update').setDisabled(true);
				Ext.getCmp('id_del').setDisabled(true);
			} else {
				// Ext.getCmp('id_addRow').setDisabled(false);
				Ext.getCmp('id_update').setDisabled(false);
				Ext.getCmp('id_del').setDisabled(false);
			}

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
			title : '<span class="commoncss">组织机构列表</span>',
			iconCls : 'app_boxesIcon',
			height : 500,
			autoScroll : true,
			region : 'center',
			store : store,
			loadMask : {
				msg : '正在加载表格数据,请稍等...'
			},
			stripeRows : true,
			frame : true,
			//autoExpandColumn : 'menuname',
			cm : cm,
			sm : sm,
			clicksToEdit : 1,
			tbar : [{
						text : '新增',
						iconCls : 'addIcon',
						id : 'id_addRow',
						handler : function() {
							showDepartmentWin();
							//addInit();
						}
					}, '-', {
						text : '修改',
						iconCls : 'acceptIcon',
						id : 'id_update',
						handler : function() {
							//if (runMode == '0') {
								//Ext.Msg.alert('提示','系统正处于演示模式下运行,您的操作被取消!该模式下只能进行查询操作!');
								//return;
							//}
							updateDepartment();
						}
					},'-', {
						text : '删除',
						iconCls : 'arrow_refreshIcon',
						id : 'id_del',
						handler : function() {
							delDepartment();
						}
					}, '-', {
						text : '刷新',
						iconCls : 'arrow_refreshIcon',
						handler : function() {
							store.reload();
						}
					}, '->', new Ext.form.TextField( {
						id : 'queryParam',
						fieldLabel : '组织名称',
						name : 'queryParam',
						emptyText : '请输入部门名称',
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
							queryDeptItem();
						}
					},'-',
					    {
						text : '重置',
						iconCls : 'btn-reset',
						scope : this,
						handler : function() {
							DeptItemReset();
						}
						},
                       '-'
					],
			bbar : bbar
		});

grid.on("cellclick", function(pGrid, rowIndex, columnIndex, e) {
			store = pGrid.getStore();
			var record = store.getAt(rowIndex);
			var fieldName = pGrid.getColumnModel()
					.getDataIndex(columnIndex);
			if (fieldName == 'delete' && columnIndex == 1) {
				var dirtytype = record.get('dirtytype');
				if (Ext.isEmpty(dirtytype)) {
					Ext.Msg.confirm('请确认', '你确认要删除当前对象吗?', function(btn,
									text) {
								if (btn == 'yes') {

									delItem(record.get('departid'));

								} else {
									return;
								}
							});
				} else {
					store.remove(record);

				}
			}
		});
		
	function queryDeptItem(){
	var queryParam = Ext.getCmp('queryParam').getValue();
			store.reload({
				params : {
					queryParam : queryParam,
					 start : 0, 
					 limit : bbar.pageSize
				}
			});
	}	
		
	function DeptItemReset() {
				Ext.getCmp('queryParam').setValue('');
			};

/*
 * store.load({ params : { start : 0, limit : bbar.pageSize } });
 */

//新打开一个页签
//Ext.mainScreem.addNewTab(viewport,  'managerpart');
//关闭TABpanel自己生成的那个panel,自动生成的那个tab需要加载一段html代码，这段代码又要重新加载到该tab，以下js无法实现，只好重新生成一个tab，将第一次打开得删除
//Ext.mainScreem.remove('docs-组织机构管理');

var aimobj = Ext.mainScreem.findById('docs-组织机构管理');
var lyobj = new Ext.layout.BorderLayout();
aimobj.setLayout(lyobj);
aimobj.add(menuTree);
aimobj.add(grid);
aimobj.doLayout();


function addInit() {
	var selectModel = menuTree.getSelectionModel();
	var selectNode = selectModel.getSelectedNode();
	var rec = new rec_part({});
	rec.set('partid', '保存后自动生成');
	rec.set('menuid', selectNode.id);
	rec.set('menuname', selectNode.text);
	rec.set('dirtytype', '1');
	grid.stopEditing();
	store.insert(0, rec);
	grid.startEditing(0, 2);
	// store.getAt(0).dirty=true; //不起作用
	store.getAt(0).set('cmpid', '不能为空,请输入');
	// 通过这种办法变相的将新增行标识为脏数据
	Ext.getCmp('id_save').setDisabled(false);
}

function saveOrUpdateData() {
alert(m);
	var m = store.modified.slice(0);
	if (Ext.isEmpty(m)) {
		Ext.MessageBox.alert('提示', '没有数据需要保存!');
		return;
	}

	for (var i = 0; i < m.length; i++) {
		var record = m[i];
		var rowIndex = store.indexOfId(record.id);
		if (Ext.isEmpty(record.get('cmpid'))) {
			Ext.Msg.alert('提示', '元素Dom标识字段数据校验不合法,请重新输入!', function() {
						grid.startEditing(rowIndex, 3);
					});
			return false;
		}
		if (Ext.isEmpty(record.get('cmptype'))) {
			Ext.Msg.alert('提示', '元素类型字段数据校验不合法,请重新输入!', function() {
						grid.startEditing(rowIndex, 4);
					});
			return false;
		}
	}

	var jsonArray = [];
	Ext.each(m, function(item) {
				jsonArray.push(item.data);
			});

	Ext.Ajax.request({
				url : 'part.ered?reqCode=saveDirtyDatas',
				success : function(response) {

					var resultArray = Ext.util.JSON
							.decode(response.responseText);
					if (resultArray.bflag == '1') {
						Ext.getCmp('id_save').setDisabled(true);
						store.reload();
					}
					Ext.Msg.alert('提示', resultArray.msg);
				},
				failure : function(response) {
					Ext.MessageBox.alert('提示', '数据保存失败');
				},
				params : {
					dirtydata : Ext.encode(jsonArray)
				}
			});
}
// 只获取选择一行
function  getOneCheckboxValue() {
	// 返回一个行集合JS数组
	var rows = grid.getSelectionModel().getSelections();
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
	var rows = grid.getSelectionModel().getSelections();
	if (Ext.isEmpty(rows)) {
		Ext.MessageBox.alert('提示', '您没有选中任何数据!');
		return;
	}
	var checkIdsSelect="";
			for ( var i = 0; i < rows.length; i++) {
				  checkIdsSelect += "'";
                  checkIdsSelect += rows[i].get('departid');
                  checkIdsSelect += "',";
			}
	return 	checkIdsSelect.substr(0,checkIdsSelect.length-1);
	// 将JS数组中的行级主键，生成以,分隔的字符串
	//var strChecked = jsArray2JsString(rows, 'departid');
	//Ext.MessageBox.alert('提示', strChecked);
	// 获得选中数据后则可以传入后台继续处理
}
function delDepartment(){
    if(getCheckboxValues()){
	var departid = getCheckboxValues();
Ext.Msg.confirm('请确认', '你确认要删除当前对象吗?', function(btn,
									text) {
								if (btn == 'yes') {

									delItem(departid);

								} else {
									return;
								}
							});
}
function delItem(departid) {

	Ext.Ajax.request({
				url : 'delDepartmentNew',
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
					departid : departid
				}
			});
			}
}

function iconColumnRender(value) {
	return "<a><img src='"+webRoot
			+ "/resources/images/fam/delete.gif'/></a>";;
}


var addDepartmentForm = new Ext.FormPanel({
    labelWidth: 75, // label settings here cascade unless overridden
    url:'addDepartmentNew',
    frame:false,
    title: '添加部门',
    fileUpload:true,
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,

    items: [{
        xtype:'fieldset',
        title: '添加部门',
        collapsible: true,
        autoHeight:true,
        defaults: {width: 210},
//        defaultType: 'textfield',
        items :[{
        	    xtype: 'textfield',
                fieldLabel: '部门名称',
                name: 'departname',
                value: ''
            },{
            	xtype: 'textfield',
                fieldLabel: '部门描述',
                name: 'departdesc'
            },{
            	xtype: 'textfield',
                fieldLabel: '父部门名称',
                readOnly:false,
                id:'pdepartname',
                name: 'pdepartname',
                listeners:{
                 'focus':function(){
                   Ext.getCmp("pdepartname").getEl().dom.readOnly = true;
                }}
            },{
                xtype:'hidden',
                name:'pdepartid'
            }
        ]
    }],

    buttons: [{
        text: '保存',
        handler: function(){
            if(addDepartmentForm.getForm().isValid()){
            	addDepartmentForm.getForm().submit({
                    url: 'addDepartmentNew',
                    waitMsg: '正在保存请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    		departmentWin.hide();
                    		store.reload();
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
        	departmentWin.hide();
        }
    }]
});
var updateDepartmentForm = new Ext.FormPanel({
    labelWidth: 75, // label settings here cascade unless overridden
    url:'updateDepartmentInfoNew',
    frame:false,
    title: '修改部门',
    fileUpload:true,
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,

    items: [{
        xtype:'fieldset',
        title: '修改部门',
        collapsible: true,
        autoHeight:true,
        defaults: {width: 210},
//        defaultType: 'textfield',
        items :[{
        	    xtype: 'textfield',
                fieldLabel: '部门名称',
                name: 'departname',
                value: ''
            },{
            	xtype: 'textfield',
                fieldLabel: '部门描述',
                name: 'departdesc'
            },{
            	xtype: 'textfield',
                fieldLabel: '父部门名称',
                readOnly:false,
                id:'udpdepartname',
                name: 'pdepartname',
                listeners:{
                 'focus':function(){
                   Ext.getCmp("udpdepartname").getEl().dom.readOnly = true;
                }}
            },{
                xtype:'hidden',
                name:'pdepartid'
            },{
                xtype:'hidden',
                name:'departid'
            } ,{
                xtype:'hidden',
                name:'sortid'
            }
        ]
    }],

    buttons: [{
        text: '保存',
        handler: function(){
            if(updateDepartmentForm.getForm().isValid()){
            	updateDepartmentForm.getForm().submit({
                    url: 'updateDepartmentInfoNew',
                    waitMsg: '正在保存请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    		updateDepartmentWin.hide();
                    		store.reload();
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
        	updateDepartmentWin.hide();
        }
    }]
});

var departmentWin = new Ext.Window({
    width:500, height: 300,
    layout: 'fit', // explicitly set layout manager: override the default (layout:'auto')
    closeAction:'hide',
    items: [addDepartmentForm]
});
var updateDepartmentWin = new Ext.Window({
    width:500, height: 300,
    layout: 'fit', // explicitly set layout manager: override the default (layout:'auto')
    closeAction:'hide',
    items: [updateDepartmentForm]
});

//显示添加部门窗口
function showDepartmentWin(){

	departmentWin.show();
	if(menuTree.getSelectionModel().getSelectedNode()==null){
		addDepartmentForm.getForm().findField('pdepartid').setValue("0000");
	    addDepartmentForm.getForm().findField('pdepartname').setValue("组织机构");
	}
	else{
		addDepartmentForm.getForm().findField('pdepartid').setValue(menuTree.getSelectionModel().getSelectedNode().attributes.id);
		addDepartmentForm.getForm().findField('pdepartname').setValue(menuTree.getSelectionModel().getSelectedNode().attributes.text);
	}
	
}
//显示修改部门窗口
function showUpdateDepartmentWin(){

	updateDepartmentWin.show();
	var rows = grid.getSelectionModel().getSelections();
	updateDepartmentForm.getForm().findField('departid').setValue(rows[0].get('departid'));
	updateDepartmentForm.getForm().findField('departname').setValue(rows[0].get('departname'));
	updateDepartmentForm.getForm().findField('departdesc').setValue(rows[0].get('departdesc'));
	updateDepartmentForm.getForm().findField('sortid').setValue(rows[0].get('sortid'));
	updateDepartmentForm.getForm().findField('pdepartid').setValue(menuTree.getSelectionModel().getSelectedNode().attributes.id);
	updateDepartmentForm.getForm().findField('pdepartname').setValue(menuTree.getSelectionModel().getSelectedNode().attributes.text);
	
}
//menuTree.root.select();
function updateDepartment(){
if(getOneCheckboxValue()){
showUpdateDepartmentWin();
}
}

</script>