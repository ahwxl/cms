<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page language="java" import="java.util.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>报表列表</title>
<link rel="stylesheet" type="text/css" href="../res/css/ext_icon.css" />
<link rel="stylesheet" type="text/css" href="../res/extjs/resources/css/ext-all.css" />
<script type="text/javascript" src="../res/extjs/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="../res/extjs/ext-all.js"></script>
<script type="text/javascript" src="../res/extjs/ux/ProgressBarPager.js"></script>
<script type="text/javascript" src="../res/extjs/ux/PanelResizer.js"></script>
</head>
<body id='reportGrid'>
<%
String id = request.getParameter("id");
%>
</body>
</html>
<script type="text/javascript">
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

	//进入报表
	function showReport() {
		var rows = reportList_getSelect();
		if (rows != 0) {
			if (rows.length == 1) {
				window.location.href='../showRepoortDetail?id='+rows[0].get('id');
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
			        value: '<%=id%>',
			        displayField : 'name',   //显示下拉框内容 
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

queryReport();

});

</script>
