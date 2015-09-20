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
  Ext.namespace('DicParaMng', 'DicParaMng.dicParaObj');


 //create application
 DicParaMng.dicParaObj = function() {
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
    	    var aimobj = Ext.mainScreem.findById('docs-滚动图片');
    	    var lyobj = new Ext.layout.BorderLayout();//创建面板布局对象
    	    aimobj.setLayout(lyobj);//应用布局对象



    	    //aimobj.add(DicParaMng.dicParaObj.catalogTree);//添加目录树面板
    	    aimobj.add(DicParaMng.dicParaObj.mygrid);//添加列表面板
    	    aimobj.doLayout();//展示页面
    	    
    	    //加载列表数据
    	    DicParaMng.dicParaObj.store.load();
       }
   };
 }(); // end of app 

 
 
 
 
 
 
 
 
 
 
 
/**
 * 创建目录树对象
 */
 DicParaMng.dicParaObj.catalogTree = new Ext.tree.TreePanel({
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
				DicParaMng.dicParaObj.catalogTree.root.reload()
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
 DicParaMng.dicParaObj.catalogTree.on('click', function(node,e) {
		e.stopEvent();
		node.expand();
		deptid = node.attributes.id;
		DicParaMng.dicParaObj.store.load({
					params : {
						start : 0,
						limit : 12,
						catalogId : deptid
					}
				});
	});
 
 
 
//数据集加载对象，给grid用的
 DicParaMng.dicParaObj.store = new Ext.data.JsonStore({
        root: 'data',
        totalProperty: 'totalCount',
        //idProperty: 'threadid',
        remoteSort: true,
        fields: [
            'id', 'pname', 'pgroup', 'pdesc','pvalue','porder'
        ],
        url: 'sysDicParaList'
    });

    //添加grid 数据加载前事件
    DicParaMng.dicParaObj.store.on("beforeload",function (selfstore){
    	//将form面板的查询参数  封装成  store 能接受的对象
    	var tmpCnf = Ext.urlDecode(Ext.Ajax.serializeForm(DicParaMng.dicParaObj.searchPanel.getForm().getEl()));
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
    	return String.format('<u onclick=\"doEditorCnt(\'{0}\')\" >修改</u>&nbsp;&nbsp;<u onclick=\"doDelCnt(\'{0}\')\">删除</u>&nbsp;&nbsp;',r.data['productId']);
    }
    //发布文章
    function doEditorCnt(id){
    	/**/
    	var editerurl ='showEditorProductPage?productId='+id;
    	Ext.mainScreem.loadClass(editerurl,"修改产品",null);
    }
    //发布文章
    function doDelCnt(id){
    	/**/
    	Ext.Ajax.request({
    		   url: 'delProductAction?productId='+id,
    		   method:'post',
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
DicParaMng.dicParaObj.searchPanel =new Ext.FormPanel({
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
     	                	//DicParaMng.dicParaObj.store.baseParams=Ext.urlDecode(Ext.Ajax.serializeForm(searchPanel.getForm().getEl()));
     	                	//alert(Ext.Ajax.serializeForm(DicParaMng.dicParaObj.searchPanel.getForm().getEl()) );
     	                	DicParaMng.dicParaObj.store.load();
     	                   }}
     	                  ,{xtype:"button",text:"删除",scope:this,handler:function (){
							var sm = DicParaMng.dicParaObj.mygrid.getSelectionModel();
							var array = sm.getSelections();//records
							var idstr ="";
							for(var i =0;i < array.length;i++){
							   idstr =idstr+array[i].get('id')+",";
							}
							if(idstr.length > 0){
							   idstr = idstr.substr(0,idstr.length-1);
							}else{
							    Ext.Msg.alert('系统提示','请选择要删除的数据');
							    return false;
							}
							Ext.Msg.confirm('系统提示', '你确定删除吗？', function(btn, text){
							    if (btn == 'yes'){
							    	DicParaMng.dicParaObj.doDel(idstr);
							    }
							});
							}
     	        	       
     	        	       }
     	                 ]});
DicParaMng.dicParaObj.sm = new Ext.grid.CheckboxSelectionModel({
	listeners:{
			     rowselect :function(selectmodel,rowIndex,record) {
			     }
			}
	});
//创建一个列表面板
DicParaMng.dicParaObj.mygrid = new Ext.grid.GridPanel({
        //width:700,
        //height:500,
        //autoHeight: true,
        //title:'分页演示',
        tbar:[{
        	text:'添加滚动图片',
            id:'add_product',
            iconCls:'silk-add',
            handler:function (){
            	// showAddProductPage
            	//Ext.mainScreem.loadClass('showAddProductPage','添加产品',null);
            	DicParaMng.dicParaObj.showWin(this.getEl());
            	//Ext.mainScreem.addNewTab( Ext.getmifObj({id:'添加产品',title:'添加产品',src:'showAddProductPageIframe'}),'添加产品');
            }
        }],
        listeners:{
            'render':function () {
            	DicParaMng.dicParaObj.searchPanel.render(this.tbar);
            }
        },
        store:  DicParaMng.dicParaObj.store,
        //renderTo: 'tree-div',
        closable:true,
        trackMouseOver:true,
        disableSelection:true,
        region : 'center',
        loadMask: true,
        sm: DicParaMng.dicParaObj.sm,
        // grid columns
        columns:[DicParaMng.dicParaObj.sm
                 ,{
            //id: 'topic', // id assigned so we can apply custom css (e.g. .x-grid-col-topic b { color:#333 })
            header: "参数名称",
            dataIndex: 'pname',
            width: 150,
            //renderer: renderTopic,
            sortable: true
        },{
            header: "参数描述",
            dataIndex: 'pdesc',
            width: 100,
            //hidden: true,
            sortable: true
        },{
            header: "参数值",
            dataIndex: 'pvalue',
            width: 200,
            align: 'right',
            sortable: true
        }/* ,{
            id: '修改日期',
            header: "修改日期",
            dataIndex: 'gmtModify',
            width: 150,
            //renderer: renderLast,
            sortable: true
        } */,{            
            header: "排序",
            dataIndex: 'porder',
            width: 150,
            //renderer: renderOpt,
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
            store:  DicParaMng.dicParaObj.store,
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
/*添加图片窗口*/
DicParaMng.dicParaObj.showWin = function(){
	if(DicParaMng.dicParaObj.addwindobj == null || DicParaMng.dicParaObj.addwindobj == undefined){
		DicParaMng.dicParaObj.addwindobj = new DicParaMng.dicParaObj.addImageWin();
	}
	DicParaMng.dicParaObj.addwindobj.show();
}

DicParaMng.dicParaObj.addImageWin = function(config){
	this.config = config;
}



DicParaMng.dicParaObj.addImageWin.prototype = {
		
		
		show : function(el, callback){
			if(!this.win){
				
				this.fsf = new Ext.FormPanel({
				    labelWidth: 75, // label settings here cascade unless overridden
				    url:'addTmplPage',
				    frame:false,
				    title: '',
				    region: 'center',
				    fileUpload:true,
				    bodyStyle:'padding:5px 5px 5px 5px',
				    width: 500,

				    items: [{
				        xtype:'fieldset',
				        title: ' ',
				        collapsible: true,
				        autoHeight:true,
				        defaults: {width: 210},
				        items :[{
				        	    xtype: 'textfield',
				                fieldLabel: '参数名称',
				                hidden:true,
				                name: 'PName',
				                value: 'gundong'
				            },{
				            	xtype: 'textfield',
				                fieldLabel: '描述',
				                name: 'PDesc'
				            },{
				            	xtype: 'textfield',
				                fieldLabel: '排序',
				                name: 'POrder'
				            },{
				            	xtype:'fileuploadfield',
				            	name:'file',
				            	//id:'file',
				            	fieldLabel: '上传文件',
				            	buttonText: '选择图片'
				            }
				        ]
				    }]
				});
				
				
				var cfg = {
				    	title: '添加图片',
				    	id: 'add-img-chooser-dlg',
				    	layout: 'border',
						width: 500,
						height: 300,
						modal: true,
						closeAction: 'hide',
						border: false,
						items:[this.fsf],
						buttons: [{
							id: 'ok-btn',
							text: '确定',
							handler: this.submitForm,
							scope: this
						},{
							text: '取消',
							handler: function(){ this.win.hide(); },
							scope: this
						}],
						keys: {
							key: 27, // Esc key
							handler: function(){ this.win.hide(); },
							scope: this
						}
					};
					Ext.apply(cfg, this.config);
				    this.win = new Ext.Window(cfg);
				
			}
			this.reset();
		    this.win.show(el);
			this.callback = callback;
			this.animateTarget = el;
		},
		reset:function(){
			;
		},
		doCallback:function(){
			alert("");
		},
		submitForm:function(){
			if(this.fsf.getForm().isValid()){
			this.fsf.getForm().submit({
                url: 'addSysDicPara',
                waitMsg: '正在上传请稍等...',
                success: function(fp, o){
                	//alert('ok');
                    //msg('Success', 'Processed file "'+o.result.file+'" on the server');
                	Ext.Msg.alert('Status', '操作成功');
                },
                failure :function(fm,rp){	                    	
                	Ext.Msg.alert('Status', '操作异常，请联系管理员.');
                }
            });
			}
		}
		
		
}

DicParaMng.dicParaObj.doDel = function(id){
	/**/
	Ext.Ajax.request({
		   url: 'delSysDicPara',
		   method:'post',
		   success: function(response, opts) {
			      var obj = Ext.decode(response.responseText);
			      //if(obj.success == true){
			      MediaDataMng.viewDataOpt.store.reload();
			      Ext.Msg.alert('系统提示',obj.info);
			       
			      //}
			      
		   },
		   failure: function(response, opts) {
			   Ext.Msg.alert('系统提示', '操作失败！');
		   },
		   params: {'idArray': id }
		});
}
    
Ext.onReady(DicParaMng.dicParaObj.init, DicParaMng.dicParaObj);
</script>