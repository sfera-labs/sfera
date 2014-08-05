// text class
Objects.text = function (e, id) {
	this.e = e;
	this.id = id;
	this.type = "";

	var _base = null; // base delegate

	var foo = this;

	// init, called by client.initObjss after assigning type
	this.init = function () {
		_base = new Objects._base(this); // setup delegate
		if (e.style.display == "none") // can be set as style
			this.visible = false;
	}

	// events. not to be assigned externally
	this.onShow = function () {}
	this.onHide = function () {}

	// set attribute
	this.setAttribute = function (attr, value) {
		switch (attr) {
		case "width":
			e.style.width = value+"px";
			break;
		case "text":
		case "value":
			e.innerHTML = value;
			/* ??????????????????????
			var d = e.childNodes[0];
			e.removeChild(d);
			d.innerHTML = value;
			e.appendChild(d);
			*/
			break;
		case "add":
			e.innerHTML += value;
			break;
		case "size":
			e.style.fontSize = value+"px";
			break;
		case "color":
			e.style.color = value;
			break;
		case "fontstyle":
			e.style.fontStyle = value;
			break;
		case "weight":
		case "fontweight":
			e.style.fontWeight = value;
			break;
		case "align":
			e.style.textAlign = value;
			break;
		default:
			_base.setAttribute(attr,value);
		}
	} // setAttribute()
} // Objects.text class