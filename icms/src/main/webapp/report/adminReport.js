Ext.onReady(function() {

	function queryReport() {
		reportList_store.baseParams = {
			name : Ext.getCmp('name').getValue(),
			description : Ext.getCmp('description').getValue(),
			reportGroupId : Ext.getCmp('reportGroupId').getValue()
		};

		reportList_store.load({
			params : {
				start : 0,
				limit : reportList_bbar.pageSize
			}
		}); //查询
	}
	
		
	var reportGroupListAll_store = new Ext.data.JsonStore({
		method:'post',
		url : '../report/getReportGroupListAll',
		fields : [ 'id', 'name', 'description' ]
	});
	
	var ReportDataSourceListAll_store = new Ext.data.JsonStore({
		method:'post',
		url : '../report/getReportDataSourceListAll',
		fields : [ 'id', 'name', 'driverClassName', 'url', 'username', 'password', 'maxIdle', 'maxActive', 'maxWait', 'validationQuery', 'jndi' ]
	});
	
	var reportTemplateList_store = new Ext.data.ArrayStore({
		fields : ['templateFile'],
		method:'post',
		url : '../report/getReportTemplateList'
	});
	
	var reportGroupAll_combo = new Ext.form.ComboBox({ 
		fieldLabel:'所属报表组',
		id:'reportGroupId', 
        name:'reportGroupId', 
        emptyText:'--请选择--', 
        editable: false, 
        triggerAction: 'all', 
        store:reportGroupListAll_store, 
        mode : 'remote', //默认远程数据加载
        valueField : 'id',  //值 
        displayField : 'name'   //显示下拉框内容 
    });
    
    var reportGroupType_combo = new Ext.form.ComboBox({
		name : 'type',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : [ 'value', 'text' ],
			data : [ [ '0', '实时报表' ], [ '1', '日报表' ], [ '2', '周报表' ],
					[ '3', '旬报表' ], [ '4', '月报表' ], [ '5', '季报表' ],
					[ '6', '年报表' ] ]
		}),
		valueField : 'value',
		displayField : 'text',
		value : '0',
		editable : false,
		width : 85
	});
	
	var fpFileUpload=new Ext.FormPanel({
		id:'fpFileUpload',
		frame:true,
		fileUpload:true,
		//url:'Default.aspx',
		items:[
			{
				xtype:'textfield',
				allowBlank:false,
				fieldLabel:'选择文件',
				inputType:'file',
				name:'templateFile'
			}
		],
		buttonAlign:'center',
		buttons:[
			{
				text:'上传',
				handler:function(){
					if(fpFileUpload.form.isValid()){
						fpFileUpload.form.submit({
							method:'post',
							url:'../report/uploadReportTemplate',
							waitMsg:'文件上传中...',
							success: function(form, action){
		                    	//Ext.Msg.alert("系统提示", "文件上传成功！");
		                    	Ext.MessageBox.alert('Success', action.result.info);
		                    	if(action.result.info=='{success:true}') {
		                    		reportTemplateList_store.reload();
		                    	}
		                    },
		                    failure :function(fm,rp){	                    	
		                    	//Ext.Msg.alert("系统提示", "文件上传失败！");
		                    	Ext.MessageBox.alert(rp.result);
		                    	if(rp.result=='{success:true}') {
		                    		reportTemplateList_store.reload();
		                    	}
		                    }
						});
					}else{
						Ext.Msg.alert("系统提示","请选择文件后再上传！");
					}
				}
			},
			{
				text:'取消',
				handler:function(){
					winFielUpload.hide();
				}
			}
		]
	});
	
	var winFielUpload=new Ext.Window({
		id:'win',
		title:'文件上传',
		//****renderTo:'divWindow',//对于window不要使用renderTo属性，只需要调用show方法就可以显示，添加此属性会难以控制其位置
		width:350,
		height:100,
		layout:'fit',
		autoDestory:true,
		modal:true,
		closeAction:'hide',
		items:[
			fpFileUpload
		]
	});
				
	function showFielUploadWindow(){
			winFielUpload.show();
	}

	var reportAddPan_form = new Ext.form.FormPanel({
		labelAlign : 'right',
		bodyStyle : 'padding:5 5 5 5',
		items : [ {
			layout : 'column',
			border : false,
			items : [ {
				columnWidth : 1,
				layout : 'form',
				labelWidth : 80, // 标签宽度
				defaultType : 'textfield',
				border : false,
				items : [ {
					fieldLabel : '报表名称',
					name : 'name',
					id : 'name',
					anchor : '100%',
					maxLength : 40,
					maxLengthText : '报表名称过长',
					minLength : 1,
					minLengthText : '报表名称过短'
				}, {
					fieldLabel : '报表描述',
					height : 50,
					name : 'description',
					id : 'description',
					anchor : '100%',
					maxLength : 40,
					maxLengthText : '报表描述过长',
					minLength : 1,
					minLengthText : '报表描述过短'
				}, {
					xtype:'combo',
					fieldLabel:'所属报表组',
			        name:'reportGroupName', 
			        emptyText:'--请选择--', 
			        editable: false, 
			        triggerAction: 'all', 
			        store:reportGroupListAll_store, 
			        mode : 'remote', //默认远程数据加载
			        valueField : 'id',  //值 
			        hiddenName:"reportGroupId",
			        displayField : 'name'   //显示下拉框内容 
				}, {
					xtype:'combo',
					fieldLabel:'数据源',
			        name:'dataSourceId', 
			        emptyText:'--请选择--', 
			        editable: false, 
			        triggerAction: 'all', 
			        store:ReportDataSourceListAll_store, 
			        mode : 'remote', //默认远程数据加载
			        valueField : 'id',  //值 
			        hiddenName:"dataSourceId",
			        displayField : 'name'   //显示下拉框内容 
				}, {
					fieldLabel : '查询语句',
					height:50,
					name : 'query',
					id : 'query',
					anchor : '100%'
				}, {
					xtype:'combo',
					fieldLabel:'报表模板',
					name : 'file',
					triggerAction : 'all',
					allowBlank:false,
					mode : 'remote',
					store : reportTemplateList_store,
					valueField : 'templateFile',
					displayField : 'templateFile',
					editable : false
				}, {
					xtype:'button',
					text:'上传模板',
					handler:showFielUploadWindow
				}, {
					xtype:'combo',
					fieldLabel:'报表类型',
					name : 'type',
					triggerAction : 'all',
					allowBlank:false,
					mode : 'local',
					store : new Ext.data.ArrayStore({
						fields : [ 'value', 'text' ],
						data : [ [ '0', '实时报表' ], [ '1', '日报表' ], [ '2', '周报表' ],
								[ '3', '旬报表' ], [ '4', '月报表' ], [ '5', '季报表' ],
								[ '6', '年报表' ] ]
					}),
					valueField : 'value',
					hiddenName:"type",
					displayField : 'text',
					value : '0',
					editable : false
				}, {
					name : 'id',
					id : 'id',
					hidden : true
				} ]
			} ]
		} ]
	});
	
	var reportAddPan_windows = new Ext.Window({
		closeAction : 'hide',
		title : '添加报表',
		layout : 'fit',
		width : 430,
		height : 450,
		closable : true,
		collapsible : true,
		maximizable : true,
		border : false,
		modal : true,
		constrain : true,
		animateTarget : Ext.getBody(),
		items : [ reportAddPan_form ],
		buttons : [ {
			text : '提交',
			handler : function() {
				submitForm();
			}
		}, {
			text : '取消',
			handler : function() {
				reportAddPan_windows.hide();
			}
		} ]
	});

	function submitForm() {
		if (!reportAddPan_form.form.isValid())
			return;
		reportGroupAddPan_type = true;
		if (reportGroupAddPan_type == false) {
			return;
		}
		reportAddPan_form.form.submit({
			url : '../report/saveOrUpdateReport',
			waitTitle : '提示',
			method : 'POST',
			waitMsg : '正在处理数据,请稍候...',
			success : function(form, action) {
				reportAddPan_windows.hide();
				reportAddPan_form.form.reset();
				queryReport();
			},
			failure : function(form, action) {
				//Ext.MessageBox.alert('提示', '数据保存失败');
				reportAddPan_windows.hide();
				reportAddPan_form.form.reset();
				queryReport();
			}
		});
	}
	
	function reportParameter_form_submitForm() {
		if (!reportParameter_form.form.isValid())
			return;
		reportParameter_form_type = true;
		if (reportParameter_form_type == false) {
			return;
		}
		reportParameter_form.form.submit({
			url : '../report/saveOrUpdateReportParameterMap',
			waitTitle : '提示',
			method : 'POST',
			waitMsg : '正在处理数据,请稍候...',
			success : function(form, action) {
				reportParameter_form.form.reset();
				queryReportParameterMap();
			},
			failure : function(form, action) {
				//Ext.MessageBox.alert('提示', '数据保存失败');
				reportParameter_form.form.reset();
				queryReportParameterMap();
			}
		});
	}
	
	
	var reportParameterListAll_store = new Ext.data.JsonStore({
		method:'post',
		url : '../report/getReportParameterListAll',
		fields : [ 'id', 'name', 'type', 'className', 'dataSourceId', 'data', 'description', 'required', 'multipleSelect', 'defaultValue' ]
	});
	
	var reportParameterMapList_store = new Ext.data.JsonStore({
		root : '',
		remoteSort : false,
		fields : [ 'id', 'reportId', 'parameterId', 'parameterName', 'required', 'sortOrder' ],
		proxy : new Ext.data.HttpProxy({
			url : '../report/getReportParameterMapList'
		})
	});
	
	
	var reportParameter_form = new Ext.form.FormPanel({
		width : 430,
		height : 200,
		labelAlign : 'right',
		bodyStyle : 'padding:5 5 5 5',
		items : [ {
			layout : 'column',
			border : false,
			items : [ {
				columnWidth : 1,
				layout : 'form',
				labelWidth : 80, // 标签宽度
				defaultType : 'textfield',
				border : false,
				items : [ {
					fieldLabel : '报表ID',
					name : 'reportId',
					id : 'reportId',
					hidden:true
				}, {
					xtype:'combo',
					fieldLabel:'报表参数',
			        name:'parameterId', 
			        emptyText:'--请选择--', 
			        editable: false, 
			        triggerAction: 'all', 
			        store:reportParameterListAll_store, 
			        mode : 'remote', //默认远程数据加载
			        valueField : 'id',  //值 
			        hiddenName:"parameterId",
			        displayField : 'name'   //显示下拉框内容 
				}, {
					fieldLabel : '排序',
					name : 'sortOrder',
					id : 'sortOrder'
				} ]
			} ]
		} ],
		buttons : [ {
			text : '提交',
			handler : function() {
				reportParameter_form_submitForm();
			}
		}, {
			text : '取消',
			handler : function() {
				reportParameter_windows.hide();
			}
		} ]
	});
	
	function queryReportParameterMap() {
		reportParameterMapList_store.baseParams = {
			reportId : Ext.getCmp('reportId').getValue()
		};

		reportParameterMapList_store.load(); //查询
	}
	
	var reportParameterMapList_rownum = new Ext.grid.RowNumberer({
		header : '序号',
		width : 40
	});
	var reportParameterMapList_sm = new Ext.grid.CheckboxSelectionModel();
	
	var reportParameterMapList_grid = new Ext.grid.GridPanel({
		width : 430,
		autoHeight:true,
		title : '参数列表',
		region : 'center',
		store : reportParameterMapList_store,
		disableSelection : false,
		loadMask : true,
		columns : [ reportParameterMapList_rownum,reportParameterMapList_sm,{
			header : 'ID',
			dataIndex : 'id',
			sortable : true
		}, {
			header : '参数名称',
			dataIndex : 'parameterName',
			sortable : true
		}, {
			header : '排序',
			dataIndex : 'sortOrder',
			sortable : true
		}],
		stripeRows : true,
		sm : reportParameterMapList_sm,
		loadMask : {
			msg : '正在加载表格数据,请稍等...'
		},
		tbar:[ {
                    text:"删除",
                    handler:reportParameterMap_deleteRow,
                    scope:this
                }]
	});
	
	function reportParameterMap_deleteRow() {
		var rows = reportParameterMap_getSelect();
		if (rows != 0) {
			Ext.MessageBox.confirm('提示', '您确认删除当前选择的' + rows.length + '条记录吗？',
					function(btn) {
						if (btn == 'yes') {
							var parameterMapIds = [];
							for ( var i = 0; i < rows.length; i++) {
								parameterMapIds.push(rows[i].get('id'));
							}
							
							Ext.Ajax.request({
								url : '../report/deleteReportParameterMap',
								method : 'post',
								success : function(result, request) {
									queryReportParameterMap();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示', '请求数据失败！');
								},
								sync : true,
								params : {
									reportId:Ext.getCmp('reportId').getValue(),
									parameterMapIds : parameterMapIds
								}
							});
						}
					});

		}
	}
	
	function reportParameterMap_getSelect() {
		var rows = reportParameterMapList_grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			Ext.MessageBox.alert('提示', '您当前没有选中任何数据!');
			return 0;
		}
		return rows;
	}
	
	var reportParameter_windows = new Ext.Window({
		closeAction : 'hide',
		title : '报表参数管理',
		layout : 'fit',
		width : 430,
		height : 400,
		closable : true,
		collapsible : true,
		maximizable : true,
		border : false,
		modal : true,
		constrain : true,
		animateTarget : Ext.getBody(),
		items : [ reportParameterMapList_grid, reportParameter_form ]
	});

	function reportList_deleteRow() {
		var rows = reportList_getSelect();
		if (rows != 0) {
			Ext.MessageBox.confirm('提示', '您确认删除当前选择的' + rows.length + '条记录吗？',
					function(btn) {
						if (btn == 'yes') {
							var reportIds = [];
							for ( var i = 0; i < rows.length; i++) {
								reportIds.push(rows[i].get('id'));
							}
							
							Ext.Ajax.request({
								url : '../report/deleteReport',
								method : 'post',
								success : function(result, request) {
									queryReport();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示', '请求数据失败！');
								},
								sync : true,
								params : {
									reportIds : reportIds
								}
							});
						}
					});

		}
	}
	
	function reportParameter_Manager() {
		var rows = reportList_getSelect();
		if (rows != 0) {
			if (rows.length == 1) {
				Ext.getCmp('reportId').setValue(rows[0].get('id'));
				queryReportParameterMap();
				reportParameter_windows.show();
			} else {
				Ext.MessageBox.alert('提示', '只能选择一个');
			}
		}
	}

	var reportList_tbar = new Ext.Toolbar({
		items : ['报表名称:', {
			xtype : 'textfield',
			id : 'name',
			name : 'name',
			emptyText : '请输入报表名称',
			width : 150,
			enableKeyEvents : true,
			// 响应回车键
			listeners : {
				specialkey : function(field, e) {
					if (e.getKey() == Ext.EventObject.ENTER) {
						queryReport();
					}
				}
			}
		}, '报表描述:', {
			xtype : 'textfield',
			id : 'description',
			name : 'description',
			emptyText : '请输入报表描述',
			width : 150,
			enableKeyEvents : true,
			// 响应回车键
			listeners : {
				specialkey : function(field, e) {
					if (e.getKey() == Ext.EventObject.ENTER) {
						queryReport();
					}
				}
			}
		}, '所属报表组:', reportGroupAll_combo, {
			text : '查询',
			iconCls : 'page_findIcon',
			handler : function() {
				queryReport();
			}
		}, {
			text : '刷新',
			iconCls : 'page_refreshIcon',
			handler : function() {
				reportList_store.reload();
			}
		}, '-', {
			text : '删除',
			iconCls : 'deleteIcon',
			handler : function() {
				reportList_deleteRow();
			}

		}, {
			text : '添加报表',
			iconCls : 'addIcon',
			handler : function() {
				reportAddPan_form.form.reset();
				reportAddPan_windows.show();
			}
		}, {
			text : '修改报表',
			iconCls : 'acceptIcon',
			handler : function() {
				if (reportGroupList_fillElement(reportAddPan_form)) {
					reportAddPan_windows.show();
				}
			}
		}, {
			text : '报表参数管理',
			iconCls : 'page_edit_1Icon',
			handler : function() {
				reportParameter_Manager();
			}
		} ]
	});

	var typeList_pagesize_combo = new Ext.form.ComboBox({
		name : 'pageSize',
		triggerAction : 'all',
		mode : 'local',
		store : new Ext.data.ArrayStore({
			fields : [ 'value', 'text' ],
			data : [ [ 10, '10条/页' ], [ 20, '20条/页' ], [ 50, '50条/页' ],
					[ 100, '100条/页' ], [ 250, '250条/页' ], [ 500, '500条/页' ] ]
		}),
		valueField : 'value',
		displayField : 'text',
		value : '20',
		editable : false,
		width : 85
	});
	var reportGroupList_number = parseInt(typeList_pagesize_combo.getValue());
	typeList_pagesize_combo.on('select', function(comboBox) {
		reportList_bbar.pageSize = parseInt(comboBox.getValue());
		reportGroupList_number = parseInt(comboBox.getValue());
		reportList_store.reload({
			params : {
				start : 0,
				limit : reportList_bbar.pageSize
			}
		});
	});
	var typeList_rownum = new Ext.grid.RowNumberer({
		header : '序号',
		width : 40
	});
	var typeList_sm = new Ext.grid.CheckboxSelectionModel();
	var reportList_store = new Ext.data.JsonStore({
		root : 'dataList',
		totalProperty : 'totalCount',
		remoteSort : false,
		fields : [ 'id', 'name', 'description', 'reportGroupId', 'reportGroupName', 'dataSourceId', 'query', 'file', 'type' ],
		proxy : new Ext.data.HttpProxy({
			url : '../report/queryReport'
		})
	});

	var reportList_bbar = new Ext.PagingToolbar({
		pageSize : reportGroupList_number,
		store : reportList_store,
		beforePageText : '当前第',
		afterPageText : '页，共{0}页',
		lastText : '尾页',
		nextText : '下一页',
		prevText : '上一页',
		firstText : '首页',
		plugins : new Ext.ux.ProgressBarPager(),
		displayInfo : true,
		displayMsg : '当前显示 {0} - {1}条, 共 {2}',
		emptyMsg : '没有记录',
		items : [ '-', '&nbsp;&nbsp;', typeList_pagesize_combo ]
	});

	// Ext.get('grid').getHeight()
	var reportList_grid = new Ext.grid.GridPanel({
		autoHeight : true,
		monitorResize : true,
		title : '报表',
		frame : true,
		autoScroll : true,
		region : 'center',
		store : reportList_store,
		disableSelection : false,
		loadMask : true,
		columns : [ typeList_rownum, typeList_sm, {
			header : '报表ID',
			dataIndex : 'id',
			sortable : true
		}, {
			header : '报表名称',
			dataIndex : 'name',
			sortable : true
		}, {
			header : '报表描述',
			dataIndex : 'description',
			sortable : true
		}, {
			header : '所属报表组',
			dataIndex : 'reportGroupName',
			sortable : true
		}],
		stripeRows : true,
		sm : typeList_sm,
		bbar : reportList_bbar,
		tbar : reportList_tbar,
		viewConfit : {
			forceFit : true
		},
		loadMask : {
			msg : '正在加载表格数据,请稍等...'
		}
	});
	reportList_grid.render('reportGrid');
	reportList_store.load({
		params : {
			start : 0,
			limit : reportGroupList_number
		}
	});

	function reportList_getSelect() {
		var rows = reportList_grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			Ext.MessageBox.alert('提示', '您当前没有选中任何数据!');
			return 0;
		}
		return rows;
	}

	function reportGroupList_fillElement(formObj) {
		var selectRow = reportList_getSelect();
		if (selectRow != 0 && selectRow.length == 1) {
			formObj.form.setValues(selectRow[0].data);
			return true;
		}
		if (selectRow != 0 && selectRow.length > 1) {
			Ext.MessageBox.alert('提示', '只允许选择一条记录!');
			return false;
		}
		return false;
	}

});