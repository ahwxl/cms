<%@ page language="java" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<script type="text/javascript">

var fibasic = new Ext.ux.form.FileUploadField({
	fieldLabel: '选择文件',
    width: 400
});

var fsf = new Ext.FormPanel({
        labelWidth: 75, // label settings here cascade unless overridden
        url:'addTmplPage',
        frame:false,
        title: '上传模板文件',
        fileUpload:true,
        bodyStyle:'padding:5px 5px 5px 5px',
        width: 500,

        items: [{
            xtype:'fieldset',
            title: 'Phone Number',
            collapsible: true,
            autoHeight:true,
            defaults: {width: 210},
//            defaultType: 'textfield',
            items :[{
            	    xtype: 'textfield',
                    fieldLabel: '模板名称',
                    name: '',
                    value: ''
                },{
                	xtype: 'textfield',
                    fieldLabel: '描述',
                    name: 'business'
                },{
                	xtype: 'textfield',
                    fieldLabel: '类别',
                    name: 'mobile'
                },{
                	xtype:'fileuploadfield',
                	name:'file',
                	id:'file',
                	fieldLabel: '上传文件',
                	buttonText: '选择文件'
                	
                }
            ]
        }],

        buttons: [{
            text: '保存',
            handler: function(){
                if(fsf.getForm().isValid()){
                	fsf.getForm().submit({
	                    url: 'addTmplPage',
	                    waitMsg: '正在上传请稍等...',
	                    success: function(fp, o){
	                    	alert('ok');
	                        msg('Success', 'Processed file "'+o.result.file+'" on the server');
	                    },
	                    failure :function(fm,rp){	                    	
	                    	//alert(rp.result);
	                    	Ext.Msg.alert('Status', '操作异常，请联系管理员.');
	                    }
	                });
                }
            }
        },{
            text: '重置',
            handler: function(){
            	//Ext.myloadMask.show();
            	//Ext.myloadMask.hide();
            	fsf.getForm().reset();
            }
        }]
    });


var aimobj = Ext.mainScreem.findById('docs-上传模板');
//fsf.doLayout();
aimobj.add(fsf);
aimobj.doLayout();

</script>