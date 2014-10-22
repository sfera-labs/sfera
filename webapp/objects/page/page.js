// page class
Objects.page = function () {
	var gestures = {}; // default gestures, null same as "off"
	
	// init
	var _init = this.init;
	this.init = function (e, obj) {
		_init.call(this, e, obj);
		
		if (this.getAttribute("title") == null) this.setAttribute("title","");
		if (this.getAttribute("orientation") != null) client.rotationEnabled = true;

		if (this.getAttribute("gestures") != null)
			setGestures(this.getAttribute("gestures"));

		//pinPages["page-"+this.id] = prot; <------------ todo
	}

	// set/get gestures
	function setGestures(g) {
		// reset
		gestures = {}; // default values in Gestures
		// parse
		g = g.trim();
		if (g) {
			var a = g.split(";");
			var b;
			for (var i=0; i<a.length; i++) {
				b = a[i].split("=");
				if (b[0].trim()) {
					gestures[b[0].trim()] = (b[1]?b[1].trim():"");
				}
			}
		}
	}
	this.getGestures = function () {
		return gestures;
	}

	this.getOrientation = function () {
		return this.getAttribute("orientation");
	}

	this.getScope = function () {
		return ""; //scope;
	}
	
	var _addObject = this.addObject;
	this.addObject = function (o,pos) {
		_addObject.call(this, o, pos);
		
	}
	// show/hide
	var _show = this.show;
	this.show = function () {
		// before show?
		if (this.onBeforeShow)
			this.onBeforeShow();

		// delegate events
		this.onShow();
		_show.call(this);
		//this.onAfterShow(); ???
		client.focus();

		// show title
		client.setPageTitle(this.getAttribute("title"));

		// after show? (init the page? ex. btsave on camerapanel)
		if (this.onAfterShow)
			this.onAfterShow();
	}
	var _hide = this.hide;
	this.hide = function () {
		// before hide?
		if (this.onBeforeHide)
			this.onBeforeHide();

		// delegate events
		this.onHide.call(this);
		_hide.call(this);

		// after hide? (ex. pages opened by user buttons)
		if (this.onAfterHide)
			this.onAfterHide();
	}
	
	// set attribute
	var _setAttribute = this.setAttribute;
	this.setAttribute = function (name, value) {
		var v;
		switch (name) {
		case "title":
		case "prot":
		case "orientation":
		case "scope":
			v = value;
			break;
		case "gestures":
			v = value;
			setGestures(v);
			break;
		default:
			return _setAttribute.call(this, name, value);
		}

		return this.updateAttribute(name, v);
	} // setAttribute()

} // Objects.page class

Objects.extend("page","__base","__container","__page");