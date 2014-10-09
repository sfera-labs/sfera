// page class
Objects.page = function (e, id) {
	this.e = e;
	this.id = id;
	this.type = "";

	var title = e.getAttribute("data-title");
	var prot = e.getAttribute("data-protected"); // pin/puk
	var orientation = e.getAttribute("data-orientation");
	var gestures = {}; // default gestures, null same as "off"
	var scope = e.getAttribute("data-scope");

	// on before/after show/hide events, externally assigned
	this.onBeforeShow = null;
	this.onAfterShow = null;
	this.onBeforeHide = null;
	this.onAfterHide = null;

	var _container = null; // container delegate
	var _page = null; // page delegate

	// init
	this.init = function () {
		_container = new Objects._container(this); // setup delegate
		_page = new Objects._page(this); // setup delegate

		if (title == null) title = "";
		if (orientation) client.rotationEnabled = true;

		if (gestures)
			setGestures(e.getAttribute("data-gestures"));

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
		return orientation;
	}

	this.getScope = function () {
		return scope;
	}

	// show/hide
	this.show = function () {
		// before show?
		if (this.onBeforeShow)
			this.onBeforeShow();

		// delegate events
		_container.onShow();
		_page.show();
		_container.onAfterShow();
		client.focus();

		// show title
		client.setPageTitle(title);

		// after show? (init the page? ex. btsave on camerapanel)
		if (this.onAfterShow)
			this.onAfterShow();
	}
	this.hide = function () {
		// before hide?
		if (this.onBeforeHide)
			this.onBeforeHide();

		// delegate events
		_container.onHide();
		_page.hide();

		// after hide? (ex. pages opened by user buttons)
		if (this.onAfterHide)
			this.onAfterHide();
	}

	// set attribute
	this.setAttribute = function (attr, value) {
		// general attributes (for every kind of object)
		switch (attr) {
		case "gestures":
			setGestures(value);
			break;
		}
	} // setAttribute()

} // Objects.page class