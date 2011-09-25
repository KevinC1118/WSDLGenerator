<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
<style type="text/css">
p {
	text-indent: 30px;
	text-align: justify;
}
</style>
</head>
<body>
	<div>
		<h1>WSDL Generator Introduction</h1>
		<p>This is a WSDL Generator service. This service can only
			generate WSDL 1.1 document now. I will consider add WSDL 2.0 feature
			in the future.</p>
		<p>This WSDL Generator is compatible for top-down manner of web
			service development. If you are prefer to use bottom-up manner,
			sorry, this is not what you need. This service depend on spreadsheet
			(Microsoft Excel file). You have to design your web service part that
			include your service name, input and output message. Utilize our
			upload function and we will generator WSDL and Schema files to you.</p>
		<h3>Usage:</h3>
		<h4>Step 1:</h4>
		<p>Setting 『address location』, a.k.a SOAP address location, in
			Setting tab page(at right sign). If the common usage of soap address
			is http://localhost:8080/WebService/service/{service name}, you
			should fill in "http://localhost:8080/WebService/service/" Then fill
			in Targetnamespace. If your Targetnamespace format is
			http://test.com.tw/{Service name}, you should fill in
			"http://test.com.tw/" If you do not want to fill in these field again
			at next time, you can check "save" option.</p>
		<h4>Step 2:</h4>
		<p>Upload excel files. There are two manner to do it. First one is
			click anywhere and it will open file selection dialog. Second one is
			drag and drop. Finish file selection, click uploading image, it will
			start to upload file.</p>
		<h4>Step 3:</h4>
		<p>Finally, you can see a dialog and there is a download link
			inside it. You will get a zip file that include all WSDL and Schema
			files.</p>
		<div style="font-size: small;">If there any usage problem, email to <a href="mailto:korprulu9@gmail.com">here</a></div>
	</div>
</body>