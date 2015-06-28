Ext.onReady(function() {

	function queryReportParameter() {
		reportParameterList_store.baseParams = {
			name : Ext.getCmp('name').getValue(),
			description : Ext.getCmp('description').getValue()
		};

		reportParameterList_store.load({
			params : {
				start : 0,
				limit : reportParameterList_bbar.pageSize
			}
		}); //查询
	}
	
	var ReportDataSourceListAll_store = new Ext.data.JsonStore({
		method:'post',
		url : '../report/getReportDataSourceListAll',
		fields : [ 'id', 'name', 'driverClassName', 'url', 'username', 'password', 'maxIdle', 'maxActive', 'maxWait', 'validationQuery', 'jndi' ]
	});

	var reportParameterAddPan_form = new Ext.form.FormPanel({
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
					fieldLabel : '名字',
					name : 'name',
					id : 'name',
					anchor : '100%'
				}, {
					fieldLabel : '描述',
					name : 'description',
					id : 'description',
					anchor : '100%'
				}, {
					xtype:'checkbox',
					fieldLabel : '必需',
					name : 'required',
					id : 'required'
				}, new Ext.form.ComboBox({
					fieldLabel : '类',
					allowBlank: false,
					forceSelection: true,
					emptyText: '请选择...',
					name : 'className',
					id : 'className',
					triggerAction : 'all',
					mode : 'local',
					store : new Ext.data.ArrayStore({
						fields : [ 'value', 'text' ],
						data : [ [ 'java.lang.String', 'java.lang.String' ],
						 		 [ 'java.lang.Double', 'java.lang.Double' ],
						 		 [ 'java.lang.Integer', 'java.lang.Integer' ],
								 [ 'java.lang.Long', 'java.lang.Long' ],
								 [ 'java.math.BigDecimal', 'java.math.BigDecimal' ],
								 [ 'java.util.Date', 'java.util.Date' ],
								 [ 'java.sql.Date', 'java.sql.Date' ],
								 [ 'java.sql.Timestamp', 'java.sql.Timestamp' ],
								 [ 'java.lang.Boolean', 'java.lang.Boolean' ]]
					}),
					valueField : 'value',
					displayField : 'text',
					editable : false,
					width : 150
				}), new Ext.form.ComboBox({
					fieldLabel : '类型',
					allowBlank: false,
					forceSelection: true,
					emptyText: '请选择...',
					name : 'type',
					id : 'type',
					triggerAction : 'all',
					mode : 'local',
					store : new Ext.data.ArrayStore({
						fields : [ 'value', 'text' ],
						data : [ [ 'Date', 'Date' ],
						 		 [ 'List', 'List' ],
						 		 [ 'Query', 'Query' ],
								 [ 'Text', 'Text' ],
								 [ 'SubReport', 'SubReport' ],
								 [ 'Boolean', 'Boolean' ]]
					}),
					valueField : 'value',
					displayField : 'text',
					editable : false,
					width : 150
				}), {
					xtype:'checkbox',
					fieldLabel : '多种选择',
					name : 'multipleSelect',
					id : 'multipleSelect'
				}, {
					fieldLabel : '默认值',
					name : 'defaultValue',
					id : 'defaultValue',
					anchor : '100%'
				}, {
					fieldLabel : '数据',
					name : 'data',
					id : 'data',
					height:50,
					anchor : '100%'
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
					name : 'id',
					id : 'id',
					hidden : true
				} ]
			} ]
		} ]
	});
	
	var reportParameterAddPan_windows = new Ext.Window({
		closeAction : 'hide',
		title : '添加报表参数',
		layout : 'fit',
		width : 430,
		height : 430,
		closable : true,
		collapsible : true,
		maximizable : true,
		border : false,
		modal : true,
		constrain : true,
		animateTarget : Ext.getBody(),
		items : [ reportParameterAddPan_form ],
		buttons : [ {
			text : '提交',
			handler : function() {
				submitForm();
			}
		}, {
			text : '取消',
			handler : function() {
				reportParameterAddPan_windows.hide();
			}
		} ]
	});

	function submitForm() {
		if (!reportParameterAddPan_form.form.isValid())
			return;
		reportParameterAddPan_type = true;
		if (reportParameterAddPan_type == false) {
			return;
		}
		reportParameterAddPan_form.form.submit({
			url : '../report/saveOrUpdateReportParameter',
			waitTitle : '提示',
			method : 'POST',
			waitMsg : '正在处理数据,请稍候...',
			success : function(form, action) {
				reportParameterAddPan_windows.hide();
				reportParameterAddPan_form.form.reset();
				queryReportParameter();
			},
			failure : function(form, action) {
				//Ext.MessageBox.alert('提示', '数据保存失败');
				reportParameterAddPan_windows.hide();
				reportParameterAddPan_form.form.reset();
				queryReportParameter();
			}
		});
	}

	function reportParameterList_deleteRow() {
		var rows = reportParameterList_getSelect();
		if (rows != 0) {
			Ext.MessageBox.confirm('提示', '您确认删除当前选择的' + rows.length + '条记录吗？',
					function(btn) {
						if (btn == 'yes') {
							var parameterIds = [];
							for ( var i = 0; i < rows.length; i++) {
								parameterIds.push(rows[i].get('id'));
							}
							
							Ext.Ajax.request({
								url : '../report/deleteReportParameter',
								method : 'post',
								success : function(result, request) {
									queryReportParameter();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert('提示', '请求数据失败！');
								},
								sync : true,
								params : {
									parameterIds : parameterIds
								}
							});
						}
					});

		}
	}

	var reportParameterList_tbar = new Ext.Toolbar({
		items : [ '报表参数名称:', {
			xtype : 'textfield',
			id : 'name',
			name : 'name',
			emptyText : '请输入报表参数名称',
			width : 150,
			enableKeyEvents : true,
			// 响应回车键
			listeners : {
				specialkey : function(field, e) {
					if (e.getKey() == Ext.EventObject.ENTER) {
						queryReportParameter();
					}
				}
			}
		}, '报表参数描述:', {
			xtype : 'textfield',
			id : 'description',
			name : 'description',
			emptyText : '请输入报表参数描述',
			width : 150,
			enableKeyEvents : true,
			// 响应回车键
			listeners : {
				specialkey : function(field, e) {
					if (e.getKey() == Ext.EventObject.ENTER) {
						queryReportParameter();
					}
				}
			}
		}, {
			text : '查询',
			iconCls : 'page_findIcon',
			handler : function() {
				queryReportParameter();
			}
		}, {
			text : '刷新',
			iconCls : 'page_refreshIcon',
			handler : function() {
				reportParameterList_store.reload();
			}
		}, '-', {
			text : '删除',
			iconCls : 'deleteIcon',
			handler : function() {
				reportParameterList_deleteRow();
			}

		}, {
			text : '添加报表参数',
			iconCls : 'addIcon',
			handler : function() {
				reportParameterAddPan_form.form.reset();
				reportParameterAddPan_windows.show();
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
	var reportParameterList_number = parseInt(typeList_pagesize_combo.getValue());
	typeList_pagesize_combo.on('select', function(comboBox) {
		reportParameterList_bbar.pageSize = parseInt(comboBox.getValue());
		reportParameterList_number = parseInt(comboBox.getValue());
		reportParameterList_store.reload({
			params : {
				start : 0,
				limit : reportParameterList_bbar.pageSize
			}
		});
	});
	var typeList_rownum = new Ext.grid.RowNumberer({
		header : '序号',
		width : 40
	});
	var typeList_sm = new Ext.grid.CheckboxSelectionModel();
	var reportParameterList_store = new Ext.data.JsonStore({
		root : 'dataList',
		totalProperty : 'totalCount',
		remoteSort : false,
		fields : [ 'id', 'name', 'description' ],
		proxy : new Ext.data.HttpProxy({
			url : '../report/queryReportParameter'
		})
	});

	var reportParameterList_bbar = new Ext.PagingToolbar({
		pageSize : reportParameterList_number,
		store : reportParameterList_store,
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
	var reportParameterList_grid = new Ext.grid.GridPanel({
		autoHeight : true,
		monitorResize : true,
		title : '报表参数',
		frame : true,
		autoScroll : true,
		region : 'center',
		store : reportParameterList_store,
		disableSelection : false,
		loadMask : true,
		columns : [ typeList_rownum, typeList_sm, {
			header : '报表参数ID',
			dataIndex : 'id',
			sortable : true
		}, {
			header : '报表参数名称',
			dataIndex : 'name',
			sortable : true
		}, {
			header : '报表参数描述',
			dataIndex : 'description',
			sortable : true
		}],
		stripeRows : true,
		sm : typeList_sm,
		bbar : reportParameterList_bbar,
		tbar : reportParameterList_tbar,
		viewConfit : {
			forceFit : true
		},
		loadMask : {
			msg : '正在加载表格数据,请稍等...'
		}
	});
	reportParameterList_grid.render('reportParameterGrid');
	reportParameterList_store.load({
		params : {
			start : 0,
			limit : reportParameterList_number
		}
	});

	function reportParameterList_getSelect() {
		var rows = reportParameterList_grid.getSelectionModel().getSelections();
		if (Ext.isEmpty(rows)) {
			Ext.MessageBox.alert('提示', '您当前没有选中任何数据!');
			return 0;
		}
		return rows;
	}

	function reportParameterList_fillElement(formObj) {
		var selectRow = reportParameterList_getSelect();
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