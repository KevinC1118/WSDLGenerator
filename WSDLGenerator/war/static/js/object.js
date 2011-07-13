/**
 * Author: Kevin.C
 */

function Dialog() {

	this.uuid = null;

	var dialog = document.createElement('div');

	dialog.className = 'dialog';

	/* dialog content element */
	var c = document.createElement('div');
	c.className = 'content';
	dialog.appendChild(c);
	/* ++ */

	var link = document.createElement('a');
	c.appendChild(link);

	var downloadImage = new Image();
	downloadImage.src = '/static/images/Download.png';
	link.appendChild(downloadImage);

	var downloadspan = document.createElement('span');
	downloadspan.innerHTML = 'Download';
	downloadspan.style.display = 'block';
	link.appendChild(downloadspan);

	/* close image button */
	var closeImage = new Image();
	closeImage.src = '/static/images/close.gif';
	closeImage.style.position = 'absolute';
	closeImage.style.top = '5px';
	closeImage.style.right = '5px';
	closeImage.style.cursor = 'pointer';
	dialog.appendChild(closeImage);

	var timeoutspan = document.createElement('span');
	timeoutspan.className = 'timeout';
	dialog.appendChild(timeoutspan);

	closeImage.addEventListener('click', function() {
		document.body.removeChild(dialog);
		window.location.href = '/';
	}, false);

	this.show = function() {

		link.href = '/Download?' + this.uuid;
		document.body.appendChild(dialog);
		timeout(600000);
	};
}

function FileObj(map) {

	var fileObj = document.createElement('div');
	fileObj.className = 'fileObj';
	fileObj.file = map.file;

	var deleteImg = new Image();
	deleteImg.src = '/static/images/delete.gif';
	fileObj.appendChild(deleteImg);

	var docImage = new Image(96, 96);
	docImage.src = '/static/images/Document.png';
	fileObj.appendChild(docImage);

	deleteImg.addEventListener('click', function() {
		var p = fileObj.parentNode;
		p.parentNode.removeChild(p);
	}, false);

	var fileName = document.createElement('span');
	fileName.appendChild(document.createTextNode(fileObj.file.name));

	fileObj.appendChild(fileName);

	return fileObj;
}

var UploadButton = function() {

	var img = new Image();
	img.src = '/static/images/upload_file.png';
	img.id = 'uploadButton';
	img.style.cursor = 'pointer';

	img.addEventListener('click', function() {

		var fileObjs = document.querySelectorAll('#fileList .fileObj');

		var files = new Array();

		for ( var i = 0, max = fileObjs.length; i < max; i++)
			files.push(fileObjs[i].file);

		upload(files);
	}, false);

	return img;
};

function timeout(time) {

	var obj = document.querySelector('.dialog .timeout');

	obj.innerHTML = Math.floor(time / 60000) + ' : ' + (function() {
		var t = (time % 60000 / 1000).toString();
		return (t.length == 1) ? '0' + t : t;
	})();

	(parseInt(time) > 0) ? setTimeout('timeout(' + parseInt(time - 1000) + ')',
			1000) : obj.innerHTML = 'Sorry, It\'s timeout';
}