<%@ page language="java" pageEncoding="utf-8"%>
<div id="hello-win" class="x-hidden">
    <div class="x-window-header">添加目录</div>
</div>

<script type="text/javascript">
	var catalogTree, Virtual, root,deptid;

	//当节点对象和树对象构造完了、执行render之前，会调用该方法
	//所以可以在这做函数注册处理.
	
	function treeRenderBeforeHandler(pTree){
	  //用来记录当前右键选种的书节点  
	  var selectedNode;
	  var rightClick = new Ext.menu.Menu( {
	                id : 'rightClickCont',
	                items : [ 
	                {
	                    id:'addUser',
	                    text : '新增栏目',
	                    handler : function(){
	                      //alert('跳转到新增机构页面');
	                      showAddCatalogWin(selectedNode.text,selectedNode.id);
	                    }
	                },
	                {
	                    id:'editUser',
	                    text : '修改栏目',
	                    handler : function(){
	                      //alert('跳转到修改:[' + selectedNode.text + ']机构的页面');
	                      showEditorCatalogWin(selectedNode.id);
	                    }
	                    
	                },
	                {
	                    id:'delUser',
	                    text : '删除栏目',
	                    handler : function(){
	                    	Ext.Msg.confirm('删除目录', '您确定要删除目录吗？', function(btn, text){
	                    	    if (btn == 'yes'){
	                    	    	delCatalog(selectedNode.id);
	                    	    }
	                    	});
	                      
	                    }
	                },
	                {
	                    id:'viewUser',
	                    text : '查看栏目',
	                    handler : function(){
	                      alert('跳转到查看机构[' + selectedNode.text + ']的页面');
	                      
	                    }
	                }
	                
	                ]
	            });
	            
	      pTree.on('contextmenu',function(node,pEventObj){
          //alert(node.text);	    	  
	      pEventObj.preventDefault();
	      rightClick.showAt(pEventObj.getXY());
	      selectedNode = node;
	    });
	      
	}
	
	var initConfig = {
		autoHeight : false,
		width: 200,
		autoWidth : false,
		autoScroll : true,
		animate : false,
		enableDD : true,
		lines : true,
		rootVisible : true,
		title : '目录树',
		border : false,
		region : 'west',
		containerScroll : true,
		//dataUrl: 'showCatalogTree',
		loader: new Ext.tree.TreeLoader({dataUrl:'showCatalogTree'}),

	    root:{
	        nodeType: 'async',
	        text: '站点',
	        draggable: false,
	        href:"",
	        hrefTarget:'',
	        //leaf:false,
	        id: '0000'
	    },
	    tbar: new Ext.Toolbar({
	        items: [{
	            text: ' 展开 ',
	            handler : reloadTree
	            
	        }/* ,{
	            text: ' 添加 '
	        },{
	            text: ' 修改 '
	        },{
	            text: ' 删除 '
	        } */,{
	            text: ' 刷新 ',
	            handler : function() {
	            	catalogTree.root.reload()
				}
	         
	        }]
	    })
	};

	catalogTree = new Ext.tree.TreePanel(initConfig);
	Ext.override(Ext.tree.TreeNodeUI, {
		onClick : function(e) { //debugger;
			e.stopEvent();
			if (this.dropping) {
				e.stopEvent();
				return;
			}
			if (this.fireEvent("beforeclick", this.node, e) !== false) {
				var a = e.getTarget('a');
				if (!this.disabled && this.node.attributes.href && a) {
					this.fireEvent("click", this.node, e);
					return;
				} else if (a && e.ctrlKey) {
					e.stopEvent();
				}
				e.preventDefault();
				if (this.disabled) {
					return;
				}
				if (this.node.attributes.singleClickExpand && !this.animating
						&& this.node.hasChildNodes()) {
					//this.node.expand();
					//this.node.toggle();
				}
				this.fireEvent("click", this.node, e);
			} else {
				e.stopEvent();
			}
		}
	});

	Ext.override(Ext.tree.TreeNodeUI, {
		onDblClick : function(e) { //debugger;
			e.preventDefault();
			if (this.node.attributes.disabled) {
				return;
			}
			if (this.checkbox) {
				this.toggleCheck();
			}
			if (this.animating && this.node.hasChildNodes()) {
				//this.node.toggle();
				//this.node.expand();
			}
			this.fireEvent("dblclick", this.node, e);
		}
	});

	Virtual = new Ext.tree.TreeNode({
		id : 'Virtual',
		text : '虚拟跟节点',
		href : "",
		hrefTarget : '',
		qtip : '',
		disabled : false,
		allowDrag : false,
		allowDrop : false,
		iconCls : 'E3-TREE-STYLE-PREFIX1'
	});
	root = Virtual;
	//tree.setRootNode(root);
	var checkChildren = function(node) {
		if (node.isLeaf()) {//非叶子节点
			return;
		}
		var nodeUI = node.getUI();
		var children = node.childNodes;
		for ( var i = 0; i < children.length; i++) {
			var child = children[i];
			var childUI = child.getUI();
			if (typeof child.attributes.checked == 'undefined') {
				continue;
			}
			if (child.attributes.checked == node.attributes.checked) {
				continue;
			}
			if (child.attributes.disabled) {
				return;
			}
			if (node.attributes.checked) {
				child.getOwnerTree().fireEvent('onChecked', child);
			} else {
				child.getOwnerTree().fireEvent('onUnchecked', child);
			}
			childUI.toggleCheck(node.attributes.checked);
			child.attributes.checked = node.attributes.checked;
			checkChildren(child);
		}
	};
	var checkParents = function(node) {
		if (node == null) {
			return;
		}
		var nodeUI = node.getUI();
		if (typeof node.attributes.checked == 'undefined') {
			return;
		}
		if (node.attributes.checked == false) {//取消父亲.
			uncheckParents(node);
			return;
		}
		var parentNode = node.parentNode;
		if (parentNode == null) {
			return;
		}
		var parentNodeUI = parentNode.getUI();
		if (typeof parentNode.attributes.checked == 'undefined') {
			return;
		}
		if (parentNode.attributes.checked) {//已经选种
			return;
		}
		//只要有一个没选种就返回,全选种就递归
		var children = parentNode.childNodes;
		for ( var i = 0; i < children.length; i++) {
			var child = children[i];
			if (typeof child.attributes.checked == 'undefined') {
				continue;
			}
			if (child.attributes.checked == false) {
				return;
			}
		}
		if (parentNode.attributes.disabled) {
			return;
		}
		parentNodeUI.toggleCheck(true);
		parentNode.getOwnerTree().fireEvent('onChecked', parentNode);
		parentNode.attributes.checked = true;
		checkParents(parentNode);
	};
	var uncheckParents = function(node) {
		var parentNode = node.parentNode;
		if (parentNode == null) {
			return;
		}
		var parentNodeUI = parentNode.getUI();
		if (typeof parentNode.attributes.checked == 'undefined') {
			return;
		}
		if (parentNode.attributes.disabled) {
			return;
		}
		parentNodeUI.toggleCheck(false);
		parentNode.attributes.checked = false;
		parentNode.getOwnerTree().fireEvent('onUnchecked', parentNode);
		uncheckParents(parentNode);
	};
	// 双击节点，checkbox选中了，但是实际并没有触发Ext的checkchange事件，所以本来想checkchange时做点事情的话，这时就失败了。
	//这里需要特别处理下这个双击事件
	catalogTree.on('dblclick', function(n, e) {
		var ckb = n.getUI().checkbox;
		if (typeof ckb == 'undefined') {
			return true;
		}
		if (n.attributes.disabled == 'undefined') {
			return true;
		}
		if (n.attributes.disabled == true) {
			return true;
		}
		n.fireEvent('checkchange', n, ckb.checked)
	}, catalogTree);
	catalogTree.on('checkchange', function(pNode, pChecked) {
		if (pChecked) {
			pNode.getOwnerTree().fireEvent('onChecked', pNode);
		} else {
			pNode.getOwnerTree().fireEvent('onUnchecked', pNode);
		}
		pNode.attributes.checked = pChecked;
		if (true) {
			checkChildren(pNode);
		}
		if (true) {
			checkParents(pNode);
		}
	});
	
	catalogTree.on('dblclick', function(n, e) {
		e.stopEvent();
	}, catalogTree);
	
	
	catalogTree.on('click', function(node,e) {
		e.stopEvent();
		deptid = node.attributes.id;
		catalogstore.load({
					params : {
						start : 0,
						limit : bbar.pageSize,
						node : deptid
					}
				});
	});
	
	
    treeRenderBeforeHandler(catalogTree);

	//tree.render('mytreediv');
	
	catalogTree.getRootNode().expand();
	
	/*
	 *添加目录表单
	 */
	var addForm = new Ext.FormPanel({
        labelAlign: 'top',
        frame:true,
        id:'addFormId',
        //title: 'Multi Column, Nested Layouts and Anchoring',
        bodyStyle:'padding:5px 5px 0',
        width: 550,
        items: [{
            layout:'column',
            items:[{
                columnWidth:.5,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '目录名称',
                    name: 'catalogName',
                    anchor:'95%'
                },{
                	xtype:          'combo',
                    mode:           'local',
                    value:          '1',
                    triggerAction:  'all',
                    forceSelection: true,
                    editable:       false,
                    fieldLabel:     '目录类别',
                    name:           'catalogType',
                    hiddenName:     'catalogType',
                    displayField:   'name',
                    valueField:     'value',
                    anchor:'95%',
                    store:          new Ext.data.JsonStore({
                        fields : ['name', 'value'],
                        data   : [
                            {name : '产品',   value: '1'},
                            {name : '文章',  value: '2'},
                            {name : '字定义', value: '99'}
                        ]
                    })
                },{
                    xtype:'textfield',
                    fieldLabel: '排序',
                    name: 'orderId',
                    value:'1',
                    anchor:'95%'
                }]
            },{
                columnWidth:.5,
                layout: 'form',
                items: [{
                    xtype:'textfield',
                    fieldLabel: '图片',
                    name: 'imageSrc',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '父目录',
                    name: 'pCatalogName',
                    //vtype:'email',
                    anchor:'95%'
                },{
                    xtype:'textfield',
                    fieldLabel: '模板',
                    name: 'tmplid',
                    anchor:'95%'
                },{
                	hidden:true,
                	xtype:'textfield',
                	name: 'pCatalogId'
                },{
                	hidden:true,
                	xtype:'textfield',
                	name: 'catalogId'
                },{
                	hidden:true,
                	xtype:'textfield',
                	name: 'actionFlag'
                }]
            }]
        },{
            xtype:'htmleditor',
            id:'bio',
            name:'catalogDesc',
            fieldLabel:'描述信息',
            height:200,
            anchor:'98%'
        }]
    });
	//弹出窗口
	var mywin = new Ext.Window({
        applyTo:'hello-win',
        layout:'fit',
        width:600,
        height:400,
        closeAction:'hide',
        plain: true,
        buttons: [{
            text:'提交',
            handler: function(){
            	addCatalog();
            }
        },{
            text: '取消',
            handler: function(){
            	//mywin.removeAll();
            	mywin.hide();
            }
        }]
    });
	
	/*
	*显示修改页面
	*/
	function showEditorCatalogWin(catalogId){
		Ext.Ajax.request({
			   url : 'showEditorCatalogPage?catalogId='+catalogId ,
			   method :'GET',
			   success : function(response, opts) {
				      var obj = Ext.decode(response.responseText);
				      fillFormData(obj);
			   }
			   //failure: ,
			   //,params: { foo: 'bar' }
		});
		addForm.getForm().findField('actionFlag').setValue('editor');
		if(mywin.getComponent('addFormId') == undefined ){
			mywin.add(addForm);
		}
		mywin.show();
	}
	
	//往表单中填充数据
	function fillFormData(catalogObj){
		
		addForm.getForm().findField('catalogId').setValue(catalogObj.catalogId);
		addForm.getForm().findField('catalogName').setValue(catalogObj.catalogName);
		addForm.getForm().findField('catalogType').setValue(catalogObj.catalogType);
		addForm.getForm().findField('imageSrc').setValue(catalogObj.imageSrc);
		addForm.getForm().findField('pCatalogName').setValue(catalogObj.pCatalogName);
		addForm.getForm().findField('catalogDesc').setValue(catalogObj.catalogDesc);
		addForm.getForm().findField('pCatalogId').setValue(catalogObj.pCatalogId);
		addForm.getForm().findField('orderId').setValue(catalogObj.orderId);
		//alert(catalogObj.catalogId);
	}
	
	
	//添加目录
	function showAddCatalogWin(pCatalogName,pCatalogId){
		//alert(addForm.getForm().findField('first'));
		addForm.getForm().reset();
		addForm.getForm().findField('pCatalogName').setValue(pCatalogName);
		addForm.getForm().findField('pCatalogId').setValue(pCatalogId);
		
		//mywin.add(addForm);
		
		if(mywin.getComponent('addFormId') == undefined ){
			mywin.add(addForm);
		}
		
		//alert(mywin.getComponent('addFormId'));
		
		mywin.show();
	}
	
	//Action add catalog
	function addCatalog(){
		var url;
		//alert(addForm.getForm().findField('actionFlag'));
		var action = addForm.getForm().findField('actionFlag').getValue();
		if(action == 'editor'){
			url = 'doEditorCatalog';
		}else url = 'doAddCatalog';
		
		addForm.getForm().getEl().dom.action = url;
		addForm.getForm().getEl().dom.method = 'POST';
		addForm.getForm().submit();
		mywin.hide();
	}
	
	/*
	*刷新目录树
	*/
	function reloadTree(){
		catalogTree.getRootNode().expand();
	}
	
	/*
	*删除目录
	*/
	function delCatalog(catalogId){
		
		Ext.Ajax.request({
			   url : 'doDelCatalog?catalogId='+catalogId ,
			   method :'GET',
			   success : function(response, opts) {
				   
				   Ext.MessageBox.alert('系统信息',response.responseText);
			   },
			   failure:function(response, opts) { 
				   Ext.MessageBox.alert('系统信息',response.responseText);
			   }
			   //,params: { foo: 'bar' }
		});
	}
	
	var catalogstore = new Ext.data.JsonStore({
	    // store configs
	    //autoDestroy: true,
	    url: 'getCatalogGridData',
	    storeId: 'myStore',
	    //totalProperty:'',
	    // reader configs
	    root: 'data',
	    listeners:{beforeload:function(thisstore,options){
	    	var node = catalogTree.getSelectionModel().getSelectedNode();
	    	if(node)thisstore.setBaseParam('node',node.id);
	    	else thisstore.setBaseParam('node','0000');
	    	//alert(options.limit);
	    }},
	    idProperty: 'name',
	    fields: ['catalogId', 'catalogName','catalogDesc','imageSrc','catalogType','pCatalogId','secondName'
	             ,'operateDate','isDeleteFlag','orderId']
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
	/**/
	function renderCatalogType(value, p, r){
		if(value == "1"){
			return "产品";
		}else if(value == "2"){
			return "文章";
		}
		return "其他";
	}
	/*操作*/ 
	function renderOpt(value, p, r){
    	return String.format('<u  onclick=\"openAddarticle(\'{0}\')\" ></u>&nbsp;&nbsp;<u onclick=\"doDeleteCatalog(\'{0}\')\">删除</u>&nbsp;&nbsp;<u onclick=\"doPublicCatalogAction(\'{0}\')\"></u>',r.data['catalogId']);
    }
	//发布目录
	function doPublicCatalogAction(catalogId){
		Ext.Ajax.request({
		   url: ''+catalogId,
		   success: function(form, action){
			   Ext.Msg.alert(action.result.info);
		   },
		   failure: function(form, action){
			   Ext.Msg.alert('操作失败');
		   }
		   //,params: { 'catalogId':catalogId }
		});

	}
	
	//del catalog
	function doDeleteCatalog(catalogId){
		Ext.Ajax.request({
			   url: 'doDelCatalog?catalogId='+catalogId,		   
			   success: function(response, opts){
				   var obj = Ext.decode(response.responseText);
				   Ext.Msg.alert('系统提示',obj.info);
			   },
			   failure: function(response, opts){
				   Ext.Msg.alert('操作失败');
			   }
			   //,params: { 'catalogId':catalogId }
			});
	}
	
var number = parseInt(pagesize_combo.getValue());
pagesize_combo.on("select", function(comboBox) {
		bbar.pageSize = parseInt(comboBox.getValue());
		number = parseInt(comboBox.getValue());
		catalogstore.reload({
					params : {
						start : 0,
						limit : bbar.pageSize
					}
				});
	});
	
	
	var bbar = new Ext.PagingToolbar({
		pageSize : number,
		store : catalogstore,
		displayInfo : true,
		displayMsg : '显示{0}条到{1}条,共{2}条',
		emptyMsg : "没有符合条件的记录",
		items : ['-', '&nbsp;&nbsp;', pagesize_combo]
	});
	
	
	var mygrid =new Ext.grid.GridPanel({
	    store: catalogstore,
	    region : 'center',
	    colModel: new Ext.grid.ColumnModel({
	        defaults: {
	            width: 120,
	            sortable: true
	        },
	        columns: [new Ext.grid.RowNumberer(),
                {id: 'id', header: '编号', width: 200, sortable: true,hidden:true, dataIndex: 'catalogId'},
	            {id: 'company', header: '目录名称', width: 200, sortable: true, dataIndex: 'catalogName'},
	            {header: '描述',  dataIndex: 'catalogDesc'},
	            {header: '类型', width: 50,  dataIndex: 'catalogType',renderer:renderCatalogType},
	            {header:'排序',width:50,dataIndex:'orderId'},
	            {header: '操作',dataIndex:'',renderer:renderOpt}	
	        ]
	    }),
	    viewConfig: {
	        forceFit: true,

//	      Return CSS class to apply to rows depending upon data values
	        getRowClass: function(record, index) {
	            var c = record.get('change');
	            if (c < 0) {
	                return 'price-fall';
	            } else if (c > 0) {
	                return 'price-rise';
	            }
	        }
	    },
	    sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	    loadMask : {
			msg : '正在加载表格数据,请稍等...'
		},
	    //width: 600,
	    //height: 300,
	    frame: true,
	    title: '目录',
	    iconCls: 'icon-grid',
	    bbar : bbar
	});
	
	
	
	
	
	
	
	
	
	
	

    var aimobj = Ext.mainScreem.findById('docs-目录管理');
    var lyobj = new Ext.layout.BorderLayout();
    aimobj.setLayout(lyobj);



    aimobj.add(catalogTree);
    aimobj.add(mygrid);
    aimobj.doLayout();	
	
    catalogstore.reload({
		params : {
			start : 0,
			node:'0000',
			limit : bbar.pageSize
		}
	});
	
    
    
    
    function openAddarticle(catalogID){
    	Ext.mainScreem.addNewTab(returnmif({id:'发布文章',title:'发布文章',src:'showAddCntPage?calalogId='+catalogID}),'发布文章');
    }
    
    
    
    
</script>