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
	
	closeImage.addEventListener('click', function(){
		document.body.removeChild(dialog);
		window.location.href = '/';
	}, false);

	this.show = function() {
		
		c.innerHTML = this.content;
		document.body.appendChild(dialog);
	};
}

var FileObj = {
		
		parent: null,
		
		fileObj: function() {
			
			var fo = document.createElement('div');
			fo.className = 'fileObj';
			
			return fo;
		},
		
		show: function() {
			
			if(!this.parent) {
				console.log("No Setting parent");
				this.parent = document.body;
			}
			
			
		}
};