if (!Array.prototype.forEach) {

	Array.prototype.forEach = function(callbackfn, thisArg) {

		var T, O = Object(this), len = O.length >>> 0, k = 0;

		if (!callbackfn || !callbackfn.call) {
			throw new TypeError();
		}

		if (thisArg) {
			T = thisArg;
		}

		while (k < len) {

			var Pk = String(k), kPresent = O.hasOwnProperty(Pk), kValue;

			if (kPresent) {
				kValue = O[Pk];

				callbackfn.call(T, kValue, k, O);
			}

			k++;
		}
	};
}

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

var showPanels = function() {
	
	var tagList = document.querySelector('#tagList');
	if(tagList.style.right) return;
	
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

function showTooltip(evt) {

	if (!evt.target.parentNode.querySelector('.unprovided')) {
		var unp = new Image(72);
		unp.className = 'unprovided';
		unp.src = '/static/images/unprovided.png';
		unp.onload = function() {
			var op = function() {

				if (unp) {

					if (unp.style.opacity < 1) {

						if (unp.style.opacity)
							unp.style.opacity = parseFloat(unp.style.opacity) + 0.1;
						else
							unp.style.opacity = '0.1';
					} else
						stop();

				}
			};
			var it = setInterval(op, 5);
			function stop() {
				clearInterval(it);
			}
		};

		this.parentNode.appendChild(unp);
	}
}

function hideTooltip(evt) {

	var p = evt.target.parentNode;
	var img = p.querySelector('.unprovided');
	p.removeChild(img);
}

window.onload = function() {

	var buttomLayer = document.getElementById('buttomLayer'), fileInput = document
			.getElementById('file');

	// Cancel dragover
	buttomLayer.addEventListener('dragover', cancel, false);

	buttomLayer.addEventListener('click', function(evt) {
		fileInput.click();
	}, false);

	buttomLayer.addEventListener('drop', createFileObj, false);
	fileInput.addEventListener('change', createFileObj, false);

	var tags = document.querySelectorAll('#tagList>li');
	for ( var i = 0, tag; (tag = tags[i]); i++) {
		tag.addEventListener('click', (function(index) {
			return function(evt) {
				showPanels();
				evt.target.index = index;
				showPanel.call(evt.target);
			};
		})(i), false);
	}

	document.getElementsByName('addressLocation')[0].onblur = function(evt) {

		var value = this.value;

		var pattern = /^http:\/\/.+\/$/ig;

		if (!pattern.test(value)) {
			// TODO
		}
	};

	[ document.getElementsByName('snPosition')[0],
			document.getElementsByName('levelIndex')[0],
			document.getElementsByName('keyIndex')[0],
			document.getElementsByName('typeIndex')[0],
			document.getElementById('save') ].forEach(function(element, index,
			array) {
		element.onmouseover = showTooltip;
		element.onmouseout = hideTooltip;
	});
};