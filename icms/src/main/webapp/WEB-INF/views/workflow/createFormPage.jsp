<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
/**
 * @module name 工作流管理
 * @module desc 流程表单管理
 * @author  wxl
 * @create  date 2012-7-7
 * @modify  man ***
 * @modify  date ***
 */
 Ext.namespace('workflowMng', 'workflowMng.processInstanceMngApp');


//create application
workflowMng.processInstanceMngApp = function() {
  // do NOT access DOM from here; elements don't exist yet

  //此处定义私有属性变量
  var dragZone1, dragZone2;

  //此处定义私有方法

  //共有区
  return {
      //此处定义共有属性变量
      

      //共有方法
      init: function() {
    	  
          //定义一些初始化行为
    	  var aimobj = Ext.mainScreem.findById('docs-流程表单管理');//获取打开得页签对象
    	  var lyobj = new Ext.layout.BorderLayout();//定义面板
    	  aimobj.setLayout(lyobj);//给页签对象设置布局格式
    	  //aimobj.add(workflowMng.processInstanceMngApp.deptTree);
    	  aimobj.add(workflowMng.processInstanceMngApp.gridPanelObj);
    	  aimobj.doLayout();//强制布局
    	  
    	  

          
    	  //SysUserMng.user.organizeTreePanel.getRootNode().expand();    	  
    	  workflowMng.processInstanceMngApp.gridStoreObj.load({params:{start:0, limit:10}});
      }
  };
}(); // end of app

$JIT.loaded('module/editorContent/editor');
$JIT.loaded('resources/js/ckeditor3.6.2/ckeditor');
$JIT.script('module/editorContent/editor');
$JIT.script('resources/js/ckeditor3.6.2/ckeditor');
Ext.onReady(workflowMng.processInstanceMngApp.init, workflowMng.processInstanceMngApp);
</script>