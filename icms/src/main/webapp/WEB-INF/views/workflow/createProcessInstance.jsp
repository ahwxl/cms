<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
/**
 * @module name 工作流管理
 * @module desc 创建流程实例 step one
 * @author  wxl
 * @create  date 2012-7-7
 * @modify  man ***
 * @modify  date ***
 */
 
//Ext.namespace('workflowMng', 'workflowMng.createProcessInsPageApp');

workflowMng.createProcessInsPageApp = Ext.extend(Ext.FormPanel,{
	constructor:function(a) {
        Ext.applyIf(this,a);
        this.init();
        this.items = [this.fieldset,this.baseForm];
        workflowMng.createProcessInsPageApp.superclass.constructor.call(this, this.config);
        this.initMainPanel();
       
    }
});


//初始化表单
workflowMng.createProcessInsPageApp.prototype.init = function() {
  // do NOT access DOM from here; elements don't exist yet
  var curFormPanel;
  this.selectUserWin = null;
  
  this.initMainPanel=function(){
	  var aimobj = Ext.mainScreem.findById('docs-创建流程实例');//获取打开得页签对象
	  aimobj.setAutoScroll(true);
	  var lyobj = new Ext.layout.BorderLayout();//定义面板
	  aimobj.setLayout(lyobj);//给页签对象设置布局格式
	  aimobj.add(this);	  
	  aimobj.doLayout();//强制布局
	  curFormPanel = this;
  };
  
  this.config = {
          id:"createProcessInsPageApp"
              ,region : 'center'
              ,title:"创建流程实例",
              buttons : [{
                  text: '保存'
              },{
                  text: '提交审批',
                 	handler : function() { // 按钮响应函数
          			submitTheForm();
          		}
              },{
                  text: '返回',
                 	handler: function(){
                     	Ext.mainScreem.remove('docs-创建流程实例');
                     }
              },'']
          };
  
  //此处定义私有属性变量
  this.baseForm = new Ext.Panel({
	    //width: 600,height:200,
		title: '基础表单',
		//autoWidth: true,
		margins:{top:0, right:5, bottom:0, left:5},
		autoScroll:false,
		listeners: {afterlayout:function(obj,e){
			obj.load({
    		    url: 'getProcessStartFormStr',
    		    params: {processDefId: '${id}', param2: 'bar'}, // or a URL encoded string
    		    //callback: callbackGetStartForm,
    		    //scope: yourObject, // optional scope for the callback
    		    discardUrl: false,
    		    nocache: false,
    		    text: 'Loading...',
    		    timeout: 30,
    		    scripts: false
    		});
        }},
		autoHeight : false
	});

  
  //get return user item
  var fetchUser = function(recArray){
  	var retval ='';
  	for(var i = 0;i<recArray.length;i++){
  		if(i==0)retval +=recArray[i].data.userName; 
  		else retval +=','+recArray[i].data.userName;
  	}
  	
  	curFormPanel.getForm().findField('assignee').setValue(retval);
  };
  //open chooser user window
  var showChooserUserWin =function (el){
  	if(this.selectUserWin == null){
  		this.selectUserWin = new AppUserChooserWin();
  	}
  	//var win =new AppUserChooserWin();
  	this.selectUserWin.show(el,fetchUser);
  };
  
  this.fieldset =new Ext.form.FieldSet({
	        xtype:'fieldset',
	        checkboxToggle:false,
	        title: '处理',
	        autoHeight:true,
	        defaults: {width:300},
	        defaultType: 'textfield',
	        collapsed: false,
	        items:[{
	                fieldLabel: 'processKey',
	                name: 'key_',
	                hidden:true,
	                value:'${processKey}'
	            },{
	                fieldLabel: '执行路径',
	                xtype:'radio',
	                boxLabel:'${outTransition }',
	                name: 'processDefId',
	                checked:true,
	                hidden: false,
	                inputValue:'${id}'
	            },{
	                fieldLabel: '选择操作人',
	                name: 'assignee',
	                listeners: {focus:function(el,e){ showChooserUserWin(el);  } },
	                emptyText:'点击选择代办人',
	                allowBlank:false
	            }
	        ]
  });
  
  /**
   * 表单提交(表单自带Ajax提交)
   */
 var submitTheForm = function() {
  	if (!curFormPanel.form.isValid())
  		return;
  	curFormPanel.form.submit({
  		url : 'createProcessInstance',
  		waitTitle : '提示',
  		method : 'POST',
  		waitMsg : '正在处理数据,请稍候...',
  		success : function(form, action) {
  			Ext.MessageBox.alert('提示', action.result.msg);
  		},
  		failure : function(form, action) {
  			Ext.MessageBox.alert('提示', '数据保存失败');
  		},
  		params : {
  			text4 : '文本4附加参数',
  			postType: 1
  		}
  	});
  };
  
}; // end of init

new workflowMng.createProcessInsPageApp({title:'创建流程实例123'	});

//Ext.onReady(workflowMng.createProcessInsPageApp.init, workflowMng.createProcessInsPageApp);
</script>