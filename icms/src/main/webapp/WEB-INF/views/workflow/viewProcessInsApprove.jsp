<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
/**
 * @module name 工作流管理
 * @module desc 创建流程实例
 * @author  wxl
 * @create  date 2012-7-7
 * @modify  man ***
 * @modify  date ***
 */
 
 Ext.namespace('workflowMng', 'workflowMng.createProcessInsPageApp');


//create application
workflowMng.createProcessInsPageApp = function() {
  // do NOT access DOM from here; elements don't exist yet

  //此处定义私有属性变量
  var dragZone1, dragZone2;

  //此处定义私有方法

  //共有区
  return {
      //此处定义共有属性变量
      

      //共有方法
      init: function() {
    	  //alert('ab');
          //定义一些初始化行为
    	  var aimobj = Ext.mainScreem.findById('docs-审批流程');//获取打开得页签对象
    	  var lyobj = new Ext.layout.BorderLayout();//定义面板
    	  aimobj.setLayout(lyobj);//给页签对象设置布局格式
    	  //aimobj.add(workflowMng.createProcessInsPageApp.myPanel);
    	  aimobj.add(fsf);
    	  //aimobj.add(moduleName.appName.gridPanelObj);
    	  aimobj.doLayout();//强制布局
    	  
    	  
  		workflowMng.createProcessInsPageApp.procActFormPanel2.load({
  		    url: 'getProcessActivityForm',
  		    params: {processDefId: '${id}', taskId: '${taskId }'}, // or a URL encoded string
  		    callback: callbackGetStartForm,
  		    //scope: yourObject, // optional scope for the callback
  		    discardUrl: false,
  		    nocache: false,
  		    text: 'Loading...',
  		    timeout: 30,
  		    scripts: false
  		});
  		workflowMng.createProcessInsPageApp.hisGridStore.load({params:{taskId: '${taskId }'}});

          
    	  //SysUserMng.user.organizeTreePanel.getRootNode().expand();
    	  //SysUserMng.user.userGridStore.load({params:{start:0, limit:10}});
      }
  };
}(); // end of app

workflowMng.createProcessInsPageApp.procActFormPanel =new Ext.Panel({//width: 600,height:200,
	//title: '基础表单',
	//autoWidth: true,
	bodyStyle:'padding:0px 0px 0',
	autoScroll:false,
	autoHeight : false
	});
workflowMng.createProcessInsPageApp.procActFormPanel2 =new Ext.Panel({//width: 600,height:200,
	//title: '基础表单',
	//autoWidth: true,
	bodyStyle:'padding:0px 0px 0',
	autoScroll:false,
	autoHeight : false,
	id : 'procActFormPanel2'
	});
	
 var reader = new Ext.data.ArrayReader({}, [
       {name: 'taskId'},
       {name: 'formId'},
       {name: 'formInfo'}
    ]);	
workflowMng.createProcessInsPageApp.hisGridStore = new Ext.data.Store({
	// True表示为，每次Store加载后，清除所有修改过的记录信息；record被移除时也会这样
	pruneModifiedRecords : true,
	proxy : new Ext.data.HttpProxy({
				url : 'getProcessTaskHisItemJson?processInsId=${processInsId}'
			}),
	reader : new Ext.data.JsonReader({
				totalProperty : 'totalCount',
				root : 'data'
			}, [{
						name : 'id'
					}, {
						name : 'assignee'
					},{
						name:'activityName'
					},{
						name:'endTime'
					}])
});

 var expander = new Ext.ux.grid.RowExpander({
        tpl : new Ext.Template(
            '<p><b>任务编号:<b> {id}<p><br>',
            '<p><b>历史记录:<b> {assignee}<p>'
        )
    });
    
    
    workflowMng.createProcessInsPageApp.hisGridPanel = new Ext.grid.GridPanel({
        store: workflowMng.createProcessInsPageApp.hisGridStore,
        cm: new Ext.grid.ColumnModel({
            defaults: {
                width: 20,
                sortable: true
            },
            columns: [
                expander,
                {id:'procinstId',header: "任务编号", width: 40, dataIndex: 'id'},
                {id:'assignee',header: "活动名称", width: 40, dataIndex: 'activityName'},
                {id:'assignee',header: "审批人", width: 40, dataIndex: 'assignee'},
                {id:'assignee',header: "操作时间", width: 40, dataIndex: 'endTime'},
                {id:'assignee2',header: "审批结果", width: 40, dataIndex: ''}
            ]
        }),
        viewConfig: {
            forceFit:true
        },
        autoWidth:true,
        loadMask :{msg:"Please wait..."},
        //width: 600,
        height: 300,
        plugins: expander,
        collapsible: true,
        animCollapse: false
        //iconCls: 'icon-grid'
       // renderTo: document.body
    });

function callbackGetStartForm(){
	
	//alert('abc');
}

var fsf = new Ext.FormPanel({
    labelWidth: 75, // label settings here cascade unless overridden
    frame:true,
    title: '审批流程',
    region : 'center',
    autoScroll:true,
    bodyStyle:'padding:5px 5px 0',
    width: 600,

    items: [{
        xtype:'fieldset',
        checkboxToggle:false,
        title: '处理',
        autoHeight:true,
        defaults: {width:300},
        //defaultType: 'textfield',
        collapsed: false,
        items :[{
        	    xtype:'textfield',
                fieldLabel: 'processKey',
                hidden:true,
                name: 'processKey',
                value:'${processKey}'
            },{
            	xtype:'textfield',
                fieldLabel: '流程编码',
                hidden:true,
                name: 'id',
                value:'${processInsId}',
                allowBlank:false
            },{
            	xtype:'textfield',
                fieldLabel: 'taskID',
                hidden:true,
                name: 'taskId',
                value:'${taskId}',
                allowBlank:false
            },{
                xtype: 'radiogroup',
                fieldLabel: '操作',
                name: 'approveAction',
                items: [
                    {boxLabel: '通过', name: 'approveAction', inputValue:'true'},
                    {boxLabel: '驳回', name: 'approveAction', inputValue: 'false', checked: true}
                ]
            },{
            	xtype:'textfield',
                fieldLabel: '选择操作人',
                name: 'curentAgentUserId',
                emptyText:'点击，选择操作人',
                listeners: {focus:function(el,e){ showChooserUserWin(el);  }  },
                allowBlank:false,
                value:'${userId}'
            },{
            	xtype:'textarea',
            	fieldLabel:'审批意见',
            	allowBlank:false,
            	name:'comment'
            }
        ]
    },{
        xtype:'fieldset',
        //checkboxToggle:false,
        title: '表单信息',
        autoHeight:true,
        name:'thisform',
        id:'thisformInfo',
        //defaults: {width:300},
        //defaultType: 'textfield',
        //collapsed: false,
        items :[workflowMng.createProcessInsPageApp.procActFormPanel2,
                new Ext.Button({
                           text: '添加附件',
                           name: 'fujian',
                           id:'fujian',
                           handler: function(){workflowMng.createProcessInsPageApp.attachmentWin.show();}
                       }),          
                new Ext.BoxComponent({
                    id:'filediv',
                    autoEl: {id:'filediv',
                             tag: 'div'
                    }
                })
                ]
     },{
         xtype:'fieldset',
         //checkboxToggle:false,
         title: '历史审核信息',
         autoHeight:true,
         //defaults: {width:300},
         //defaultType: 'textfield',
         //collapsed: false,
         items :[workflowMng.createProcessInsPageApp.hisGridPanel
         ]
      }
    ],
    tbar:new Ext.Toolbar({
    items: [
        {
            // xtype: 'button', // default for Toolbars, same as 'tbbutton'
            text: 'Button'
        },
        {
            xtype: 'splitbutton', // same as 'tbsplitbutton'
            text: 'Split Button'
        },
        // begin using the right-justified button container
        '->', // same as {xtype: 'tbfill'}, // Ext.Toolbar.Fill
        {
            xtype: 'textfield',
            name: 'field1',
            emptyText: 'enter search term'
        },
        // add a vertical separator bar between toolbar items
        '-', // same as {xtype: 'tbseparator'} to create Ext.Toolbar.Separator
        'text 1', // same as {xtype: 'tbtext', text: 'text1'} to create Ext.Toolbar.TextItem
        {xtype: 'tbspacer'},// same as ' ' to create Ext.Toolbar.Spacer
        'text 2',
        {xtype: 'tbspacer', width: 50}, // add a 50px space
        'text 3'
    ]
}),
    buttons: [{
        text: '提交审批',
       	handler : function() { // 按钮响应函数
			submitTheForm();
		}
    },{
        text: '返回',
        handler: function(){
        	Ext.mainScreem.remove('docs-审批流程');
        }
    }]
});


workflowMng.createProcessInsPageApp.addAttachmentForm = new Ext.FormPanel({
    labelWidth: 75,
    url:'addTmplPage',
    frame:false,
    fileUpload:true,
    bodyStyle:'padding:5px 5px 5px 5px',
    width: 500,
    items: [{
        xtype:'fieldset',
        title: '请选择附件',
        collapsible: true,
        autoHeight:true,
        defaults: {width: 210},
        items :[{
            	  xtype: 'fileuploadfield',
                  fieldLabel: '选择文件',
                  name: 'file'
               }]
    }],

    buttons: [{
        text: '保存',
        handler: function(){
            if(workflowMng.createProcessInsPageApp.addAttachmentForm.getForm().isValid()){
            	workflowMng.createProcessInsPageApp.addAttachmentForm.getForm().submit({
                    url: 'uploadProcessAttachmentFile',
                    //method:'post',
                    waitMsg: '正在上传请稍等...',
                    success: function(form,action){
                        workflowMng.createProcessInsPageApp.attachmentWin.hide();
                    	showFileLabel(action.result.fileId,action.result.fileName);
                    },
                    failure :function(fm,rp){	                    	
                    	Ext.Msg.alert('Status', '操作异常，请联系管理员.');
                    }
                });
            }
        }
    },{
        text: '关闭',
        handler: function(){
        	workflowMng.createProcessInsPageApp.attachmentWin.hide();
        }
    }]
});


workflowMng.createProcessInsPageApp.attachmentWin = new Ext.Window({
    width:500, height: 150,
    layout: 'fit', 
    title:'添加附件',
    closeAction:'hide',
    items: [workflowMng.createProcessInsPageApp.addAttachmentForm]
});

/**
 * 表单提交(表单自带Ajax提交)
 */
function submitTheForm() {
	if (!fsf.form.isValid())
		return;
    var el = Ext.getCmp("procActFormPanel2").getEl().dom;
    var el2 = Ext.getCmp("filediv").getEl().dom;
	fsf.form.submit({
		url : 'completeProcessInstanc',
		waitTitle : '提示',
		method : 'POST',
		waitMsg : '正在处理数据,请稍候...',
		success : function(form, action) {
			Ext.MessageBox.alert('提示', action.result.msg);
		},
		failure : function(form, action) {
			Ext.MessageBox.alert('提示', '数据保存失败!');
		},
		params : {
			text4 : '文本4附加参数',
			postType: 1,
			formInfo : el.innerHTML+el2.innerHTML
		}
	});
}


/*
workflowMng.createProcessInsPageApp.myPanel = new Ext.Panel({
    //renderTo: 'grid-container',
    monitorResize: true, // relay on browser resize
    region : 'center',
    //title: '创建流程实例',
    height: 400,
    width:600,
    items: [fsf]
});
*/
//get return user item
function fetchUser(recArray){
	var retval ='';
	for(var i = 0;i<recArray.length;i++){
		if(i==0)retval +=recArray[i].data.userName; 
		else retval +=','+recArray[i].data.userName;
	}
	
	fsf.getForm().findField('curentAgentUserId').setValue(retval);
}
//open chooser user window
function showChooserUserWin(el){	
	var win =new AppUserChooserWin();
	win.show(el,fetchUser);
}

function removeFileDiv(id){
    var el = Ext.get("filediv").dom;
    var el2 = Ext.get(id).dom;
    el.removeChild(el2);
}

function showFileLabel(fileId,fileName){
    var el = Ext.get("filediv").dom;
    var oDiv=document.createElement("DIV");
    oDiv.id=Ext.id();
    oDiv.innerHTML='<img src="resources/images/fam/hxz.png"><input type="hidden" value="'+fileId+'" name="fileId"/><font size="3"><a href="downLoadForm?fileId='+fileId+'">'+fileName+'</a></font><img src="resources/images/fam/delete.gif" onClick="removeFileDiv(\''+oDiv.id+'\');">';
    el.appendChild(oDiv);
}


Ext.onReady(workflowMng.createProcessInsPageApp.init, workflowMng.createProcessInsPageApp);
</script>