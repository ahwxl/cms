<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="module/editorContent/editor.js"></script>
<script type="text/javascript" src="resources/js/ckeditor3.6.2/ckeditor.js"></script>
<style type="text/css"></style>
<title>发布文章</title>
</head>
<body>

<form action="saveContent" method="post">
		<p>
			<label for="editor1">
				添加内容:</label>
<br />
文章标题： <input name="cnt_caption" /><br />
副标题：&nbsp;&nbsp;&nbsp;&nbsp;<input name="second_caption" /><br />
选择目录：<input name="catalog_id" /><br />
			<textarea class="ckeditor" cols="80" id="editor1" name="content" rows="10">&lt;p&gt;This is some &lt;strong&gt;sample text&lt;/strong&gt;. You are using &lt;a href="http://ckeditor.com/"&gt;CKEditor&lt;/a&gt;.&lt;/p&gt;</textarea>
		</p>
		<p>
			<input type="submit" value="确定" />
		</p>
</form>
<script type="text/javascript">
CKEDITOR.replace( 'editor1',
	    {
	        filebrowserBrowseUrl : 'ckfinderPop',
	        filebrowserUploadUrl : 'uploader/upload.php',
	        filebrowserImageWindowWidth : '640',
	        filebrowserImageWindowHeight : '480'
	    });
</script>

</body>
</html>