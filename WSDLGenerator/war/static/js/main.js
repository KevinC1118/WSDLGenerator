function loadstart(ev) {

	var mask = document.createElement('div'), div = document
			.createElement('div'), percent = document.createElement('div'), loaderImage = new Image();

	mask.id = 'mask';
	mask.className = 'mask';

	loaderImage.src = '/static/images/ajax-loader.gif';
	loaderImage.style.verticalAlign = 'middle';
	loaderImage.style.display = 'inline-block';

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

var upload = function(files) {

	hideTipword();

	var xmlhttp = new XMLHttpRequest(), url = 'Upload', boundary = 'fdsfwefFDSF';

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

var showPanels = function(evt) {

	evt.preventDefault();
	evt.stopPropagation();

	var tagList = document.querySelector('#tagList');
	var panel = document.querySelector('#settingPanels');

	tagList.style.right = '300px';
	panel.style.width = '300px';
	panel.style.overflow = 'auto';
};

var closePanels = function(evt) {

	var tagList = document.querySelector('#tagList');
	var panel = document.querySelector('#settingPanels');

	tagList.style.right = '';
	panel.style.width = '';
	panel.style.overflow = '';
};

var showPanel = function() {

	var panels = document.querySelectorAll('#settingPanels > div');

	for ( var i = 0, max = panels.length; i < max; i++) {
		(i == parseInt(this.index)) ? panels[i].style.display = 'block'
				: panels[i].style.display = 'none';
	}
};

window.onload = (function() {

	var buttomLayer = document.getElementById('buttomLayer'), fileInput = document
			.getElementById('file');

	// Cancel dragover
	buttomLayer.addEventListener('dragover', cancel, false);

	buttomLayer.addEventListener('click', function(evt) {
		fileInput.click();
		evt.stopPropagation();
		evt.preventDefault();
	}, false);

	buttomLayer.addEventListener('drop', createFileObj, false);
	fileInput.addEventListener('change', createFileObj, false);

	var tags = document.querySelectorAll('#tagList>li');
	for ( var i = 0, max = tags.length; i < max; i++) {
		tags[i].addEventListener('mouseover', showPanels, false);
		tags[i].addEventListener('click', (function() {
			tags[i].index = i;
			return showPanel;
		})(), false);
	}
});