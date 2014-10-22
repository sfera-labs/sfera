// interface class
Objects["interface"] = function (e, id) {
	this.e = e;
	this.id = id;
	this.type = "";

	this.autoReload = true; // auto reload on changes
	this.accessibility = false; // accessibility: support for voiceover (set title on buttons)

	this.pages = []; // array of pages (objInstance) for quick reference
	this.includes = {}; // include containers
	this.includesNeeded = []; // list of needed includes, to load

	var foo = this;

	// init, called by client.initObjss after assigning type
	this.init = function () {
	}

	// events. not to be assigned externally
	this.onShow = function () {}
	this.onHide = function () {}
	
	// updates with current dictionary data
	this.selectDictionary = function () {
		if (dictionary.size) {
			if (!this.width) this.width = parseInt(dictionary.size[0]);
			if (!this.height) this.height = parseInt(dictionary.size[1]);
		}
	} // selectDictionary()

	// add page and set size. obj, pos (order, used in undo)
	this.addPage = function (page,pos) {
		if (pos==null)
			this.pages.push(page);
		else
			this.pages.splice(pos,0,page);
		//page.resetPageSize();
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
	this.pagesSortFunc = function (a,b) {
		if (a.obj.id == "menu" && b.obj.id != "menu")
			return -1;
		if (a.obj.id != "menu" && b.obj.id == "menu")
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
		this.pages.sort(this.pagesSortFunc);
	} // sortPages()

	// is compact?
	this.isPageCompact = function (w,h) {
		return (w<=500);
	} // isPageCompact()

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
				//this.pages[i].resetPageSize();
			}
		}
	} // updateSize()

	// add include. object (to redraw)
	this.addInclude = function (obj) {
		// already loaded?
		if (this.includes[obj.attrValues[0]]) {
			obj.redraw(); // redraw now
			return;
		}

		for (var i=0; i<this.includesNeeded.length; i++) {
			if (this.includesNeeded[i].file == obj.attrValues[0]) { // already to be loaded?
				this.includesNeeded[i].objs.push(obj);
				return;
			}
		}

		project.includesNeeded.push({file:obj.attrValues[0], objs:[obj]});

		return true;
	} // addInclude()

	// reset all objects
	this.reset = function () {
		for (var i=0; i<this.pages.length; i++) {
			for (var k=0; k<this.pages[i].objs.length; k++) {
				this.pages[i].objs[k].reset();
			}
		}
	} // reset all

	// get all objects in the project. parent object
	this.getAllObjs = function (p) {
		var a = [];
		var r;
		var i;
		// no parent, all pages
		if (!p) {
			for (i=0; i<project.pages.length; i++) {
				r = this.getAllObjs(project.pages[i]);
				if (r) a = a.concat(r);
			}
		} else {
			a.push(p);
			if (p.objs && p.objs.length) {
				for (i=0; i<p.objs.length; i++) {
					r = this.getAllObjs(p.objs[i]);
					if (r) a = a.concat(r);
				}
			}
		}
		return a;
	} // getAllObjs()

	// to JSON
	this.toJSON = function () {
		/*
		var s = { language: this.language,
				  header: this.header,
				  orientationMode: this.orientationMode,
				  width: this.width,
				  height: this.height,
				  deviceImage: this.deviceImage,
				  scale: this.scale,
				  style: this.style,
				  uiset: this.uiset,
				  cameraList: this.cameraList,
				  cameraGridList: this.cameraGridList,
				  cameraOverlay: this.cameraOverlay };
		return s;
		*/
	} // toJSON()

	// from JSON. return all the attribute ids that changed
	this.fromJSON = function (o) {
		/*
		var c = []; // what changed. indexes of the attributes
		var k = 0;
		for (var i in s) {
			if (this[i]!=s[i]) {
				c.push((k<2)?k:(k<5)?2:k-2);
				this[i] = s[i];
			}
			k++;
		}
		return c.unique();
		*/
	} // fromJSON()

	// set attribute
	this.setAttribute = function (attr, value) {
		switch (attr) {
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
			this.autoReload = (value != "false" && value != false);
			break;
		}
	} // setAttribute()
} // Objects.text class