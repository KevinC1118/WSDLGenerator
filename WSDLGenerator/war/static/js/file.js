function build(boundary, files, fields) {

	var dashdash = '--', crlf = '\r\n', builder = '';

	// builder += 'content-disposition: form-data';
	// builder += crlf;
	// builder += 'Content-Type: multipart/form-data; boundary=' + boundary;
	// builder += crlf;

	if (fields) {

		for ( var i = 0, max = fields.length; i < max; i++) {
			builder += dashdash + boundary + crlf;
			builder += 'content-disposition: form-data; name="'
					+ fields[i].name + '"' + crlf + crlf;
			builder += fields[i].value + crlf;
		}
	}

	var len = files.length;
	for ( var i = 0; i < len; i++) {

		builder += dashdash + boundary + crlf;

		var file = files[i];
		/* Generate headers. */
		builder += 'content-disposition: form-data; name="file"; ';
		if (file.fileName) {
			builder += 'filename="' + file.fileName + '"';
		}
		builder += crlf;

		builder += 'Content-Type: application/octet-stream' + crlf + crlf;

		/* Append binary data. */
		builder += file.getAsBinary() + crlf;
	}

	/* Mark end of the request. */
	builder += dashdash + boundary + dashdash + crlf;

	return builder;
}

var upload = function(files) {

	hideTipword();

	var xmlhttp = new XMLHttpRequest(), url = 'Upload', boundary = 'fdsfwefFDSF', inputs = document
			.querySelectorAll('#settingPanels>div:FIRST-CHILD input');

	xmlhttp.open('POST', url, true);
	xmlhttp.setRequestHeader('Content-type', 'multipart/form-data; boundary='
			+ boundary);

	xmlhttp.upload.addEventListener('loadstart', loadstart, false);

	xmlhttp.addEventListener('readystatechange', function(ev) {
		if (xmlhttp.readyState == 4 & xmlhttp.status == 200) {

			var dialog = new Dialog(), resptxt = xmlhttp.responseText
					.split(',');

			dialog.uuid = resptxt[1];

			document.body.removeChild(document.getElementById('loaderImage'));

			dialog.show();
		}
		// showDialog(xmlhttp.responseText.split(','));
	}, false);

	xmlhttp.sendAsBinary(build(boundary, files, inputs));
};