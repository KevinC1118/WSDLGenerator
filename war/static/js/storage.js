var SettingStorage = {

	_record : window.localStorage.getItem('_wsdlgenerator_34fadfa_') ? JSON
			.parse(window.localStorage.getItem('_wsdlgenerator_34fadfa_'))
			: new Object,

	$menu : document.getElementById('settingRecord'),
	$inputbox : document.getElementById('saveName'),
	$saveButton : document.getElementById('saveButton'),
	$deleteButton : document.getElementById('deleteButton'),

	$addressLocation : document.getElementById('addressLocation'),
	$targetnamespace : document.getElementById('targetnamespace'),
	$snPosition : document.getElementById('snPosition'),
	$levelIndex : document.getElementById('levelIndex'),
	$keyIndex : document.getElementById('keyIndex'),
	$typeIndex : document.getElementById('typeIndex'),

	init : function() {

		for ( var v in SettingStorage._record)
			SettingStorage.menuAdd(v, v);

		SettingStorage.$menu.addEventListener('change', function(evt) {
			(evt.target.value != 'null') ? SettingStorage
					.selection(evt.target.value) : null;
		}, false);

		SettingStorage.$saveButton.addEventListener('click', function(evt) {

			var v = {
				addressLocation : SettingStorage.$addressLocation.value,
				keyIndex : SettingStorage.$keyIndex.value,
				levelIndex : SettingStorage.$levelIndex.value,
				snPosition : SettingStorage.$snPosition.value,
				targetnamespace : SettingStorage.$targetnamespace.value,
				typeIndex : SettingStorage.$typeIndex.value
			};

			var n = SettingStorage.$inputbox.value;
			SettingStorage.recordAdd(n, v);
			SettingStorage.menuAdd(n, n);

		}, false);

		SettingStorage.$deleteButton.addEventListener('click', function(evt) {

			if (confirm('Delete ' + SettingStorage.$menu.value + '?')) {
				SettingStorage.recordDelete(SettingStorage.$menu.value);
				SettingStorage.menuDelete();
			}
			
		}, false);
	},

	menuAdd : function(name, value) {
		SettingStorage.$menu.add(new Option(name, value), null);
	},

	menuDelete : function() {
		SettingStorage.$menu.remove(SettingStorage.$menu.selectedIndex);
	},

	recordAdd : function(name, value) {
		SettingStorage._record[name] = value;
		window.localStorage.setItem('_wsdlgenerator_34fadfa_', JSON
				.stringify(SettingStorage._record));
	},

	recordDelete : function(name) {
		if (SettingStorage._record[name])
			delete SettingStorage._record[name];
		window.localStorage.setItem('_wsdlgenerator_34fadfa_', JSON
				.stringify(SettingStorage._record));
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

function showOrHideInputbox(checkbox/* checkbox */, obj) {

	if (obj instanceof Array) {
		checkbox.checked ? obj.forEach(function(e, i, l) {
			e.style.visibility = 'visible';
		}) : obj.forEach(function(e, i, l) {
			e.style.visibility = 'hidden';
		});
	} else
		(checkbox.checked) ? obj.style.visibility = 'visible'
				: obj.style.visibility = 'hidden';

}