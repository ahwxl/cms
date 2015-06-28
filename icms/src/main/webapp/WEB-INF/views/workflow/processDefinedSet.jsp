<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
/**
 * @module name 工作流管理
 * @module desc 流程定义设置
 * @author  wxl
 * @create  date 2012-7-7
 * @modify  man ***
 * @modify  date ***
 */
 Ext.namespace('workflowMng', 'workflowMng.processDefinedAppSet');


//create application
workflowMng.processDefinedAppSet = function() {
  // do NOT access DOM from here; elements don't exist yet

  //此处定义私有属性变量
  var dragZone1, dragZone2;

  //此处定义私有方法

  //共有区
  return {
      //此处定义共有属性变量
      

      //共有方法
      init: function() {
    	  alert('');
          //定义一些初始化行为
    	  var aimobj = Ext.mainScreem.findById('docs-流程表单管理');//获取打开得页签对象
    	  var lyobj = new Ext.layout.BorderLayout();//定义面板
    	  aimobj.setLayout(lyobj);//给页签对象设置布局格式
    	  aimobj.add(workflowMng.processDefinedAppSet.tabs);
    	  //aimobj.add(workflowMng.processDefinedApp.gridPanelObj);
    	  aimobj.doLayout();//强制布局
    	  
    	  

          
    	  //SysUserMng.user.organizeTreePanel.getRootNode().expand();    	  
    	  //workflowMng.processDefinedApp.gridStoreObj.load({params:{start:0, limit:10}});
      }
  };
}(); // end of app

workflowMng.processDefinedAppSet.tabs = new Ext.TabPanel({
    activeTab: 0,
    items: [{
        title: '设置角色',
        html: 'A simple tab'
    },{
        title: '设置表单',
        html: 'Another one'
    }]
});












Ext.onReady(workflowMng.processDefinedAppSet.init, workflowMng.processDefinedAppSet);
</script>