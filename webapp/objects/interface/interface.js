// project class
Objects.project = function (e) {
	this.autoReload = true; // auto reload on changes
	this.accessibility = false; // accessibility: support for voiceover (set title on buttons)

	var foo = this;
	
	this.pages = []; // array of pages (objInstance) for quick reference

	// events. not to be assigned externally
	this.onShow = function () {}
	this.onHide = function () {}

	// add page and set size. obj, pos (order, used in undo)
	this.addPage = function (page,pos) {
		if (pos==null)
			this.pages.push(page);
		else
			this.pages.splice(pos,0,page);
		//page.resetPageSize();
		this.e.appendChild(page.e);
	} // addPage()

	// delete page
	this.delPage = function (page) {
		for (var i=0; i<this.pages.length; i++) {
			if (this.pages[i] == page) {
				this.pages.splice(i,1);
				return; // done
			}
		}
	} // delPage()

	// sort function for pages.sort
	function pagesSortFunc(a,b) {
		if (a.obj.id == "homepage" && b.obj.id != "homepage")
			return -1;
		if (a.obj.id != "homepage" && b.obj.id == "homepage")
			return 1;
		// both the same, check name
		if (a.attrValues[0] > b.attrValues[0])
			return 1;
		if (a.attrValues[0] < b.attrValues[0])
			return -1;
		if (this.orientationMode) {
			if (a.orientation == "vertical" && b.orientation == "horizontal")
				return 1;
			if (a.orientation == "horizontal" && b.orientation == "vertical")
				return -1;
		}
		return 0;
	} // pagesSortFunc()

	// sort pages alphabetically
	this.sortPages = function () {
//		this.pages.sort(pagesSortFunc);
	} // sortPages()
	
	// update project, pages sizes, sets page orientation
	this.updateSize = function (w,h,w2,h2) {
		if (w != null) this.width = w;
		if (h != null) this.height = h;
		if (w2 != null) this.width2 = w2;
		if (h2 != null) this.height2 = h2;

		for (var i=0; i<this.pages.length; i++) {
			// reset orientation
			if (this.pages[i].obj.id != "popup") {
				if (this.pages[i].orientation != "portrait" || !this.orientationMode) {
					if (this.orientationMode)
						this.pages[i].orientation = "landscape"; // default
					else
						this.pages[i].orientation = null;
				} else {
					this.pages[i].orientation = "portrait";
				}
				// reset size
//				this.pages[i].resetPageSize();
			}
		}
	} // updateSize()
	
	// set attribute
	var _setAttribute = this.setAttribute;
	this.setAttribute = function (name, value) {
		var v;
		switch (name) {
		case "skin":
		case "title":
		case "language":
			v = value;
			break;
		case "width":
		case "height":
			v = parseInt(value);
			break;
		// kiosk
		case "kiosk":
			switch (value) {
			case "nolock":
			case "lock":
			case "strict":
				client.kiosk = value;
				break;
			default:
				client.kiosk = "";
			}
			client.updateKiosk();
			break;
		// lock
		case "lock":
			lockPage = (value != "")?value:null;
			// otherwise links are already updated on client.showPage
			if (lastPageSet == value)
				client.updateLinks()
			statusIcon.show();
			// continues to set page
		// set page
		case "page":
			// skip page attribute on first time
			if (client.firstUpdate) return;
			if (lastPageSet != value) {
				if (value != "")
					client.showPage("page:"+value,null,{lock:false});
				else
					client.pageBack();
				lastPageSet = value;
			}
			break;
		// page back
		case "pageback":
			if (userButtonPage && userButtonPage.page == value) {
				var pageDiv = document.getElementById("page-"+userButtonPage.page);
				// popup?
				for (var i=0; i<client.popups.length; i++) {
					if (client.popups[i].content == pageDiv) {
						userButtonPage = null;
						client.closePopup(client.popups[i]);
						return;
					}
				}
				// not a popup
				userButtonPage = null;
				client.pageBack();
			}
			break;
		// accessibility: support for voiceover (set status on buttons' label)
		case "accessibility":
			this.accessibility = (value!="false"?value:false);
			client.kiosk = "nolock";
			client.updateKiosk();
			break;
		// blink (only false)
		case "blink":
			if (value==false || value=="false")
				animations.stopAllBlink();
			break;
		// auto reload, default true
		case "autoreload":
			v = parseBool(value);
			this.autoReload = v;
			break;
		default:
			return _setAttribute.call(this, name, value);
		}

		return this.updateAttribute(name, v);
	} // setAttribute()
} // Objects.project class

Objects.extend("project","__base");
