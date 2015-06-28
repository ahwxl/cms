Ext.onReady(function(){
	
	var treepanel = new Ext.tree.TreePanel({
     	useArrows: true,
        autoScroll: true,
        animate: true,
        enableDD: true,
        containerScroll: true,
        border: false,
        dataUrl: '../report/loadReportGroupTree',
        root: {
            nodeType: 'async',
            text: '报表组',
            draggable: false,
            id: '-1'
        }
    });
    treepanel.getRootNode().expand();
    
      // 右边具体功能面板区
    var contentPanel = new Ext.TabPanel({
        region : 'center',
        enableTabScroll : true,
        activeTab : 0,
        items : [{
            id : 'homePage',
            title : '首页',
            autoScroll : true,
            html : '<div style="position:absolute;color:#ff0000;top:40%;left:40%;">Tree控件和TabPanel控件结合功能演示</div>'
        }]
    });
    
    // 设置树的点击事件
    function treeClick(node, e) {
        if (node.isLeaf()) {
            e.stopEvent();
            var n = contentPanel.getComponent(node.id);
            if (!n) {
                Ext.getCmp('reportGroupId').setValue(node.id);
                n = contentPanel.add(reportList_grid);
                queryReport();
            }
            contentPanel.setActiveTab(n);
        }
    }
    // 增加鼠标单击事件
    treepanel.on('click', treeClick);
	
	var viewport = new Ext.Viewport({
		layout : 'border',  
         items : [ {  
	         region : "west",  
	         width : 200,
             title : "报表组",
             width: 200,
	         split: true,
             minSize: 175,
             maxSize: 400,
             collapsible: true,
             items : [treepanel]
         }, {  
            region : "center",  
         	title : "报表列表" ,
         	items : [contentPanel]
         } ]
	});
	
	
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

	//进入报表
	function showReport() {
		var rows = reportList_getSelect();
		if (rows != 0) {
			if (rows.length == 1) {
				var n = contentPanel.getComponent(rows[0].get('id'));
        if (!n) {
            var n = contentPanel.add({
                'id' : rows[0].get('id'),
                'title' : rows[0].get('name'),
                closable : true,
                autoLoad : {
                    url : '../report/showRepoortDetail?id='+rows[0].get('id'),
                    scripts : true
                } // 通过autoLoad属性载入目标页,如果要用到脚本,必须加上scripts属性
            });
        }
        contentPanel.setActiveTab(n);
			} else {
				Ext.MessageBox.alert('提示', '只能选择一个');
			}
		}
		
	}

	var reportList_tbar = new Ext.Toolbar({
		items : [ '报表名称:', {
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
		}, {
					xtype:'combo',
					fieldLabel:'所属报表组',
					id:'reportGroupId',
			        name:'reportGroupId', 
			        emptyText:'--请选择--', 
			        editable: false, 
			        triggerAction: 'all', 
			        store:reportGroupListAll_store, 
			        mode : 'remote', //默认远程数据加载
			        valueField : 'id',  //值 
			        hiddenName:"reportGroupId",
			        displayField : 'name' ,  //显示下拉框内容 
			        hidden : true
				}, {
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
			text : '进入报表',
			iconCls : 'page_edit_1Icon',
			handler : function() {
				showReport();
			}
		} ]
	});
		
var reportGroup_combo_store = new Ext.data.Store({
	proxy : new Ext.data.HttpProxy({
				url : '../report/getReportGroupListJson'
			}),
	reader : new Ext.data.JsonReader({
				totalProperty : 'itemNum',
				root : 'cntItem'
			}, [{
						name : 'id'
					}, {
						name : 'name'
					}, {
						name : 'description'
					}])
});
	
	var reportGroup_combo = new Ext.form.ComboBox({
		id: "reportGroup_combo",
		fieldLabel : '所属报表组',
		triggerAction : 'all',
		mode : 'remote',
		store : reportGroup_combo_store,
		valueField : 'id',
		displayField : 'name',
		width : 85
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
		fields : [ 'id', 'name', 'description', 'reportGroupId', 'reportGroupName' ],
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