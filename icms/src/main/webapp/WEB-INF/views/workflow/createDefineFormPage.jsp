<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>绑定活动节点到动态表单--第二步</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<script type="text/javascript" src="resources/js/jbpmworkflow/appworkflow.js"></script>
	<link rel="stylesheet" type="text/css" href="resources/js/easyui-1.1.1/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="resources/js/easyui-1.1.1/themes/default/easyui.css">
	<link rel="stylesheet" href="resources/js/ztree3/css/zTreeStyle/zTreeStyle.css" type="text/css">	
	<script type="text/javascript" src="resources/js/jquery-1.7.2.min.js"></script> 
	<script type="text/javascript" src="resources/js/easyui-1.1.1/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="resources/js/json/json2.js"></script>
	
	<script type="text/javascript" src="resources/js/ztree3/js/jquery.ztree.core-3.0.js"></script> 

<style>
<!--
.mytab5 TD{height:28px;}

.dp{
	opacity:0.5;
	filter:alpha(opacity=80);
}
.over{
	background:#F5F5DC;/*#FBEC88*/
}

.leftmenu{margin-bottom:8px;}

.DIVcontentright INPUT{width:220px;}
.DIVcontainer{width:350px;height:30px;background-color:#F5F5F5 }
.DIVcontentleft{width:80px;height:30px;background-color:#F5F5F5;float:left;text-align:center;font-size:18px;border:1px solid #33CCFF;}
.DIVcontentright{width:270px;height:30px;background-color:#FFFFCC;float:left;border:1px solid #33CCFF;border-left-style:none;}


.hideDiv{display:none;}
.actDivContainer{border:1px solid #E6E6FA;height:300px;}
.aimUserOrRole{border:1px solid #5F9EA0;background-color:#E6E6FA;height:25px;width:200px;margin-bottom:5px;margin-left:5px; }
.focused {background: #abcdef;}

-->
</style>
</head>
<body style="overflow-y:hidden;overflow:hidden;" onload="appInit()">
<form action="" name="myform" method="post">
<table width="100%" height="90%" border="1" cellpadding="0" cellspacing="0">
<tr>
<td colspan="2">
<div style="width:100%;border:0px solid  #E6E6FA;float:left;clear:left;">
<table>
<tr>
<td>表单名称</td><td>:<input id="formName" name="formName" type="text" width="400"/></td>
</tr>
<tr>
<input id="formType" name="formType" type="hidden"/>
<td>表单类型</td><td>:<input id="formTypeName" name="formTypeName" type="text" onclick="showMenu();" readonly width="400"/></td>
</tr>
<tr>
<td>备注</td><td>:<input id="formDesc" name="formDesc" type="text" width="400"></td>
</tr>
</table>
</div>
</td>
</tr>
<tr height="85%">
<td width="16%" valign="top" >
<!-- 左边表单菜单 -->
<div style="width:100%;height:100%;border:1px solid  #E6E6FA;float:left;clear:left;">
<div id="d1" class="drag leftmenu" objectType="text">
文本框<img src="resources/images/workflow/form/form_01.gif" />
</div>
<div id="d2" class="drag leftmenu" objectType="textArea">
文本域<img src="resources/images/workflow/form/form_02.gif" />
</div>
<div id="d3" class="drag leftmenu" objectType="radio">
单选按钮<img src="resources/images/workflow/form/form_03.gif" />
</div>
<div id="d4" class="drag leftmenu" objectType="checkbox">
多选按钮<img src="resources/images/workflow/form/form_04.gif" />
</div>
<div id="d5" class="drag leftmenu" objectType="select">
下拉列表<img src="resources/images/workflow/form/form_05.gif" />
</div>
<div id="d5" class="drag leftmenu" objectType="file">
文件上传<img src="resources/images/workflow/form/form_09.gif" />
</div>
</div>
</td><td valign="top" height="5%">

<div id="wfDesignCr">
<div id="div_act" class="hideDiv actDivContainer" >
<input type="hidden" name="formInfo" id="formInfo" >
<table border="0" cellpadding="0" cellspacing="0"  class="mytab5" >
<tr>
<td id="target" width="350" class="target">&nbsp;</td><td width="350" id="target" class="target">&nbsp;</td>
</tr>
<tr>
<td id="target" class="target" >&nbsp;</td><td width="350" id="target" class="target">&nbsp;</td>
</tr>
<tr>
<td  class="target">&nbsp;</td><td width="350" class="target">&nbsp;</td>
</tr>
</table>
</div>
</div>



</td>
</tr>
</table>
</form>

<input type="button" name="" value=" 确 定 " onclick="doSubmit()"/><input type="button" name="" value=" 返 回 " onclick=""/><input type="button" name="" value=" 下一步 " onclick="nextStep2()"/>


<!-- 编辑表单区 -->
<div id="editFormW" class="easyui-window" title="表单域设置" icon="icon-save" closed="true" style="width:500px;height:450px;padding:5px;background: #fafafa;">
		<div class="easyui-layout" fit="true">
			<div region="center" border="false" style="padding:10px;background:red;border:1px solid #ccc;">
				<form action="" name="myEditFormTmp">
				<div id="tt" class="easyui-tabs" style="width:430px;height:350px;">
						<div title="基础信息" style="padding:20px;display:none;">						  
						  <table>
							<tr>
							<input type="hidden" name="tmpformId"/>
							<td>名称：<br><br><br></td><td><input type="text" name="tmpformName"/><br><br><br><br></td>
							</tr>
							<tr>
							<td>描述：<br><br><br></td><td><textarea rows="5" cols="18" name="tmpformDesc"></textarea>
							<br><br><br><br></td>
							</tr>
							</table>
						</div>
						<br><br><div title="列表信息" style="padding:20px;display:none;">
						<img src="resources/images/workflow/act_plus.gif" style="cursor:pointer;" onclick="addrow('listform')"/>&nbsp;&nbsp;&nbsp;&nbsp; <img src="resources/images/workflow/act_minus.gif" style="cursor:pointer;" onclick="removerow('listform')"/>
						<table id="listform" class="listTable">
						  <thead><tr><td width="50%">项目标签<br><br><br></td><td>值<br><br><br></td></tr></thead>
						  <tbody></tbody>
						</table>
						</div>
						
				</div>
				<input type="button" value=" 确 定 " onclick="rewriteFormElementValue(myEditFormTmp.tmpformId.value)"/>
			  </form>
			</div>			
		</div>
</div>
<div id="menuContent" class="menuContent" style="display:none; position: absolute;background-color:#ffffff;border:1px solid #617775;">
	<ul id="treeDemo" class="ztree" style="margin-top:0; width:160px;"></ul>
</body>
</html>
<script type="text/javascript">
//设计流程第二步
function nextStep2(){
var url = "${ctx}/jbpmWorkFlow/parsexml!modifyProDefStep2.action?processDeploymentId="+myform.processDeploymentId.value;
top.addTab(url,"流程设计第二步");
}

function appInit(){
//初始化页签
$("#tabsContainer").find("li")[0].click();
}

//页签切换
var preTabObj=null;
var preDivObj= null;
function changeActTab(liveobj,divId){
//alert(liveobj.className);
if(preTabObj == liveobj)return ;

if(null != preTabObj){
$(preTabObj).removeClass("active");
$(preDivObj).addClass("hideDiv");
//alert($(preDivObj).get(0).innerText);
}

$(liveobj).addClass("active");
$("#"+divId).removeClass("hideDiv");
preTabObj = liveobj;
preDivObj = $("#"+divId).get(0);

}

//添加视图显示
function addUserOrRole(){
var newElemtObj = $("<div></div>").attr("className","aimUserOrRole").bind("dblclick",function(){ removeUserOrRole($(this)) }).html("用户名称");
if(null != preDivObj) $(preDivObj).append(newElemtObj);

}

//删除视图对象 角色或用户
function removeUserOrRole(aimObj){
aimObj.remove();
}



//----------------------------------以下是表单设计-------------------------------
var targetaimOBJ = "";
var sourceOBJ = "";


//当前活动对象
var currentTagFormContainer = new Array();




function initapp(){
var defautFormObj ={id:'1',name:'2',ftitle:'',fvalue:'',ismust:'1',formtype:'',ishasitem:'0'};
//JSON.stringify();
//alert(JSON.stringify(defautFormObj));
}



$(function(){
			$('.drag').draggable({
				//proxy:'clone',
				revert:true,
				cursor:'auto',
				onStartDrag:function(){
					$(this).draggable('options').cursor='not-allowed';
					$(this).draggable('proxy').addClass('dp');
					
					sourceOBJ = $(this)[0].parentNode;
				},
				onStopDrag:function(){
					$(this).draggable('options').cursor='auto';
				}
				,
				/**/deltaX:10,
				deltaY:10,
				proxy:function(source){
					var n = $('<div class="proxy DIVcontainer"></div>');
					//n.html($(source).html()).appendTo('body');
					//alert(formContent[source.objectType]);
					n.html(formContent[source.objectType]).appendTo('body');
					return n;
				}
				
			});
			$('.target').droppable({
				//accept:'#d1,#d3',
				onDragEnter:function(e,source){
					$(source).draggable('options').cursor='auto';
					$(source).draggable('proxy').css('border','1px solid red');
					$(this).addClass('over');
					
					targetaimOBJ = $(this)[0];
				},
				onDragLeave:function(e,source){
					$(source).draggable('options').cursor='not-allowed';
					$(source).draggable('proxy').css('border','1px solid #ccc');
					$(this).removeClass('over');
				},
				onDrop:function(e,source){
				    //initTableTdContent("#act_01");
				    /*var tmpstr="";
				      for(var tmp in source){
				         tmpstr = tmpstr+tmp + ",";
				      }
				      alert(tmpstr);*/
	      
				    //alert(source.comeType);
				    //$(this).html("");
				    //source.id="ddd";
				    if(source.comeType == "newCreate"){
				      $(this).append(source);
				      //$(this).html(formContent[source.objectType]);
					  $(this).removeClass('over');
				    }else{
				    
				    //显示
				    var objhtml = source.innerHTML;
				    var newNode = createNewMoveNode(source.objectType);
				    newNode.html(formContent[source.objectType]);
				    targetaimOBJ.innerHTML ="";
					$(this).append(newNode);
					$(this).removeClass('over');
					
					//新建对象
					createFormElement(newNode.get(0).fid,source.objectType);
				    }
				    
				    
				}
			});
		});

//创建一个新可移动的div
function createNewMoveNode(objectType){
var id = '_'+getwfId();//主键

return $("<div></div>").attr("class",".drag").attr("comeType","newCreate").attr("id",id).attr("fid",id).attr("objectType",objectType).draggable({
				proxy:'clone',
				revert:true,
				cursor:'auto',
				onStartDrag:function(){
					$(this).draggable('options').cursor='not-allowed';
					$(this).draggable('proxy').addClass('dp');
				},
				onStopDrag:function(){
					$(this).draggable('options').cursor='auto';
				}
			}).bind(
			  'dblclick',
			   function (){
			     showFormInfo(this);
			   }
			);
}

//成功创建一个表单元素
function createFormElement(fid,formtype){

currentTagFormContainer[fid] = new formElementObj();
currentTagFormContainer[fid].id = fid;
currentTagFormContainer[fid].formtype = formtype;
//alert(fid);
}

//显示表单对象的值
function viewFormElement(fid){
//alert(currentTagFormContainer[fid]['id']);
//将表单对象的值赋给 展示表单
myEditFormTmp.tmpformName.value = currentTagFormContainer[fid].name ;
myEditFormTmp.tmpformDesc.value = currentTagFormContainer[fid].formtype ;
myEditFormTmp.tmpformId.value = currentTagFormContainer[fid].id ;
myEditFormTmp.tmpformDesc.value = currentTagFormContainer[fid].ftitle ;
//将表单字段的列表数组回写到展示表单中
reWriteToView(currentTagFormContainer[fid].item);


}

//重新写入表单元素的值
function rewriteFormElementValue(fid){

$('#editFormW').window('close');

currentTagFormContainer[fid].name   = myEditFormTmp.tmpformName.value;
currentTagFormContainer[fid].ftitle = myEditFormTmp.tmpformDesc.value;

var formObjListArray = getFormObjListArray('listform');
currentTagFormContainer[fid].item = formObjListArray;
//修改展示区的值
$('#'+fid).children()[0].innerText=myEditFormTmp.tmpformName.value;
}






//给表格的每个单元格初始化一个空格
function initTableTdContent(tabid){
var obj = $(tabid);
var rowArray = obj[0].rows;
for(var i = 0;i < rowArray.length;i++){
  
  var cellArray = rowArray[i].cells;  
  for(var j = 0;j < cellArray.length ;j++){
     //alert(cellArray[j].innerHTML); 
     if(cellArray[j].innerHTML ==""){
       cellArray[j].innerHTML =="&nbsp;";
     }
  }
}

}

/**
*obj 被点击对象
*/
function showFormInfo(obj){
//document.getElementById("mytxa").value=obj.outerHTML;
//alert($(obj).children()[0].innerText);//获取第一个div的内容

myEditFormTmp.reset();
$('#editFormW').window('open');

//show form edit
viewFormElement(obj.fid);
//alert(JSON.stringify(obj));
//alert(getwfId());
//alert(obj.outerHTML);

}

function doCancel(){
    
  //window.history.go("-1");
  //endHarvest();
  $("#tb_11").find("img").each(function (index, domEle) {
             domEle.click();
          }
  );
  
  
  $("#wfDesignCr").children().each(function (index, domEle) {

                         var aim = $(domEle).find("td").children();//获取单元格中的所有div(文档对象)
						 var act_formArray = new Array();
						 for(var i = 0;i < aim.length;i++){
						  act_formArray.push(currentTagFormContainer[aim[i].id]);
						 }
						 //给每个活动对应的一个表单					
						 $("#form"+domEle.id.substr(3,domEle.id.length)).val(JSON.stringify(act_formArray));
                       }
  
  
  ); 
  
  var obj = document.getElementById("wfDesignCr");
    //alert(obj.innerHTML);
    document.getElementById("mytxa").value=obj.innerHTML;
}

function doSubmit(){

 //收获表单
 $("#wfDesignCr").children().each(function (index, domEle) {

                         var aim = $(domEle).find("td").children();//获取单元格中的所有div(文档对象)
						 var act_formArray = new Array();
						 for(var i = 0;i < aim.length;i++){
						  act_formArray.push(currentTagFormContainer[aim[i].id]);
						 }
						 //给每个活动对应的一个表单
						 $("#formInfo").val(JSON.stringify(act_formArray));
                       }
  
  
  ); 
 
 myform.action= "saveDefForm2";
 myform.submit();
}

//按顺序获取表格中的表单对象
function endHarvest(){
 var aim = $("#act_01").find("td").children();//获取单元格中的所有div(文档对象)
 var act_formArray = new Array();
 //alert(aim.length);
 for(var i = 0;i < aim.length;i++){
  //alert(aim[i].id+aim[i].objectType);
  act_formArray.push(currentTagFormContainer[aim[i].id]);
 }
myform.form_act_01.value=(JSON.stringify(act_formArray));
document.getElementById("mytxa").value=myform.form_act_01.value;

}
var setting = {
			async: {
				enable: true,
				url:"showFormTypdTree",
				autoParam:["id", "name"],
				otherParam:{"otherParam":"zTreeAsyncTest","node":"0000"},
				dataFilter: filter
			},
			callback: {
				beforeAsync: beforeAsync,
				onClick: onClick
				
			}
		};
function filter(treeId, parentNode, childNodes) {
			if (!childNodes) return null;
			for (var i=0, l=childNodes.length; i<l; i++) {
				childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			}
			return childNodes;
		}
function beforeAsync(treeId, treeNode) {
			return treeNode ? treeNode.level < 5 : true;
		}

function showMenu() {
			var cityObj = $("#formTypeName");
			var cityOffset = $("#formTypeName").offset();
			$("#menuContent").css({left:cityOffset.left + "px", top:cityOffset.top + cityObj.outerHeight() + "px"}).slideDown("fast");
			$("body").bind("mousedown", onBodyDown);
			alert("showMenu");
}
function hideMenu() {
	        $("#menuContent").fadeOut("fast");
			$("body").unbind("mousedown", onBodyDown);
		}
function onBodyDown(event) {
			if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
				hideMenu();
			}
		}
		

function onClick(e, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj("treeDemo");						
			nodes = zTree.getSelectedNodes();
			v = "";
			nodes.sort(function compare(a,b){return a.id-b.id;});
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].id + ",";
			}
			if (v.length > 0 ) v = v.substring(0, v.length-1);
			var cityObj = $("#formType");
			cityObj.attr("value", v);
			$("#formTypeName").val(treeNode.name);
		}
$(document).ready(function(){
$.fn.zTree.init($("#treeDemo"), setting);
});		
</script>
