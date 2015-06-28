<%@ page language="java" pageEncoding="utf-8" %>
<script type="text/javascript">
 /**
  * @module name 文章列表管理
  * @module desc 系统菜单的添加和删除
  * @author  wxl
  * @create  date 20120406
  * @modify  man ***
  * @modify  date ***
  */
  Ext.namespace('SysModuleMng', 'SysModuleMng.moduleMng');


 //create application
 SysModuleMng.moduleMng = function() {
   // do NOT access DOM from here; elements don't exist yet

   //此处定义私有属性变量
   //var dragZone1, dragZone2;

   //此处定义私有方法

   //共有区
   return {
       //此处定义共有属性变量
       

       //共有方法
       init: function() {
     	  
           //定义一些初始化行为
    	   //获取id为"docs-文章管理"的面板
    	    var aimobj = Ext.mainScreem.findById('docs-模块管理');    
    	    var lyobj = new Ext.layout.BorderLayout();//创建面板布局对象
    	    aimobj.setLayout(lyobj);//应用布局对象



    	    aimobj.add(SysModuleMng.moduleMng.catalogTree);//添加目录树面板
    	    aimobj.add(SysModuleMng.moduleMng.mygrid);//添加列表面板
    	    aimobj.doLayout();//展示页面
    	    
    	    //加载列表数据
    	    SysModuleMng.moduleMng.store.load();
       }
   };
 }(); // end of app 

 
 
/**
 * 创建目录树对象
 */
 SysModuleMng.moduleMng.catalogTree = new Ext.tree.TreePanel({
	    //renderTo: 'tree-div',
	    loader : new Ext.tree.TreeLoader({
					baseAttrs :{},
					baseParams:{pmoduleid:'0000'},//默认参数是node
					dataUrl : 'getModuleTreeDataJson'
				}),
		root: {
		        nodeType: 'async',
		        text: '模块树',
		        expanded : true,
		        id: '0000'
		    },
	    title:'模块树',
	    region : 'west',
		width:190,
		tools : [{
					id : 'refresh',
					handler : function() {
						SysModuleMng.moduleMng.catalogTree.root.reload()
					}
				}],
	    collapsible : true,
		autoScroll : false,
		animate : false,
		useArrows : false,
		border : false
	    
	});

 SysModuleMng.moduleMng.catalogTree2 = new Ext.tree.TreePanel({
	    //renderTo: 'tree-div',
	    loader : new Ext.tree.TreeLoader({
					baseAttrs :{},
					baseParams:{pmoduleid:'0000'},//默认参数是node
					dataUrl : 'getModuleTreeDataJson'
				}),
		root: {
		        nodeType: 'async',
		        text: '模块树',
		        expanded : true,
		        id: '0000'
		    },
	    title:'模块树',
	    region : 'west',
		width:190,
		tools : [{
					id : 'refresh',
					handler : function() {
						SysModuleMng.moduleMng.catalogTree.root.reload()
					}
				}],
	    collapsible : true,
		autoScroll : false,
		animate : false,
		useArrows : false,
		border : false
	    
	});
 
	SysModuleMng.moduleMng.catalogTree.on('beforeload', function(node) {
		this.loader.baseParams = {
			pmoduleid:node.id
		};
	});
//添加目录树节点点击事件，添加别的事件也是这种方式，以此类推
	
	SysModuleMng.moduleMng.catalogTree.on('click', function(node,e) {
		        e.stopEvent();
				if (!node.isLeaf()) {
					SysModuleMng.moduleMng.store.load(
								 {
								 params :{pmoduleid:node.id,start : 0,limit : 10}
								 }
							  );
					return;
				}
				SysModuleMng.moduleMng.store.load({
							params : {
								start : 0,
								limit : 10
							}
						});
				
	});
 
    
  SysModuleMng.moduleMng.store = new Ext.data.Store({
			// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
			pruneModifiedRecords : true,
			proxy : new Ext.data.HttpProxy({
						url : 'getModuleGridDataJson'
					}),
			reader : new Ext.data.JsonReader({
						totalProperty : 'pageInfo.totalRowNum',
						root : 'data'
					}, [{
								name : 'moduleid'
							}, {
								name : 'modulename'
							}, {
								name : 'moduletype'
							}, {
								name : 'pmoduleid'
							}, {
								name : 'sortid'
							}])
		});

    //添加grid 数据加载前事件
    SysModuleMng.moduleMng.store.on("beforeload",function (selfstore){
    	//将form面板的查询参数  封装成  store 能接受的对象
    	var tmpCnf = Ext.urlDecode(Ext.Ajax.serializeForm(SysModuleMng.moduleMng.searchPanel.getForm().getEl()));
    	tmpCnf.pmoduleid = '0000';//从哪一条记录开始
    	tmpCnf.start = 0;//从哪一条记录开始
    	tmpCnf.limit = 10;//查询条数
    	selfstore.baseParams=tmpCnf;

    });
    
    //对列表的某列作处理
    function renderModuleOpt(value, p, record){
    	return String.format('<u onclick=\"delSysModuleById(\'{0}\')\" >删除</u>&nbsp;&nbsp;<u onclick=\"goEditSysModulePage(\'{0}\')\" >修改</u>',record.data['moduleid']);
    }
    //删除
    function delSysModuleById(id){
    	Ext.Ajax.request({
    		   url: 'doDelSysModule?moduleid='+id,
    		   method:'get',
    		   success: function(response, opts) {
    			      alert(response.responseText);
    		   },
    		   failure: function(response, opts) {
    			      alert('失败');
    		   }
    		});
    	
    	}
    function goEditSysModulePage(id){
	   	var url = 'toEditSysModulePage?moduleid='+id;
	   	/**
	   	*打开一个iframe窗口
	   	*/
	   	Ext.mainScreem.addNewTab(Ext.getmifObj({id:'module1',title:'修改模块信息',src:url}),'module1');
	}
    
    
    //表单查询条件面板
SysModuleMng.moduleMng.searchPanel =new Ext.FormPanel({
     	region:"north",
     	height:35,
     	frame:false,
     	border:false,id:"AppUserSearchForm",
     	layout:"hbox",
     	layoutConfig:{padding:"5",align:"middle"},
     	defaults:{xtype:"label",border:false,margins:{top:0,right:4,bottom:4,left:4}},
     	          items:[{text:"模块名称"},{xtype:"textfield",name:"modulename",value:''},
     	                 {xtype:"button",text:"查询",iconCls:"search",scope:this,handler:function (){
     	                	//store.baseParams=Ext.urlDecode(Ext.Ajax.serializeForm(searchPanel.getForm().getEl()));
     	                	SysModuleMng.moduleMng.store.load();
     	                   }}
     	                 ]});
    
//创建一个列表面板
SysModuleMng.moduleMng.mygrid = new Ext.grid.GridPanel({
        //width:700,
        //height:500,
        //autoHeight: true,
        //title:'分页演示',
        store:  SysModuleMng.moduleMng.store,
        //renderTo: 'tree-div',
        closable:true,
        trackMouseOver:true,
        disableSelection:true,
        region : 'center',
        loadMask: true,

        // grid columns
        columns:[{
            //id: 'topic', // id assigned so we can apply custom css (e.g. .x-grid-col-topic b { color:#333 })
            header: "模块编码",
            dataIndex: 'moduleid',
            width: 150,
            //renderer: renderTopic,
            sortable: true
        },{
            header: "模块名称",
            dataIndex: 'modulename',
            width: 100,
            //hidden: true,
            sortable: true
        },{
            header: "模块类别",
            dataIndex: 'moduletype',
            width: 70,
            align: 'right',
            sortable: true
        },{            
            header: "操作",
            //dataIndex: 'operate_date',
            width: 150,
            renderer: renderModuleOpt,
            sortable: true
        }],

        // customize view config
        viewConfig: {
            forceFit:true,
            enableRowBody:false,
            showPreview:false,
            getRowClass : function(record, rowIndex, p, store){
                if(this.showPreview){
                    p.body = '<p>'+record.data.cnt_id+'</p>';
                    return 'x-grid3-row-expanded';
                }
                return 'x-grid3-row-collapsed';
            }
        },
		tbar : [
					{
						text : '新增',
						iconCls : 'addIcon',
						id : 'id_addRow',
						handler : function() {
							showSysModuleWin();
						}
					}, '-', {
						text : '刷新',
						iconCls : 'arrow_refreshIcon',
						handler : function() {
							SysModuleMng.moduleMng.store.reload();
						}
					}, '-',SysModuleMng.moduleMng.searchPanel
					],
        // paging bar on the bottom
        bbar: new Ext.PagingToolbar({
            pageSize: 10,
            store:  SysModuleMng.moduleMng.store,
            displayInfo: true,
            displayMsg: 'Displaying topics {0} - {1} of {2}',
            emptyMsg: "No topics to display",
            items:[
                '-', {
                pressed: true,
                enableToggle:true,
                text: 'Show Preview',
                cls: 'x-btn-text-icon details',
                toggleHandler: function(btn, pressed){
                    var view = mygrid.getView();
                    view.showPreview = pressed;
                    view.refresh();
                }
            }]
        })
    });

var comboxWithTree = new Ext.form.ComboBox({
	store:new Ext.data.SimpleStore({fields:[],data:[[]]}),
	//editable:true,
	//typeAhead: true,
	//forceSelection: true,
	allowBlank:true,
	shadow:false,
	mode: 'local',
	triggerAction:'all',
	emptyText:'Select a state...',
	//fieldLabel: '父模块名称',
	maxHeight: 200,
	tpl: '<tpl for="."><div style="height:200px"><div id="tree1"></div></div></tpl>'
	//selectedClass:'',
	//onSelect:Ext.emptyFn
});
comboxWithTree.on('expand',function(){
	SysModuleMng.moduleMng.catalogTree2.render('Tree1');
});

var addSysModuleForm = new Ext.FormPanel({
    labelWidth: 75, // label settings here cascade unless overridden
    url:'addTmplPage',
    frame:false,
    title: '添加模块',
    fileUpload:true,
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,

    items: [{
        xtype:'fieldset',
        title: 'Phone Number',
        collapsible: true,
        autoHeight:true,
        defaults: {width: 210,
        allowBlank: false},
//        defaultType: 'textfield',
        items :[{
        	    xtype: 'textfield',
                fieldLabel: '模块名称',
                name: 'modulename',
                value: ''
            },{
            	xtype: 'textfield',
                fieldLabel: '模块类型',
                name: 'moduletype'
            },{
            	xtype: 'textfield',
                fieldLabel: '父模块名称',
                name: 'pmodulename'
            },comboxWithTree
            ,{
                xtype:'hidden',
                name:'pmoduleid'
            }
        ]
    }],

    buttons: [{
        text: '保存',
        handler: function(){
            if(addSysModuleForm.getForm().isValid()){
            	addSysModuleForm.getForm().submit({
                    url: 'addSysModule',
                    waitMsg: '正在上传请稍等...',
                    success: function(form, action){
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
        	sysModuleWin.hide();
        }
    }]
});
   
var sysModuleWin = new Ext.Window({
    width:500, height: 500,
    layout: 'fit', // explicitly set layout manager: override the default (layout:'auto')
    title:'添加模块',
    closeAction:'hide',
    items: [addSysModuleForm]
});
 
//显示添加模块窗口
function showSysModuleWin(){

	sysModuleWin.show();
	addSysModuleForm.getForm().findField('pmoduleid').setValue(SysModuleMng.moduleMng.catalogTree.getSelectionModel().getSelectedNode().attributes.id);
	addSysModuleForm.getForm().findField('pmodulename').setValue(SysModuleMng.moduleMng.catalogTree.getSelectionModel().getSelectedNode().attributes.text);
	
}
    
Ext.onReady(SysModuleMng.moduleMng.init, SysModuleMng.moduleMng);
</script>