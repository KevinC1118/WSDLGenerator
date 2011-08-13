<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div>
	<span>Setting Record</span>
	<div style="vertical-align: middle;">
		<select id="settingRecord">
			<option value="null">Chose...</option>
		</select> <img id="deleteButton" alt="Delete Record"
			src="/static/images/delete.gif" title="Delete saved record"
			style="cursor: pointer;">
	</div>
	<span>Address Location</span>
	<div>
		<input id="addressLocation" type="text" value="http://wsdlgenerator/"
			size="27" pattern="^http:\/\/.+\/$">
	</div>
	<span>TargetNamespace prefix</span>
	<div>
		<input id="targetnamespace" type="text" value="http://wsdlgenerator/"
			size="27" pattern="^http:\/\/.+\/$">
	</div>
	<span>Cell of service name</span>
	<div>
		<input id="snPosition" type="text" size="4" value="B2"
			title="For example: A1, B2, C3">
	</div>
	<div>
		<span>Save</span> <input type="checkbox" id="save" name="save"
			onchange="showOrHideInputbox(this, [document.getElementById('saveName'), document.getElementById('saveButton')]);">
		<input type="text" id="saveName" size="5" style="visibility: hidden;">
		<input type="button" id="saveButton" value="Save"
			style="visibility: hidden;">
	</div>
</div>