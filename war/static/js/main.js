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