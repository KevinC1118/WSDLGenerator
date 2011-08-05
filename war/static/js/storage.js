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

		SettingStorage._record = {
			a : 'abc',
			d : 'cde'
		};

		for ( var v in SettingStorage._record) {
			SettingStorage.menuAdd(v, v);
		}

		SettingStorage.$menu.addEventListener('change', function(evt) {
			(evt.target.value != 'null') ? SettingStorage
					.selection(evt.target.value) : null;
		}, false);
	},

	menuAdd : function(name, value) {
		var option = new Option(name, value), div = document
				.createElement('div'), img = new Image();

		div
				.setAttribute('style',
						'line-height:0;display:inline-block;width:100%;padding:2px;text-align:right;');

		img.src = 'static/images/delete.gif';
		img.setAttribute('style', 'position:relative;right:5px;');

//		img.addEventListener('click', function(evt){
//			console.log(evt);
//		}, false);
		
		option.addEventListener('click', function(evt) {
			evt.preventDefault();
			console.log(evt);
		}, false);

		option.appendChild(div).appendChild(img);
		SettingStorage.$menu.add(option, null);
	},

	recordAdd : function(name, value) {
		SettingStorage._record[name] = value;
	},

	recordDelete : function(name) {
		if (SettingStorage._record[name])
			delete SettingStorage._record[name];
	},

	selection : function(name) {

		var rec = SettingStorage._record[name];

		SettingStorage.$addressLocation.value = rec.addressLocation ? rec.addressLocation
				: null;
		SettingStorage.$keyIndex.value = rec.keyIndex ? rec.keyIndex : null;
		SettingStorage.$levelIndex.value = rec.levelIndex ? rec.levelIndex
				: null;
		SettingStorage.$snPosition.value = rec.snPosition ? rec.snPosition
				: null;
		SettingStorage.$targetnamespace.value = rec.targetnamespace ? rec.targetnamespace
				: null;
		SettingStorage.$typeIndex.value = rec.typeIndex ? rec.typeIndex : null;
	}
};
SettingStorage.init();