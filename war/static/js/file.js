if (!XMLHttpRequest.prototype.sendAsBinary) {

	// http://javascript0.org/wiki/Portable_sendAsBinary
	XMLHttpRequest.prototype.sendAsBinary = function(datastr) {
		function byteValue(x) {
			return x.charCodeAt(0) & 0xff;
		}
		var ords = Array.prototype.map.call(datastr, byteValue);
		var ui8a = new Uint8Array(ords);
		this.send(ui8a.buffer);
	};
}

var upload = function(files) {

	hideTipword();

	var xmlhttp = new XMLHttpRequest(), url = 'Upload', boundary = '---------fdsfwefFDSF', inputs = document
			.querySelectorAll('#settingPanels>div:FIRST-CHILD input'), dashdash = '--', crlf = '\r\n', builder = '';

	xmlhttp.open('POST', url, true);
	xmlhttp.setRequestHeader('Content-type', 'multipart/form-data; boundary='
			+ boundary);

	xmlhttp.upload.addEventListener('loadstart', loadstart, false);

	xmlhttp.addEventListener('readystatechange', function(ev) {
		if (xmlhttp.readyState == 4 & xmlhttp.status == 200) {

			var dialog = new Dialog(), resptxt = xmlhttp.responseText
					.split(',');

			if (resptxt[0] == 'err')
				console.log(resptxt[1]);
			else {
				dialog.uuid = resptxt[1];

				document.body.removeChild(document
						.getElementById('loaderImage'));

				dialog.show();
			}
		}
	}, false);

	if (inputs) {

		for ( var i = 0, input; input = inputs[i]; i++) {
			builder += dashdash + boundary + crlf;
			builder += 'content-disposition: form-data; name="' + input.name
					+ '"' + crlf + crlf;
			builder += input.value + crlf;
		}
	}

	for ( var i = 0, f; f = files[i]; i++) {

		var fr = new FileReader();
		fr.readAsBinaryString(f);
		fr.onload = (function(file, index, length) {
			return function(evt) {

				builder += /* boundary */dashdash
						+ boundary /* boundary end */
						+ crlf
						+ /* headers */'content-disposition: form-data; name="file"; filename="'
						+ file.name + '"' + crlf
						+ 'Content-Type: application/octet-stream'/* headers end */ + crlf
						+ crlf + /* data */evt.target.result
						+ crlf;

				if (index == length - 1) {

					/* Mark end of the request */
					builder += dashdash + boundary + dashdash + crlf;

					// send
					xmlhttp.sendAsBinary(builder);
				}
			};
		})(f,i,files.length);
	}
};