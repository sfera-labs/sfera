// link class
Objects.link = function () {
	this.popups = {}; // can have associated popups

	this.link = ""; // link (page or url)

	// custom onClick event (won't open the target page)
	this.onClick = null;

	var foo = this;

	var _init = this.init;
	this.init = function (e, obj) {
		_init.call(this, e, obj);
		
		//browser.initButtonEvents(e,{onclick:onClick});
	}

	function onClick() {
		if (foo.onClick) {
			foo.onClick();
		} else {
			client.focus();
			client.showPage(foo.link,foo);
		}
	}

	// events. not to be assigned externally
	this.onShow = function () {} // do nothing on show
	this.onHide = function () {
		browser.onButtonEvent(null,this.e,"mouseout");
		// close all non modal popups, those are closed by showPage
		for (var id in this.popups) if (this.popups[id].popupType != "modal") {
			client.closePopup(this.popups[id]); // so popups don't float, we close them
		}
	}

	// set attribute
	var _setAttribute = this.setAttribute;
	this.setAttribute = function (name, value) {
		var v;
		switch (name) {
		case "width":
		case "height":
			v = parseInt(value);
			this.e.style[name] = v+"px";
			var span = this.e.getElementsByTagName("span")[0];
			span.style[name] = v+"px";
			break;
		case "link":
			v = value;
			if (v) {
				if (v.search(/(http|ftp|https):\/\//) == 0 || // full url
					v.indexOf("/") == 0 ||
					v.indexOf("../") == 0) { // relative url
					this.link = "url:"+v;
				} else if (this.type=="admin") { // admin page
					this.link = "page:admin"+v;
				} else if (v.indexOf("code:") == 0) { // javascript code
					this.onClick = new Function("event","e",v.substr(5)); // remove code:
				} else {
					this.link = "page:"+v;
				}
			}
			this.onClick = null;
			break;
		case "onclick":
			v = value;
			this.onClick = new Function(v);
			break;
		default:
			return _setAttribute.call(this, name, value);
		}

		return this.updateAttribute(name, v);
	} // setAttribute()
} // Objects.link class

Objects.extend("link","__base","__pos","__button");