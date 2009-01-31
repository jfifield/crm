/*-----------------------------------------------------------------------------------------------*/
Event.observe(window, 'load', _mdlg_initialize, false);

var ModalDialog = Class.create();

ModalDialog.show = function(message, buttons, callback, data)
{
	var mdlgDiv = document.getElementById('_mdlg');
	if (!mdlgDiv)
	{
	    var body = document.getElementsByTagName('body')[0];
		var mdlgDiv = document.createElement('div');
		mdlgDiv.id = '_mdlg';
		Element.addClassName(mdlgDiv, 'mdlg');
		body.appendChild(mdlgDiv);
		
		var messageDiv = document.createElement('div');
		messageDiv.id = '_mdlg_message';
		Element.addClassName(messageDiv, 'message');
		mdlgDiv.appendChild(messageDiv);

		var buttonsDiv = document.createElement('div');
		buttonsDiv.id = '_mdlg_buttons';
		Element.addClassName(buttonsDiv, 'footer');
		mdlgDiv.appendChild(buttonsDiv);
	}

	var messageDiv = document.getElementById('_mdlg_message');
	var buttonsDiv = document.getElementById('_mdlg_buttons');
	
	messageDiv.innerHTML = message;
	
	var html = '';
    for (i = 0; i < buttons.length; i++) {
    	html += '<button class="button" id="ModalDialog:' + buttons[i] + '">' + buttons[i] + '</button>&nbsp;'
    }
	
	buttonsDiv.innerHTML = html;
	
	var dlg = new ModalDialog(mdlgDiv, callback, data);
	dlg.show();
}

ModalDialog.prototype = {

	yOffset: 0,

    initialize: function(element, callback, data) {
        this.element = element;
        this.callback = callback;
        this.data = data;
    },
    
    show: function() {
		if (Prototype.Browser.IE) {
			this._getYOffset();
			this._prepareIE('100%', 'hidden');
			this._scroll(0, 0);
			this._toggleSelects('hidden');
		}
		this._displayLightbox('block');
		this._registerObservers();
    },
    
    hide: function() {
		this._unregisterObservers();
		this._displayLightbox('none');
		if (Prototype.Browser.IE) {
			this._toggleSelects('visible');
			this._prepareIE('auto', 'auto');
			this._scroll(0, this.yOffset);
			this._getYOffset();
		}
    },
    
	_getYOffset: function(){
		if (self.pageYOffset) {
			this.yOffset = self.pageYOffset;
		} else if (document.documentElement && document.documentElement.scrollTop){
			this.yOffset = document.documentElement.scrollTop; 
		} else if (document.body) {
			this.yOffset = document.body.scrollTop;
		}
	},

	_prepareIE: function(height, overflow) {
		var body = document.getElementsByTagName('body')[0];
		body.style.height = height;
		body.style.overflow = overflow;
  
		var html = document.getElementsByTagName('html')[0];
		html.style.height = height;
		html.style.overflow = overflow; 
	},

	_scroll: function(x, y) {
		window.scrollTo(x, y);
	},

	_toggleSelects: function(visibility) {
		var selects = document.getElementsByTagName('select');
		for(i = 0; i < selects.length; i++) {
			selects[i].style.visibility = visibility;
		}
	},

	_displayLightbox: function(display) {
		$('_mdlg_overlay').style.display = display;
		$(this.element).style.display = display;
	},

    _fireCallback: function(result) {
        this.hide();
        this.callback(result, this.data);
    },
    
	_registerObservers: function() {
        this.observers = new Array();
        var elements = $(this.element).getElementsByTagName('*');
        for(i = 0; i < elements.length; i++) {
            var element = elements[i];
            if (element.id.startsWith('ModalDialog:')) {
                var result = element.id.substring(12);
                var observer = { element: element, eventName: 'click', handler: this._fireCallback.bind(this, result) };
                this.observers[this.observers.length] = observer;
                Event.observe(observer.element, observer.eventName, observer.handler);
            }
        }
    },
    
	_unregisterObservers: function() {
        if (this.observers) {
            for (i = 0; i < this.observers.length; i++) {
                var observer = this.observers[i];
                Event.stopObserving(observer.element, observer.eventName, observer.handler);
            }
        }
    }

}

/*-----------------------------------------------------------------------------------------------*/
function _mdlg_initialize() {
    var body = document.getElementsByTagName('body')[0];
	var overlay = document.createElement('div');
	overlay.id = '_mdlg_overlay';
	body.appendChild(overlay);
}
