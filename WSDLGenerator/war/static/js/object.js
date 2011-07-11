/**
 * Author: Kevin.C
 */

function Dialog() {

	this.content = "";

	var dialog = document.createElement('div');

	dialog.className = 'dialog';

	/* dialog content element */
	var c = document.createElement('div');
	c.className = 'content';
	dialog.appendChild(c);
	/* ++ */

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
	timeoutspan.to = timeout;
	dialog.appendChild(timeoutspan);
	
	closeImage.addEventListener('click', function() {
		document.body.removeChild(dialog);
		window.location.href = '/';
	}, false);

	this.show = function() {

		c.innerHTML = this.content;
		document.body.appendChild(dialog);
		timeoutspan.to(600000);
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