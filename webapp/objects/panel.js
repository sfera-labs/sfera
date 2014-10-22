// panel
Objects.panel = function () {
	// set attribute
	var _setAttribute = this.setAttribute;
	this.setAttribute = function (name, value) {
		var v;
		switch (name) {
		case "width":
		case "height":
			v = parseInt(value);
			this.e.style[name] = v+"px";
			break;
		// color
		case "style":
			v = value;
			var p = e.className.split(" ");
			p[1] = v;
			e.className = p.join(" ");
			break;
		default:
			return _setAttribute.call(this, name, value);
		}

		return this.updateAttribute(name, v);
	} // setAttribute()
} // Objects.panel class

Objects.extend("panel","__base","__pos");