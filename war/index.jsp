<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/static/js/json2.js" charset=""></script>
<script type="text/javascript" src="/static/js/main.js" charset="UTF-8"></script>
<script type="text/javascript" src="/static/js/obj.js"
	charset="UTF-8"></script>
<script type="text/javascript" src="/static/js/file.js" charset="UTF-8"></script>
<link rel="stylesheet" type="text/css" href="/static/css/main.css" />
<title>WSDL Generator</title>
</head>
<body>
	<div id="buttomLayer">
		<div id="tipword">You can drag and drop file into here, or click and
			select</div>
	</div>
	<input type="file" id="file" name="file" multiple="multiple">
	<ul id="tagList">
		<li id="settingTag"><div></div></li>
		<li id="gdTag"><div></div></li>
	</ul>
	<div id="settingPanels">
		<!-- settingTag -->
		<jsp:include page="WEB-INF/pages/settingTag.jsp"></jsp:include>
		<!-- gdTag -->
		<jsp:include page="WEB-INF/pages/gdTag.jsp"></jsp:include>
		<img alt="close" src="/static/images/close.gif"
			onclick="closePanels()"
			style="position: absolute; bottom: 10px; left: 10px; cursor: pointer;">
		<script type="text/javascript" src="/static/js/storage.js"></script>
	</div>
</body>
</html>