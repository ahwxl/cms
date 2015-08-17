<%@ page language="java" pageEncoding="utf-8" %>
<script type="text/javascript">
 /**
  * @module name 添加文章
  * @module desc 文章管理
  * @author  wxl
  * @create  date 20130306
  * @modify  man ***
  * @modify  date ***
  */
  Ext.namespace('ArticleMng', 'ArticleMng.articleEditor');


  //create application
  ArticleMng.articleEditor = function() {
    // do NOT access DOM from here; elements don't exist yet

    //此处定义私有属性变量
    //var dragZone1, dragZone2;
    //this.addArticleForm;
    var cke_editor_id =Ext.id();
    var addArticleForm=null;
    //此处定义私有方法
    this.onClose = function (){
    }
    
    

    //共有区
    return {
        //此处定义共有属性变量
        

        //共有方法
        init: function() {
      	  
            //定义一些初始化行为
     	   //获取id为"docs-文章管理"的面板
     	    var aimobj = Ext.mainScreem.findById('docs-修改文章');
     	    var lyobj = new Ext.layout.FitLayout();//创建面板布局对象
     	    aimobj.setLayout(lyobj);//应用布局对象
     	    addArticleForm = new ArticleMng.articleEditor.myform({title:'修改文章',cke_editor_id:cke_editor_id,id:'myatcform'});
     	    aimobj.add(addArticleForm);//添加目录树面板
     	    aimobj.doLayout();//展示页面
     	    
     	    //加载列表数据
     	    /**/
     	    CKEDITOR.replace( cke_editor_id,
     	    	    {
     	    	        filebrowserBrowseUrl : 'ckfinderPop',
     	    	        filebrowserUploadUrl : 'uploader/upload.php',
     	    	        filebrowserImageWindowWidth : '640',
     	    	        filebrowserImageWindowHeight : '580'
     	    	    });
     	    
     	    aimobj.on('close', onClose, this, {
			single: true,
			delay: 100
			});

     	    
        }
    };
  }(); // end of app 

/**
 * 创建目录树对象
 */
ArticleMng.articleEditor.catalogTree = new Ext.tree.TreePanel({
	    //renderTo: 'tree-div',
	    title:'目录树',
	    useArrows: true,
	    //autoScroll: true,
	    autoHeight:true,
	    animate: true,
	    region: 'west',
	    //enableDD: true,
	    containerScroll: true,
	    width : 230,
	    border: false,
	    // auto create TreeLoader
	    loader: new Ext.tree.TreeLoader({dataUrl:'showCatalogTree'}),//请求的url地址
	    tools : [{
			id : 'refresh',
			handler : function() {
				ArticleMng.articleListMng.catalogTree.root.reload()
			}
		}],
		listeners:{
			dblclick : function(node,e){
				ArticleMng.articleEditor.processDefComboxTree.setValue(node.text);
				//ArticleMng.articleEditor.myform.getForm().findField('catalog_id').setValue(node.id);
				Ext.getCmp('myatcform').getForm().findField('catalog_id').setValue(node.id);
				ArticleMng.articleEditor.processDefComboxTree.collapse();
			}
		},
	    root: {
	        nodeType: 'async',
	        text: '目录树',
	        draggable: false,
	        id: '0000'
	    }
	});
//添加目录树节点点击事件，添加别的事件也是这种方式，以此类推
ArticleMng.articleEditor.catalogTree.on('click', function(node,e) {
	e.stopEvent();
	node.expand();
	//deptid = node.attributes.id;
	//ArticleMng.articleListMng.store.load();
});

ArticleMng.articleEditor.processDefComboxTree=new Ext.form.ComboBox({
	//id : 'parentdeptname',
	store : new Ext.data.SimpleStore({
				fields : [],
				data : [[]]
			}),
	editable : false,
	value : ' ',
	emptyText : '请选择...',
	fieldLabel : '目录名称',
	//anchor : '100%',
	mode : 'local',
	triggerAction : 'all',
	width:210,
	maxHeight : 300,
	// 下拉框的显示模板,addDeptTreeDiv作为显示下拉树的容器
	tpl : "<tpl for='.'><div style='height:230px'><div id='editorDeptTreeDiv'></div></div></tpl>",
	allowBlank : false,
	onSelect : Ext.emptyFn,
	listeners:{
							expand:function(){	 
								 ArticleMng.articleEditor.catalogTree.render('editorDeptTreeDiv');
							//	 birtReport.report.reportTypeTree.getRootNode().expand();
							//	 birtReport.report.reportTypeTree.root.reload();
							},
							collapse:function(){
								//alert("1"); 
								//this.comboBox.expand();
								//this.expand();
							}
						}
});

//监听下拉框的下拉展开事件
/* ArticleMng.articleEditor.processDefComboxTree.on('expand', function() {
			// 将UI树挂到treeDiv容器
			ArticleMng.articleEditor.catalogTree.render('addDeptTreeDiv');
			// addDeptTree.root.expand(); //只是第一次下拉会加载数据
			ArticleMng.articleEditor.catalogTree.root.reload(); // 每次下拉都会加载数据

}); */


ArticleMng.articleEditor.myform = function (config){
	  this.cke_editor_id = config.cke_editor_id;
	  this.getCkeditorObj = function (){
		  //alert(config.cke_editor_id);
		  //alert(CKEDITOR.instances[config.cke_editor_id].getData());
		  return CKEDITOR.instances[config.cke_editor_id].getData();
	  }
	  
	  var submitForm = function(){
		  alert(this.title);
		  alert();
	  }
	  
	  this.config={
	  labelWidth: 75, // label settings here cascade unless overridden
      url:'save-form.php',
      id: config.id,
      frame:true,
      title: '发布文章',
      bodyStyle:'padding:5px 5px 0',
      width: 650,
      defaults: {width: 230},
      defaultType: 'textfield',
      items: [{
              fieldLabel: '编号',
              hidden:true,
              id:'id',
              name: 'id'
          },{
              fieldLabel: '文章标题',
              name: 'cnt_caption',
              id :'cnt_caption',
              allowBlank:false
          },{
              fieldLabel: '副标题',
              
              name: 'second_caption'
          },{
              fieldLabel: '选择目录',
              hidden:true,
              id:'catalog_id',
              name: 'catalog_id'
          },
          ArticleMng.articleEditor.processDefComboxTree,
          {
              fieldLabel: '选择目录',
              hidden:true,
              name: 'content'
          } /**/,new Ext.BoxComponent({
        	  name:'content1',
        	  id: this.cke_editor_id,
        	  fieldLabel:'文章内容',
        	    autoEl: {
        	        tag: 'textarea',        	        
        	        name:'content1'
        	    }
        	})
        	
        	
      ],
      listeners:{
  		afterrender :function(formobj) {
  			Ext.Ajax.request({
  		         url: 'queryCntById?id=${fmContent.id}',
  		         method:'POST',
  		         success: function(response, opts) {
  		             var obj = Ext.decode(response.responseText);
  		           formobj.getForm().findField('id').setValue(obj.id);
  		           formobj.getForm().findField('cnt_caption').setValue(obj.cnt_caption);
  		           formobj.getForm().findField('catalog_id').setValue(obj.catalog_id);
  		           //formobj.getForm().findField('content1').setValue(obj.content);
  		           CKEDITOR.instances[formobj.cke_editor_id].setData(obj.content);
  		           //alert(formobj.cke_editor_id);
  		             //Ext.Msg.alert('系统提示',obj.id);
  		         },
  		         failure: function(response, opts) {
  		          Ext.Msg.alert('系统提示', '操作失败！');
  		         },
  		         params: {}
  		      });
  	        
  	     }
  	},

      buttons: [{
          text: 'Save',
          scope:this,
          handler :this.confirm
      },{
          text: '重置',
          scope:this,
          handler:this.btnReSet
      }]
    };
	//Ext.apply(this.config,config);
	MainPanel.superclass.constructor.call(this, this.config);
}
  
  
  
  
  
Ext.extend(ArticleMng.articleEditor.myform, Ext.FormPanel,{
	/*initEvents : function(){
        ArticleMng.articleEditor.myform.superclass.initEvents.call(this);
        this.body.on('click', this.onClick, this);
    }*/
    title: '发布文章2',
    id:'myArticleForm',
    confirm:function(){
        	  //getCkeditorObj();
        	  //submitForm();
        	  //alert(this.title);
        	  if(this.getForm().isValid()){
        		  //alert(this.getCkeditorObj());
        		  this.getForm().findField('content').setValue(this.getCkeditorObj());
        		  this.getForm().submit({
	                    url: 'editorContent',
	                    waitMsg: '处理中...',
	                    success: function(fp, o){
	                        Ext.Msg.show({
	                            title: '系统提示',
	                            msg: '操作成功！',
	                            minWidth: 200,
	                            modal: true,
	                            icon: Ext.Msg.INFO,
	                            buttons: Ext.Msg.OK
	                        });
	                    },
	                    failure :function(){
	                    	Ext.Msg.show({
	                    		title: '系统提示',
	                            msg: '操作失败！',
	                            minWidth: 200,
	                            modal: true,
	                            icon: Ext.Msg.INFO,
	                            buttons: Ext.Msg.OK
	                        });
	                    }
	                });
              }
          },
     btnReSet:function(){
        	  this.getForm().reset();
     } 
});
  
  
  
 /**
  * 创建目录树对象
  */
 Ext.onReady(ArticleMng.articleEditor.init, ArticleMng.articleEditor);
// alert(addArticleForm.title);
</script>