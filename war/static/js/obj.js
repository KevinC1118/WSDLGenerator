/**
 * Author: KevinC
 */

var Dialog = function() {

	this.content = null;

	this.dialog = document.createElement('div');
	d = this.dialog;

	d.className = 'dialog';

	/* dialog content element */
	this.contentdiv = document.createElement('div');
	c = this.contentdiv;

	c.className = 'content';
	d.appendChild(c);
	/* ++ */

	/* close image button */
	var closeImage = new Image();
	closeImage.src = '/static/images/close.gif';
	closeImage.style.position = 'absolute';
	closeImage.style.top = '5px';
	closeImage.style.right = '5px';
	closeImage.style.cursor = 'pointer';
	d.appendChild(closeImage);

	closeImage.addEventListener('click', (function(n) {
		return function() {
			document.body.removeChild(n);
		};
	})(d), false);

	Dialog.prototype.show = function() {

		c.innerHTML = this.content;
		document.body.appendChild(d);
	};
};

function DownloadDialog() {
	// Call the parent constructor

	this.uuid = null;

	var link = document.createElement('a');
	this.contentdiv.appendChild(link);

	var downloadImage = new Image();
	downloadImage.src = '/static/images/Download.png';
	link.appendChild(downloadImage);

	var downloadspan = document.createElement('span');
	downloadspan.innerHTML = 'Download';
	downloadspan.style.display = 'block';
	link.appendChild(downloadspan);

	var timeoutspan = document.createElement('span');
	timeoutspan.className = 'timeout';
	this.dialog.appendChild(timeoutspan);

	DownloadDialog.prototype.show = function() {

		link.href = '/Download?' + this.uuid;
		document.body.appendChild(this.dialog);
		var time = 300000;

		changeTimeoutspan();
		var t = setInterval(function() {
			changeTimeoutspan();
		}, 1000);

		function changeTimeoutspan() {
			timeoutspan.innerHTML = Math.floor(time / 60000) + ' : '
					+ (function() {
						var t = (time % 60000 / 1000).toString();
						return (t.length == 1) ? '0' + t : t;
					})();
			time -= 1000;
			(time < 0) ? (function() {
				clearTimeout(t);
				timeoutspan.innerHTML = 'Sorry, It\'s timeout';
			})() : null;
		}

	};

};
// inherit Dialog
DownloadDialog.prototype = new Dialog;
DownloadDialog.prototype.constructor = DownloadDialog;

function ErrorMsgDialog() {

	this.contentdiv.style.margin = '30px 2px 2px 2px';

	var ta = document.createElement('textarea');
	ta.setAttribute('readonly', 'readonly');
	ta.setAttribute('wrap', 'off');
	ta.style.fontSize = '0.7em';
	ta.style.minHeight = '165px';
	ta.style.minWidth = '343px';
	ta.style.maxHeight = '165px';
	ta.style.maxWidth = '343px';
	ta.style.overflow = 'auto';
	ta.style.backgroundColor = 'white';
	ta.style.border = 'none';
	ta.style.lineHeight = '1.5em';
	ta.style.resize = 'none';

	this.contentdiv.appendChild(ta);

	ErrorMsgDialog.prototype.show = function() {

		if (this.content instanceof Array) {

			this.dialog.className += ' errorMsgDialog';

			this.content.forEach(function(e, i, l) {
				this.value += e + '\n';
			}, ta);
			document.body.appendChild(this.dialog);
		}
	};
}
ErrorMsgDialog.prototype = new Dialog;
ErrorMsgDialog.prototype.constructor = ErrorMsgDialog;

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

var SettingStorage = {

	_record : window.localStorage.getItem('_wsdlgenerator_') ? JSON
			.parse(window.localStorage.getItem('_wsdlgenerator_')) : new Object,

	$menu : document.getElementById('settingRecord'),
	$addressLocation : document.getElementById('addressLocation'),
	$targetnamespace : document.getElementById('targetnamespace'),
	$snPosition : document.getElementById('snPosition'),
	$levelIndex : document.getElementById('levelIndex'),
	$keyIndex : document.getElementById('keyIndex'),
	$typeIndex : document.getElementById('typeIndex'),

	init : function() {

		for(var v in this._record) {
			this.menuAdd(v, this._record[v]);
		}

		this.$menu.addEventListener('change', function(evt) {
			selection(this._record[evt.target.value]);
		}, false);
	},
	
	menuAdd : function(name, value) {
		var option = document.createElement('option');
		option.setAttribute('value', value);
		option.innerHTML = name;
		this.$menu.appendChild(option);
	},

	recordAdd : function(rec) {},
	
	recordDelete : function(rec) {},
	
	selection : function(rec) {

		this.$addressLocation.value = rec.addressLocation;
		this.$keyIndex.value = rec.keyIndex;
		this.$levelIndex.value = rec.levelIndex;
		this.$snPosition.value = rec.snPosition;
		this.$targetnamespace.value = rec.targetnamespace;
		this.$typeIndex.value = rec.typeIndex;
	}
};
SettingStorage.init();