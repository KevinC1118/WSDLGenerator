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

			var dialog = new DownloadDialog(), resptxt = JSON
					.parse(xmlhttp.responseText);

			dialog.uuid = resptxt.ID;

			document.body.removeChild(document.getElementById('loaderImage'));

			dialog.show();

			if (resptxt.ERROR) {
				var errorDialog = new ErrorMsgDialog;
				errorDialog.content = resptxt.ERROR;
				errorDialog.show();
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
						+ 'Content-Type: application/octet-stream' + crlf
						+ crlf + /* data */evt.target.result + crlf;

				if (index == length - 1) {

					/* Mark end of the request */
					builder += dashdash + boundary + dashdash + crlf;

					// send
					xmlhttp.sendAsBinary(builder);
				}
			};
		})(f, i, files.length);
	}
};

function loadstart(ev) {

	var mask = document.createElement('div'), div = document
			.createElement('div'), percent = document.createElement('div'), loaderImage = new Image();

	mask.id = 'mask';
	mask.className = 'mask';

	loaderImage.src = '/static/images/ajax-loader.gif';
	loaderImage.style.verticalAlign = 'middle';
	loaderImage.style.display = 'inline-block';

	div.id = 'loaderImage';

	percent.id = 'percent';
	percent.style.display = 'block';
	percent.style.marginTop = '5px';
	percent.style.fontSize = '0.9em';
	percent.innerHTML = 'Loading...';

	document.body.appendChild(mask);

	div.appendChild(loaderImage);
	div.appendChild(percent);
	document.body.appendChild(div);
}

function hideTipword() {
	document.getElementById('tipword').style.visibility = 'hidden';
}

function cancel(e) {
	if (e.preventDefault)
		e.preventDefault(); // required by FF + Safari
	e.dataTransfer.dropEffect = 'copy'; // tells the browser what drop effect is
	// allowed here
	return false; // required by IE
}

var createFileObj = function(evt) {

	var files;

	if (evt.type == 'change')
		files = evt.currentTarget.files;
	else /* if (evt.type == 'drop') */{
		cancel(evt);
		files = evt.dataTransfer.files;
	}

	var ol;

	if (document.querySelector('#fileList'))
		ol = document.querySelector('#fileList');
	else {
		ol = document.createElement('ol');
		ol.id = 'fileList';
		document.body.appendChild(ol);
	}

	for ( var i = 0, max = files.length; i < max; i++) {

		var li = document.createElement("li"), fo = new FileObj({
			file : files[i],
		});

		li.appendChild(fo);
		ol.appendChild(li);
	}

	if (!document.querySelector('#uploadButton'))
		document.body.appendChild(new UploadButton());
};