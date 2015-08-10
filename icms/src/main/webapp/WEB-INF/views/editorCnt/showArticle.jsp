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
  Ext.namespace('ArticleMng', 'ArticleMng.articleListMng');


 //create application
 ArticleMng.articleListMng = function() {
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
    	    var aimobj = Ext.mainScreem.findById('docs-文章管理');
    	    var lyobj = new Ext.layout.BorderLayout();//创建面板布局对象
    	    aimobj.setLayout(lyobj);//应用布局对象



    	    aimobj.add(ArticleMng.articleListMng.catalogTree);//添加目录树面板
    	    aimobj.add(ArticleMng.articleListMng.mygrid);//添加列表面板
    	    aimobj.doLayout();//展示页面
    	    
    	    //加载列表数据
    	    ArticleMng.articleListMng.store.load();
       }
   };
 }(); // end of app 

 
 
 
 
 
 
 
 
 
 
 
/**
 * 创建目录树对象
 */
 ArticleMng.articleListMng.catalogTree = new Ext.tree.TreePanel({
	    //renderTo: 'tree-div',
	    title:'目录树',
	    useArrows: true,
	    autoScroll: true,
	    animate: true,
	    region: 'west',
	    //enableDD: true,
	    containerScroll: true,
	    width : 180,
	    border: true,
	    // auto create TreeLoader
	    loader: new Ext.tree.TreeLoader({dataUrl:'showCatalogTree'}),//请求的url地址
	    tools : [{
			id : 'refresh',
			handler : function() {
				ArticleMng.articleListMng.catalogTree.root.reload()
			}
		}],
	    //dataUrl: '',
	    root: {
	        nodeType: 'async',
	        text: '目录树',
	        draggable: false,
	        id: '0000'
	    }
	});
//添加目录树节点点击事件，添加别的事件也是这种方式，以此类推
 ArticleMng.articleListMng.catalogTree.on('click', function(node,e) {
		e.stopEvent();
		node.expand();
		deptid = node.attributes.id;
		ArticleMng.articleListMng.store.load({
					params : {
						start : 0,
						limit : 12,
						catalog_id : deptid
					}
				});
	});
 
 
 
//数据集加载对象，给grid用的
 ArticleMng.articleListMng.store = new Ext.data.JsonStore({
        root: 'topics',
        totalProperty: 'totalCount',
        //idProperty: 'threadid',
        remoteSort: true,
        fields: [
            'cnt_id', 'cnt_caption', 'catalog_id', 'operate_date'
        ],
        url: 'getCntList'
    });

    //添加grid 数据加载前事件
    ArticleMng.articleListMng.store.on("beforeload",function (selfstore){
    	//将form面板的查询参数  封装成  store 能接受的对象
    	var tmpCnf = Ext.urlDecode(Ext.Ajax.serializeForm(ArticleMng.articleListMng.searchPanel.getForm().getEl()));
    	tmpCnf.start = 0;//从哪一条记录开始
    	tmpCnf.limit = 10;//查询条数
    	selfstore.baseParams=tmpCnf;

    });
    

    function renderTopic(value, p, record){
        return String.format(
                '{0}',
                value);
    }
    function renderLast(value, p, r){
    	//value.dateFormat('M j, Y, g:i a')
        return String.format('{0}<br/>by {1}',value , r.data['cnt_id']);
    }
    //对列表的某列作处理
    function renderOpt(value, p, r){
    	return String.format('<a href=\'OutputHTML/web/{0}.html\' target=\"_blank\">预览</a>&nbsp;&nbsp;<u onclick=\"doPulicCnt(\'{0}\')\">删除</u>&nbsp;&nbsp;<u onclick=\"doPulicCnt(\'{0}\')\">发布</u>',r.data['cnt_id']);
    }
    //发布文章
    function doPulicCnt(id){
    	/**/
    	Ext.Ajax.request({
    		   url: 'doPublicCnt?id='+id,
    		   method:'get',
    		   success: function(response, opts) {
    			      //var obj = Ext.decode(response.responseText);
    			      Ext.Msg.alert('系统提示',response.responseText);
    		   },
    		   failure: function(response, opts) {
    			   Ext.Msg.alert('系统提示', '操作失败！');
    		   },
    		   params: { id: id }
    		});
    }
  //发布文章
    function doDelCnt(id){
    	/**/
    	Ext.Ajax.request({
    		   url: 'doDelCnt?id='+id,
    		   method:'get',
    		   success: function(response, opts) {
    			      //var obj = Ext.decode(response.responseText);
    			      //alert(response.responseText);
    			      Ext.Msg.alert('系统提示', response.responseText);
    		   },
    		   failure: function(response, opts) {
    			      Ext.Msg.alert('系统提示', '操作失败！');
    		   },
    		   params: { id: id }
    		});
    }
    
    
    //表单查询条件面板
ArticleMng.articleListMng.searchPanel =new Ext.FormPanel({
     	region:"north",
     	height:35,
     	frame:false,
     	border:false,id:"AppUserSearchForm",
     	layout:"hbox",
     	layoutConfig:{padding:"5",align:"middle"},
     	defaults:{xtype:"label",border:false,margins:{top:0,right:4,bottom:4,left:4}},
     	          items:[{text:"发布人"},{xtype:"textfield",name:"operate_user_id",value:''},
     	                 {text:"标题"},{xtype:"textfield",name:"cnt_caption",value:''},
     	                 {text:"发布时间:从"},{xtype:"datefield",format:"Y-m-d",name:"Q_accessionTime_D_GT"},
     	                 {text:"至"},{xtype:"datefield",format:"Y-m-d",name:"Q_accessionTime_D_LT"},
     	                 {xtype:"button",text:"查询",iconCls:"search",scope:this,handler:function (){
     	                	//ArticleMng.articleListMng.store.baseParams=Ext.urlDecode(Ext.Ajax.serializeForm(searchPanel.getForm().getEl()));
     	                	//alert(Ext.Ajax.serializeForm(ArticleMng.articleListMng.searchPanel.getForm().getEl()) );
     	                	ArticleMng.articleListMng.store.load();
     	                   }}
     	                 ]});
    
//创建一个列表面板
ArticleMng.articleListMng.mygrid = new Ext.grid.GridPanel({
        //width:700,
        //height:500,
        //autoHeight: true,
        //title:'分页演示',
        tbar:ArticleMng.articleListMng.searchPanel,
        store:  ArticleMng.articleListMng.store,
        //renderTo: 'tree-div',
        closable:true,
        trackMouseOver:true,
        disableSelection:true,
        region : 'center',
        loadMask: true,

        // grid columns
        columns:[{
            //id: 'topic', // id assigned so we can apply custom css (e.g. .x-grid-col-topic b { color:#333 })
            header: "文章编号",
            dataIndex: 'cnt_id',
            width: 150,
            //renderer: renderTopic,
            sortable: true
        },{
            header: "标题",
            dataIndex: 'cnt_caption',
            width: 100,
            //hidden: true,
            sortable: true
        },{
            header: "目录编码",
            dataIndex: 'catalog_id',
            width: 70,
            align: 'right',
            sortable: true
        },{
            id: '发布日期',
            header: "发布日期",
            dataIndex: 'operate_date',
            width: 150,
            //renderer: renderLast,
            sortable: true
        },{            
            header: "操作",
            //dataIndex: 'operate_date',
            width: 150,
            renderer: renderOpt,
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

        // paging bar on the bottom
        bbar: new Ext.PagingToolbar({
            pageSize: 10,
            store:  ArticleMng.articleListMng.store,
            displayInfo: true,
            displayMsg: 'Displaying topics {0} - {1} of {2}',
            emptyMsg: "No topics to display",
            items:[
                '-', {
                pressed: false,
                enableToggle:true,
                //text: 'Show Preview',
                cls: 'x-btn-text-icon details',
                toggleHandler: function(btn, pressed){
                    //var view = mygrid.getView();
                    //view.showPreview = pressed;
                    //view.refresh();
                }
            }]
        })
    });
Ext.onReady(ArticleMng.articleListMng.init, ArticleMng.articleListMng);
</script>