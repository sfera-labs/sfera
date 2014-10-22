// text class
Objects.text = function () {
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
		case "text":
			v = value;
			this.e.innerHTML = v;
			/* ??????????????????????
			var d = e.childNodes[0];
			e.removeChild(d);
			d.innerHTML = value;
			e.appendChild(d);
			*/
			break;
		case "add":
			this.e.innerHTML += value;
			break;
		case "size":
			v = parseInt(value);
			this.e.style.fontSize = value+"px";
			break;
		case "color":
			v = value;
			this.e.style.color = value;
			break;
		case "fontstyle":
			v = value;
			this.e.style.fontStyle = value;
			break;
		case "weight":
		case "fontweight":
			v = value;
			this.e.style.fontWeight = value;
			break;
		case "align":
			v = value;
			this.e.style.textAlign = value;
			break;
		default:
			return _setAttribute.call(this, name, value);
		}

		return this.updateAttribute(name, v);
	} // setAttribute()
} // Objects.text class

Objects.extend("text","__base","__pos");