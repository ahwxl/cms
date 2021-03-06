<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="resources/js/ext-3.4.0/resources/css/ext-all.css" />
<link rel="stylesheet" type="text/css" href="resources/js/ext-3.4.0/resources/css/ux-all.css" />
<link rel="stylesheet" type="text/css" href="resources/js/ext-3.4.0/resources/css/xtheme-gray.css" />
<!----><link rel="stylesheet" type="text/css" href="resources/css/style.css?v=20150808" />
<!----><link rel="stylesheet" type="text/css" href="resources/css/silk.css?v=20150806" />
<link rel="stylesheet" type="text/css" href="resources/css/ext_css_patch.css" />
<script type="text/javascript" src="resources/js/ext-3.4.0/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="resources/js/ext-3.4.0/ext-all.js"></script>
<script type="text/javascript" src="module/A_sys/TabCloseMenu.js"></script>
<script type="text/javascript" src="module/A_sys/docs.js?20150901"></script>
<script type="text/javascript" src="module/A_sys/ext-lang-zh_CN.js"></script>
<script type="text/javascript" src="module/A_sys/tree.js?v20150817"></script>
<title>内容管理</title>
</head>
<body scroll="no">

<div id="loading-mask"></div>
<div id="loading">
    <div class="loading-indicator">
        <img src="resources/images/common/extanim32.gif" width="32" height="32" style="margin-right:8px;" align="absmiddle" />
        Loading&hellip;
    </div>
</div>

<div id="header" style="background-color: #025b80;">
    <!--<img style="margin-left: 5px" src="" alt="Ext JS API Documentation" height="50" width="210" />-->
    <div style="float:left;width:300px;margin-top:9px;margin-left:10px;color:white;"><h1 class="sys_logo_bg1">内容管理平台<!-- 协作管理平台 --></h1></div>
    <div style="float:right; margin-top: 15px;margin-right: 10px;color:white;">
                    <a href="#" onclick="updateUserPassWordWin()" >欢迎</a> | 
        <a href="loginOut" style="padding:5px">退出</a> | 
        <a href="http://www.landfalcon.com/" style="padding:5px" target="_black">联系我们</a>
        <input type="hidden" value="" id="userName">
        <input type="hidden" id="isVerifySuccess">
    </div>
</div>

<div id="classes"></div>

<div id="main"></div>

<div id="screem"><div id="screem2">欢迎您！</div></div>

<select id="search-options" style="display:none">
    <option>Starts with</option>
    <option>Ends with</option>
    <option>Any Match</option>
</select>










<script type="text/javascript" src="resources/js/sys/ext-fixes.js"></script>
<script type="text/javascript" src="resources/js/sys/ext-basex.js"></script>
<script type="text/javascript" src="resources/js/sys/jit.js"></script>
<!-- 富文本编辑器 -->
<script type="text/javascript" src="module/editorContent/editor.js"></script>
<script type="text/javascript" src="resources/js/ckeditor3.6.2/ckeditor.js"></script>
<script type="text/javascript" src="resources/js/ckeditor.js"></script>
<!--  <script type="text/javascript" src="resources/js/jquery-1.7.2.min.js"></script>-->
<script type="text/javascript">

//自定义验证密码

Ext.apply(Ext.form.VTypes,{ 
password : function(val, field) 
{ 
        if (field.initialPassField) 
{ 
            var pwd = Ext.getCmp(field.initialPassField); 
            return (val == pwd.getValue()); 
        } 
        return true; 
    }, 
    passwordText : '确认密码有误，请重新输入！' 
});


      /* Define our dependency table for ux module names */
Ext.onReady(function(){

     var ux= 'resources/js/sys/', extux = 'lib/ext-3.3+/examples/ux/';
     
     Ext.apply(
      $JIT.depends , {
      // JS source file   | source location    | Dependencies (in required load order)
        'uxvismode'   :   {path: ux }
       ,'multidom'    :   {path: ux }
       ,'uxmedia'     :   {path: ux ,          depends: [ '@uxvismode' ,'@uxmask']}
       ,'uxaudio'     :   {path: ux ,          depends: [ '@uxmedia']}
       ,'uxflash'     :   {path: ux ,          depends: [ '@uxmedia'] }
       ,'uxchart'     :   {path: ux ,          depends: [ '@uxflash' ] }
       ,'uxfusion'    :   {path: ux ,          depends: [ '@uxchart'] }
       ,'uxofc'       :   {path: ux ,          depends: [ '@uxchart'] }
       ,'uxamchart'   :   {path: ux ,          depends: [ '@uxchart'] }
       ,'uxflex'      :   {path: ux ,          depends: [ '@uxflash'] }
	   ,'audioevents' :   {path: ux }
	   ,'uxmask'      :   {path: ux }
       ,'mif'         :   {path: ux ,          depends: ['@multidom' , '@uxvismode'] }     
       ,'mifmsg'      :   {path: ux ,          depends: [ '@miframe'] }
       ,'mifdd'       :   {path: ux ,          depends: [ '@miframe'] }
       ,'miframe'     :   {virtual : true,     depends: [ '@mif'] }
      
      });
	 
	  var url = location.href.split('#')[0],
        params = url.split('?')[1] || '',
        args = Ext.urlDecode(params) || {}, 
        script = args.script || args.demo ;
       
        
      $JIT.setMethod('DOM'); //<script> tags for debugging/viewing
      Ext.ResourceLoader.debug = !!args.debug;  	
	  Ext.ResourceLoader.disableCaching = false;

      //load the demo site startup scripts
      $JIT(
           {debug: true},
           ux + 'ux-all',
           ux + 'codelife', 
		   ux + 'toolstips',
		   ux + 'shortcuts',
		   ux+'multidom',
		   ux+'uxworkers',
		   '@uxmedia',
		   '@uxflash',
		   '@audioevents',
		   'resources/js/sys/demowin',
		    ux+'mif',
		   'module/B_mif/startup'
		  );
});

var updateUserPassWordForm = new Ext.FormPanel({
    labelWidth: 75, // label settings here cascade unless overridden
    url:'updateSysUserPwd',
    frame:false,
    title: '修改密码',
    fileUpload:true,
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 150,

    items: [{
        xtype:'fieldset',
        title: '修改密码',
        collapsible: true,
        autoHeight:true,
        defaults: {width: 200},
//        defaultType: 'textfield',
        items :[{
        	    xtype: 'textfield',
                fieldLabel: '当前密码',
                inputType : "password",
                allowBlank:false,
                name: 'userPwd',
                id: 'userPwd2'
                
            },{
                xtype:'textfield',
                inputType : "password",
                fieldLabel: '新口令',
                allowBlank:false,
                name: 'newPwd',
                id: 'userPwd3',
                listeners:{
                'focus':function(){
                  verifyPassword();
                }}
            },{
                inputType : 'password',
                vtype : "password",
                xtype : "textfield",
                fieldLabel: '确认口令',
                allowBlank:false,
                name: 'passwd',
                vtypeText:"两次密码不一致！", 
                initialPassField : 'userPwd3' // id of the initial         
            }
        ]
    }],

    buttons: [{
        text: '保存',
        handler: function(){
            if(updateUserPassWordForm.getForm().isValid()){
                var userName  = Ext.getDom('userName').value ;
            	updateUserPassWordForm.getForm().submit({
                    url: 'updateSysUserPwd',
                    waitMsg: '正在保存请稍等...',
                    success: function(form, action){
                    	Ext.MessageBox.alert('Success', action.result.info);
                    		updateUserPassWord.hide();
                    },
                    failure :function(fm,rp){	                    	
                    	//alert(rp.result);
                    	Ext.Msg.alert('Status', '操作异常，请联系管理员.');
                    },
                    params : {
					userName : userName
				}
                });
            }
        }
    },{
        text: '关闭',
        handler: function(){
        	//Ext.myloadMask.show();
        	//Ext.myloadMask.hide();
        	updateUserPassWordForm.getForm().reset();
        	updateUserPassWord.hide();
        }
    }]
});
var updateUserPassWord = new Ext.Window({
    width:350, height: 220,
    layout: 'fit', // explicitly set layout manager: override the default (layout:'auto')
    closeAction:'hide',
    items: [updateUserPassWordForm]
});
function updateUserPassWordWin(){
Ext.getDom('isVerifySuccess').value = "" ;
updateUserPassWordForm.getForm().reset();
updateUserPassWord.show();
}
function verifyPassword() {
    var userPwd = Ext.getCmp("userPwd2").getValue();
    var userName  = Ext.getDom('userName').value ;
    var isVerifySuccess = Ext.getDom('isVerifySuccess').value;
    if(isVerifySuccess!="sucess"){
	Ext.Ajax.request({
				url : 'verifyPassword',
				success : function(response) {
					var resultArray = Ext.util.JSON
							.decode(response.responseText);
							if("sucess"==resultArray.info)
							Ext.getCmp("isVerifySuccess").setValue("sucess");
					Ext.Msg.alert('提示', resultArray.info);
				},
				failure : function(response) {
					Ext.MessageBox.alert('提示', '数据验证失败');
				},
				params : {
					userPwd : userPwd,
					userName : userName
				}
			});
			}
	}
</script>



</body>
</html>