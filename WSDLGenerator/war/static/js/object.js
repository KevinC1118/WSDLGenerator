/**
 * Author: Kevin.C
 */

function Dialog() {

	this.content = "";

	var dialog = document.createElement('div');

	dialog.className = 'dialog';
//	dialog.style.top = (document.documentElement.clientHeight - 250) / 2 + 'px';
//	dialog.style.left = (document.documentElement.clientWidth - 500) / 2 + 'px';

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

	function close(callback) {

		if (callback) callback();

		document.body.removeChild(dialog);
	}

	this.open = function(closeCallback) {
		
		c.innerHTML = this.content;
		
		closeImage.addEventListener('click', function() {
			close(closeCallback);
		}, false);
		
		document.body.appendChild(dialog);
	};
}

var fileWindow = function(params) {
	/*
	 * file : file object
	 *  
	 */
	
	if(!params) Error('No paramters');
	
};