function loadstart(ev) {

	var mask = document.createElement('div'), div = document
			.createElement('div'), percent = document.createElement('div'), loaderImage = new Image();

	mask.id = 'mask';
	mask.className = 'mask';

	loaderImage.src = '/static/images/ajax-loader.gif';
	loaderImage.style.verticalAlign = 'middle';
	loaderImage.style.display = 'inline-block';
	// loaderImage.style.top = (document.documentElement.clientHeight -
	// loaderImage.height)
	// / 2 + 'px';
	// loaderImage.style.left = (document.documentElement.clientWidth -
	// loaderImage.width)
	// / 2 + 'px';

	div.id = 'loaderImage';
	div.className = 'loaderImage';

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

function build(files, boundary) {

	var dashdash = '--', crlf = '\r\n', builder = '';

	builder += 'content-disposition: form-data';
	builder += crlf;
	builder += 'Content-Type: application/octet-stream, boundary=' + boundary;
	builder += crlf;

	var len = files.length;
	for ( var i = 0; i < len; i++) {

		builder += dashdash;
		builder += boundary;
		builder += crlf;

		var file = files[i];
		/* Generate headers. */
		builder += 'Content-Disposition: form-data; name="file"';
		if (file.fileName) {
			builder += '; filename="' + file.fileName + '"';
		}
		builder += crlf;

		builder += 'Content-Type: application/octet-stream';
		builder += crlf;
		builder += crlf;

		/* Append binary data. */
		builder += file.getAsBinary();
		builder += crlf;

	}

	/* Mark end of the request. */
	builder += dashdash;
	builder += boundary;
	builder += dashdash;
	builder += crlf;

	return builder;
}

function timeout(time) {

	var obj = document.querySelector('.dialog .timeout');

	obj.innerHTML = Math.floor(time / 60000) + ' : ' + (time % 60000) / 1000;

	(parseInt(time) > 0) ? setTimeout('timeout(' + parseInt(time - 1000) + ')',
			1000) : obj.innerHTML = 'Sorry, It\'s timeout';
}

var upload = function(files) {

	hideTipword();

	var xmlhttp = new XMLHttpRequest(), url = 'Upload', boundary = 'fdsfwefFDSF';

	xmlhttp.open('POST', url, true);
	xmlhttp.setRequestHeader('Content-type', 'multipart/form-data; boundary='
			+ boundary);

	xmlhttp.upload.addEventListener('loadstart', loadstart, false);

	// xmlhttp.upload.addEventListener('load', function(ev) {
	// progress(100, progress_inner, total_width);
	// }, false);

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

	xmlhttp.sendAsBinary(build(files, boundary));
};

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

window.onload = (function(e) {

	var buttomLayer = document.getElementById('buttomLayer'), fileInput = document
			.getElementById('file');

	// Cancel dragover
	buttomLayer.addEventListener('dragover', cancel, false);
	// buttomLayer.addEventListener('dragenter', cancel, false);

	buttomLayer.addEventListener('click', function(evt) {
		fileInput.click();
		evt.stopPropagation();
		evt.preventDefault();
	}, false);

	// Do upload
	// buttomLayer.addEventListener('drop', upload, false);
	// fileInput.addEventListener('change', upload, false);

	buttomLayer.addEventListener('drop', createFileObj, false);
	fileInput.addEventListener('change', createFileObj, false);

});