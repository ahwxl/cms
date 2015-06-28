//表单对象
function formElementObj(){

this.id = '';
this.name = '';
this.ftitle = '';
this.fvalue = '';
this.ismust = '1';//1为必须
this.formtype= '';//表单类型
this.item = new Array();//列表

}
//列表对象
function FormElementValueItemObj(){
this.value = '';
this.label = '';
}

//主键
function getwfId(){
return new Date().getTime();
}

//支持的所有表单类型
var formContent =  new Array();
formContent['text']="<div class='DIVcontentleft'>文本框</div><div class='DIVcontentright'><input /></div>";
formContent['textArea']="<div class='DIVcontentleft'>文本域</div><div class='DIVcontentright'><textarea cols='15' rows='2'></textarea></div>";
formContent['radio']="<div class='DIVcontentleft'>单选按钮</div><div class='DIVcontentright'><input type=radio /></div>";
formContent['checkbox']="<div class='DIVcontentleft'>多选按钮</div><div class='DIVcontentright'><input type=checkbox /></div>";
formContent['file']="<div class='DIVcontentleft'>文件上传</div><div class='DIVcontentright'><input type=file /></div>";
formContent['select']="<div class='DIVcontentleft'>下拉列表</div><div class='DIVcontentright'><select name=''></select></div>";
/*添加table 行*/

function addrow(tableid){
$('#'+tableid+' tbody').append("<tr><td><input /></td><td><input /></td></tr>");//tr:eq(0) .insertAfter()
}
/*删除table 行*/
function removerow(tableid){
var rownum = $('#'+tableid+' tbody tr ').size();
$('#'+tableid+' tbody tr:eq('+(rownum-1)+')').remove();//删除最后一行单元格
}
/*将表单域的列表写入一个对象数组中*/
function getFormObjListArray(tableid){
var objarray = new Array();
var inpnum = 0;
var newtmpobj = null;
$('#'+tableid+' tbody tr td').each(function(index) {
  var tmp = $(this).find("input").val();
  if(inpnum ==0){
     newtmpobj = new FormElementValueItemObj();
     newtmpobj.label = tmp;
     inpnum ++;
  }else{
     newtmpobj.value = tmp;
     objarray.push(newtmpobj);
     inpnum =0;
  }
});
//alert(JSON.stringify(objarray));
return objarray;
}
/*将表单字段列表项回写*/
function reWriteToView(objArray){
if(null == objArray || objArray.length == 0){
  $('#listform tbody tr').remove();
  return null;
}
$('#listform tbody tr').remove();
for(var i =0;i<objArray.length;i++){
   $('#listform tbody').append("<tr><td><input value='"+objArray[i].label+"' /></td><td><input value='"+objArray[i].value+"'/></td></tr>");
}

}
