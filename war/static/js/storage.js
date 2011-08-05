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
		SettingStorage.$menu.add(new Option(name, value), null);
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
				: '';
		SettingStorage.$keyIndex.value = rec.keyIndex ? rec.keyIndex : '';
		SettingStorage.$levelIndex.value = rec.levelIndex ? rec.levelIndex : '';
		SettingStorage.$snPosition.value = rec.snPosition ? rec.snPosition : '';
		SettingStorage.$targetnamespace.value = rec.targetnamespace ? rec.targetnamespace
				: '';
		SettingStorage.$typeIndex.value = rec.typeIndex ? rec.typeIndex : '';
	}
};
SettingStorage.init();