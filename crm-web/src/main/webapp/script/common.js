function post(action, params)
{
	var f = document.createElement('form');
	f.action = action;
	f.method = 'POST';
	document.body.appendChild(f);
	
	for (var p in params)
	{
		var i = document.createElement('input');
		i.type = 'hidden';
		i.name = p;
		i.value = params[p];
		f.appendChild(i);
	}
	
	f.submit();
}

var ConfirmDialogMode = Prototype.Browser.IE ? "mdlg" : "pwc";
//var ConfirmDialogMode = "pwc"; // prototype window class
//var ConfirmDialogMode = "mdlg"; // custom modal dialog class
//var ConfirmDialogMode = "std"; // standard javascript confirm

function confirmPost(message, action, params) {
	if (ConfirmDialogMode == "pwc") {
	    var options = {
	        width: 200,
	        okLabel: "Yes",
	        cancelLabel: "No",
	        className: "alphacube",
	        onOk: function(win) {
	            post(action, params);
	            return true;
	        }
	    };
	    Dialog.confirm(message, options);
	}
	else if (ConfirmDialogMode == "mdlg") {
	    var data = { action: action, params: params };
	    ModalDialog.show(message, ['Yes', 'No'], _confirmPostCallback, data);
	}
	else {
    	var result = confirm(message) ? 'Yes' : 'No';
    	_confirmPostCallback(result, data);
	}
}

function _confirmPostCallback(result, data) {
    if (result == 'Yes') {
        post(data.action, data.params);
    }
}

function deleteObject(objectName, id, source, sourceObject, sourceObjectId) {
    var message = format(DELETE_OBJECT_MESSAGE, objectName);
    var action = objectName + '.edit';
    var params = {
    	id: id,
    	source: source || '',
    	source_object: sourceObject || '',
    	source_object_id: sourceObjectId || '',
    	__delete: true
    };
    confirmPost(message, action, params);
}

function format(str) {
    for (var i = 1; i < arguments.length; i++) {
        str = str.replace('{' + (i-1) + '}', arguments[i]);
    }
    return str;
}

var DELETE_OBJECT_MESSAGE = 'Are you sure you want to permanently delete this {0}?';
