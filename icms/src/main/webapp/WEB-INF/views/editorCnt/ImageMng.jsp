<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/newcms/resources/js/ckfinder2.1/ckfinder.js?20180426"></script>
<style type="text/css"></style>
<title>图片管理</title>
</head>
<body>
	<p style="padding-left: 10px; padding-right: 10px;">
		<script type="text/javascript">

			// This is a sample function which is called when a file is selected in CKFinder.
			function showFileInfo( fileUrl, data )
			{
				var msg = 'The selected URL is: <a href="' + fileUrl + '">' + fileUrl + '</a><br /><br />';
				// Display additional information available in the "data" object.
				// For example, the size of a file (in KB) is available in the data["fileSize"] variable.
				if ( fileUrl != data['fileUrl'] )
					msg += '<b>File url:</b> ' + data['fileUrl'] + '<br />';
				msg += '<b>File size:</b> ' + data['fileSize'] + 'KB<br />';
				msg += '<b>Last modified:</b> ' + data['fileDate'];

				// this = CKFinderAPI object
				this.openMsgDialog( "Selected file", msg );
				//parent.window.returnValue= data['fileUrl'];
			}

			// You can use the "CKFinder" class to render CKFinder in a page:
			var finder = new CKFinder();
			// The path for the installation of CKFinder (default = "/ckfinder/").
			finder.basePath = '../';
			// The default height is 400.
			finder.height = 600;
			finder.width ='100%';
			// This is a sample function which is called when a file is selected in CKFinder.
			finder.selectActionFunction = showFileInfo;
			finder.create();

			// It can also be done in a single line, calling the "static"
			// create( basePath, width, height, selectActionFunction ) function:
			// CKFinder.create( '../', null, null, showFileInfo );

			// The "create" function can also accept an object as the only argument.
			// CKFinder.create( { basePath : '../', selectActionFunction : showFileInfo } );

		</script>
	</p>
</body>
</html>