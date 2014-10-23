/************************************************************
 * SFERA WEBAPP, version 0.0
 *  (c) 2010-2014 Home Systems Consulting S.p.A.
 *
 *  For information, see the Home Systems Consulting web site:
 *  http://www.homesystemsconsulting.com/
 *
*************************************************************/


//--------------------------------------------------------------------------------------------------------------------------
// Common Code -------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------

String.prototype.trim = function () {
    //return this.replace(/^\s*/, "").replace(/\s*$/, "");
	var	str = this.replace(/^\s\s*/, ''),
		ws = /\s/,
		i = str.length;
	while (ws.test(str.charAt(--i)));
	return str.slice(0, i + 1);
}
if (typeof String.prototype.startsWith != "function") {
	String.prototype.startsWith = function (str){
		return this.slice(0, str.length) == str;
	};
}
if (typeof String.prototype.endsWith != "function") {
	String.prototype.endsWith = function (str){
		return this.slice(-str.length) == str;
	};
}
Number.prototype.mod = function (n) { // useful for ciclying 0-->n
	return ((this%n)+n)%n;
}
Number.prototype.next = function (b) { // n: base
	return (this+1).mod(b);
}
Number.prototype.previous = function (b) {
	return (this-1).mod(b);
}
// array is array is array.
if (!Array.prototype.isArray) {
	Array.prototype.isArray = function (vArg) {
	    return Object.prototype.toString.call(vArg) === "[object Array]";
	}
}
// return an array with unique elements
Array.prototype.unique = function () {
	var a = [];
	var l = this.length;
	for (var i=0; i<l; i++) {
	  for (var j=i+1; j<l; j++) {
	    if (this[i] === this[j])
	      j = ++i; //skip
	  }
	  a.push(this[i]);
	}
	return a;
};
// get last element
Array.prototype.last = function () {
	if (!this.length) return null;
	return this[this.length-1];
}
// clone array
Array.prototype.clone = function () {
	return this.slice(0);
}
// intersection of two arrays
Array.prototype.intersect = function (arr) {
	var a = [];
	var l = this.length;
	for (var i=0; i<l; i++) {
		if (arr.indexOf(this[i]) != -1) {
			a.push(this[i]);
		}
	}
	return a;
}
// same
Array.prototype.same = function (arr) {
	if (this.length != arr.length) return false;
	for (var i=0; i<this.length; i++) {
		if (this[i] != null && arr[i] != null && // not null
			typeof this[i] === "object" && typeof arr[i] === "object" && // both objs
			this[i].isArray && arr[i].isArray) { // arrays
			if (!this[i].same(arr[i]))
				return false;
		} else if (this[i] != arr[i]) {
			return false;
		}
	}
	return true;
}
// search
if (!Array.prototype.indexOf) {
  Array.prototype.indexOf = function (searchElement, fromIndex) {
    "use strict";
    if (this == null) {
      throw new TypeError();
    }
    var t = Object(this);
    var len = t.length >>> 0;

    if (len === 0) {
      return -1;
    }
    var n = 0;
    if (arguments.length > 1) {
      n = Number(arguments[1]);
      if (n != n) { // shortcut for verifying if it's NaN
        n = 0;
      } else if (n != 0 && n != Infinity && n != -Infinity) {
        n = (n > 0 || -1) * Math.floor(Math.abs(n));
      }
    }
    if (n >= len) {
      return -1;
    }
    var k = n >= 0 ? n : Math.max(len - Math.abs(n), 0);
    for (; k < len; k++) {
      if (k in t && t[k] === searchElement) {
        return k;
      }
    }
    return -1;
  }
}

//Production steps of ECMA-262, Edition 5, 15.4.4.18
//Reference: http://es5.github.io/#x15.4.4.18
if (!Array.prototype.forEach) {

Array.prototype.forEach = function(callback, thisArg) {

 var T, k;

 if (this == null) {
   throw new TypeError(' this is null or not defined');
 }

 // 1. Let O be the result of calling ToObject passing the |this| value as the argument.
 var O = Object(this);

 // 2. Let lenValue be the result of calling the Get internal method of O with the argument "length".
 // 3. Let len be ToUint32(lenValue).
 var len = O.length >>> 0;

 // 4. If IsCallable(callback) is false, throw a TypeError exception.
 // See: http://es5.github.com/#x9.11
 if (typeof callback !== "function") {
   throw new TypeError(callback + ' is not a function');
 }

 // 5. If thisArg was supplied, let T be thisArg; else let T be undefined.
 if (arguments.length > 1) {
   T = thisArg;
 }

 // 6. Let k be 0
 k = 0;

 // 7. Repeat, while k < len
 while (k < len) {

   var kValue;

   // a. Let Pk be ToString(k).
   //   This is implicit for LHS operands of the in operator
   // b. Let kPresent be the result of calling the HasProperty internal method of O with argument Pk.
   //   This step can be combined with c
   // c. If kPresent is true, then
   if (k in O) {

     // i. Let kValue be the result of calling the Get internal method of O with argument Pk.
     kValue = O[k];

     // ii. Call the Call internal method of callback with T as the this value and
     // argument list containing kValue, k, and O.
     callback.call(T, kValue, k, O);
   }
   // d. Increase k by 1.
   k++;
 }
 // 8. return undefined
};
}

// remove empty elements
Array.prototype.clean = function (deleteValue) {
	for (var i = 0; i < this.length; i++) {
		if (this[i] == deleteValue) {         
			this.splice(i, 1);
			i--;
		}
	}
	return this;
}

// add event cross browser
window.addEvent = function (event, target, method) {
	if (target.addEventListener) {
		target.addEventListener(event, method, false);
	} else if (target.attachEvent) {
		target.attachEvent("on" + event, method);
	} else {
		target["on" + event] = method;
	}
}
window.removeEvent = function (event, target, method) {
	if (target.removeEventListener) {
		target.removeEventListener(event, method, false);
	} else if (target.attachEvent) {
		target.detachEvent("on" + event, method);
	} else {
		target["on" + event] = null;
	}
}

// JSON
var JSON = JSON || {};
JSON.parse = JSON.parse || function (str) {
	if (str === "") str = '""';
	eval("var p=" + str + ";");
	return p;
};



var parser; // parser object
var files; // files object
var cInterface; // current interface
var dictionary; // interface dictionary, objects, interface attributes..
var client;
var manager = null; // null, so Parser knows we're on client

var config = {}; // configuration parameters, interface name, skin name..
var urls = {
	get:function (name) {
		switch (name) {
		case "dictionary":  return "/"+config.interf+"/dictionary.xml";
		case "index" :      return "/"+config.interf+"/index.xml";
		case "subscribe" :  return "/x/subscribe?"+(config.clientId?config.clientId+"&":"")+"nodes=*";
		case "state" :      return "/x/status/"+config.clientId+"?";
		}
	}
};

function Client() {
	// general purpose request
	var req = new XMLReq("GET");
	req.onLoaded = onReqLoaded;
	req.onError = onReqError;
	
	var started = false; // connected: ongoing state requests 
	
	var cSync = ""; // currently cSync?
	
	var self = this;
	
	// local timestamps, to check required updates
	this.localTs = {
		"dictionary":-1,
		"index":-1,
		"subscribe":-1,
		"status":-1
	};
	
	this.remoteTs = {
		"dictionary":-1,
		"index":-1,
		"subscribe":-1,
		"status":-1
	};
	
	// get current timestamp
	function getTimestamp() {
		return (new Date()).getTime();
	}
	
	// init
	this.init = function () {
		parser = new Parser();
		
		this.sync();
	}; // init()
	
	// sync, if necessary
	this.sync = function () {
		// force status?
		this.localTs.status = -1;
		
		for (var s in this.localTs) {
			if (this.localTs[s] == -1 || this.localTs[s] < this.remoteTs[s]) {
				cSync = s;
				req.open(urls.get(s),20);
				break;
			}
		}
		/*
		// dictionary?
		if (this.localTs.dictionary == -1 || this.localTs.dictionary < this.remoteTs.dictionary) {
			cSync = "dictionary";
			req.open(urls.get("dictionary"));
		}
		// index
		else if (this.localTs.index == -1 || this.localTs.index < this.remoteTs.index) {
			cSync = "index";
			req.open(urls.get("index"));
		}
		// status
		else {
			cSync = "state";
			req.open(urls.get("state"));
		}
		*/
	};
	
	function onReqLoaded() {
		console.log(cSync+" loaded");
		var e = document.getElementById("output");
		e.innerHTML += cSync+" loaded:<br><textarea style='width:500px; height:200px'>"+req.getResponseText()+"</textarea><br><br>";
		self.localTs[cSync] = getTimestamp();
		
		var status;
		
		switch (cSync) {
		case "dictionary":
			console.log("creating dictionary");
			dictionary = new Dictionary(req.getResponseXML());
			break;
		case "index":
			console.log("parsing interface");
			parser.parseInterface(req.getResponseXML(), {cInterface:cInterface});
			self.showPage();
			break;
		case "subscribe":
			status = JSON.parse(req.getResponseText());
			break;
		case "status":
			status = JSON.parse(req.getResponseText());
			break;
		}
		
		self.sync();
	}
	function onReqError() {
		console.log("error");
		var e = document.getElementById("output");
		e.innerHTML += "<br><br>Error.<br><br>";
	}
	
	this.showPage = function (page) {
		var e = document.getElementById("pagescontainer");
		e.appendChild(cInterface.pages[0].e);
		cInterface.pages[0].e.style.display = "inline";
	}
	
} // Client()

window.onload = function () {
	client = new Client();
	client.init();
};


//--------------------------------------------------------------------------------------------------------------------------
// Animations --------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------

// Animations Class
function Animations() {
	var step = 50; // animation step msec

	var anims = {}; // animation data
	var cAnimation = 0; // current animation id

	var foo = this;

	// options: waitTime, fadeInTime, idleTime, fadeOutTime (msec time), fadeFrom, fadeTo (opacity), onFadeIn, onFadedIn, onFadeOut, onFadedOut (events)
	this.fadeAnimation = function(e, options) {
		if (!options) return; // ?
		cAnimation++;
		var a = options; // get all options and add
		a.e = e;
		a.type = "fade";
		a.id = cAnimation;
		a.timeout = null;
		a.s = 0; // status: 0 init (and wait), 1 fadeIn, 2 idle, 3 fadeOut
		a.c = 0; // current step. for status 1,3
		a.t = 0; // total steps for current status
		if (a.fadeFrom == null)
			a.fadeFrom = 0;
		if (a.fadeTo == null)
			a.fadeTo = 100;
		if (a.fadeFrom == a.fadeTo) { // shouldn't happen
			setOpacity(a.fadeFrom);
			return null;
		}
		// there's already a fade animation on this object?
		for (var i in anims) {
			if (anims[i] && anims[i].e == e) {
				if (anims[i].type == "fade")
					a.cv = anims[i].v; // save current value, so we fade from there
				this.removeAnimation(anims[i]);
			}
		}
		anims["a"+cAnimation] = a;
		onAnimationUpdate(a);
		return a;
	} // fadeInOut()

	// size animation
	this.sizeAnimation = function(e, f, time, w, h) {
		if (!options) return; // ?
		cAnimation++;
		var a = { e:e, type:"size", id:cAnimation, timeout:null, a:time, w:w, h:h, ow:e.offsetWidth, oh:e.offsetHeight };
		a.s = 0; // status: 0 init, 1 resizing
		a.t = 0; // current steps
		// there's already a size animation on this object?
		for (var i in anims) {
			if (anims[i] && anims[i].e == e) {
				this.removeAnimation(anims[i]);
			}
		}
		anims["a"+cAnimation] = a;
		onAnimationUpdate(a);
		return a;
	} // sizeAnimation()

	// remove animation
	this.removeAnimation = function(a) {
		// interval?
		if (a.timeout) {
			clearTimeout(a.timeout);
			a.timeout = null;
		}
		anims[a.id] = null;
		delete anims[a.id];
	} // removeAnimation()

	//
	function callAnimationUpdateOnTimeout(a,msec) {
		a.timeout = setTimeout(function(){onAnimationUpdate(a)},msec);
	} // callAnimationUpdateOnTimeout()

	//  on animation update
	function onAnimationUpdate(a) {
		//var a = anims["a"+id];
		if (!a) return; // error
		// interval?
		if (a.timeout) {
			clearTimeout(a.timeout);
			a.timeout = null;
		}
		// still there?
		if (!a.e) {
			foo.removeAnimation(a);
			return;
		}
		// animation
		switch (a.type) {
		case "fade":
			switch (a.s) {
			case 0: // init, wait
				a.s = 1;
				if (a.onFadeIn) // event
					a.onFadeIn();
				if (a.fadeInTime) { // fadeInTime
					a.t = Math.floor(a.fadeInTime/step); // total steps
					if (a.cv != null) {
						a.v = a.cv; // current value
						a.cv = null; // so we don't do it again on fadeOut
						a.c = Math.round(a.t*((a.v-a.fadeFrom)/(a.fadeTo-a.fadeFrom))); // calc current step, so we fade from there
					} else {
						a.v = a.fadeFrom; // current value
					}
					setOpacity(a.e,a.v);
					a.e.style.display = "inline";
					callAnimationUpdateOnTimeout(a,a.waitTime?a.waitTime:step);
					break;
				}
				// next state, don't break
			case 1: // fading in
				// done?
				if (!a.fadeInTime || a.c == a.t) {
					a.v = a.cv?a.cv:a.fadeTo; // current value
					setOpacity(a.e,a.v);
					a.s = 2;
					if (a.onFadedIn) // event
						a.onFadedIn();
					if (a.idleTime) {
						callAnimationUpdateOnTimeout(a,a.idleTime);
						break;
					} // else next state, don't break
				} else {
					a.c++;
					a.v = a.fadeFrom+((a.fadeTo-a.fadeFrom)*a.c/a.t); // current value
					setOpacity(a.e,a.v);
					callAnimationUpdateOnTimeout(a,step);
					break;
				}
			case 2: // idle
				if (!a.fadeOutTime) {
					foo.removeAnimation(a); // all done
					break;
				} else {
					a.s = 3;
					a.c = 0;
					a.t = Math.floor(a.fadeOutTime/step); // total steps
					if (a.cv != null) {
						a.v = a.cv; // current value
						a.c = Math.round(a.t*((a.fadeTo-a.v)/(a.fadeTo-a.fadeFrom))); // calc current step, so we fade from there
					}
					if (a.onFadeOut) // event
						a.onFadeOut();
					// next state, don't break
				}
			case 3: // fading out
				// done?
				if (a.c == a.t) {
					a.v = a.fadeFrom; // current value
					setOpacity(a.e,a.v);
					a.e.style.display = "none"; // optimize
					foo.removeAnimation(a); // all done
					if (a.onFadedOut) // event
						a.onFadedOut();
				} else {
					a.c++;
					a.v = a.fadeTo-((a.fadeTo-a.fadeFrom)*a.c/a.t); // current value
					setOpacity(a.e,a.v);
					callAnimationUpdateOnTimeout(a,step);
				}
				break;
			}
			break;
		case "size":
			switch (a.s) {
			case 0: // init
				a.s = 1;
				a.t = Math.floor(a.a/step); // total steps
			}

		}
	} // onAnimationUpdate()

	// set element opacity
	function setOpacity(obj,opacity) {
		opacity = (opacity == 100)?99.999:opacity; // why?
		obj.style.filter = "alpha(opacity:"+opacity+")"; // IE/Win
		obj.style.KHTMLOpacity = opacity/100; // Safari<1.2, Konqueror
		obj.style.MozOpacity = opacity/100; // Older Mozilla and Firefox
		obj.style.opacity = opacity/100; // Safari 1.2, newer Firefox and Mozilla, CSS3
	} // setOpacity()

	//--------------------------------------------------------------------------------------------------------------------------------
	// Blinker -----------------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------------------------

	var blinkInterval = null;
	var blinkE = []; // elements that blink
	var blinkC = 0; // counter 0->3

	this.getBlinks = function () {
		return blinkE;
	}

	// check if the element is not null and currently inside the DOM
	function isInDOMTree(node) {
		if (!node) return false;
		// find ultimate ancestor
		while(node.parentNode)
			node = node.parentNode;
		return !!(node.body);
	}

	// set blinking. mode: true|slow; fast; false;. r:reserved for interface objects, stopAll doesn't stop these
	this.setBlink = function (e,mode,r) {
		// already there?
		for (var i=0; i<blinkE.length; i++) {
			if (blinkE[i].e == e) {
				updateElementBlink(e,null);
				blinkE.splice(i,1);
				break;
			}
		}
		// add?
		if (mode && mode != "false") {
			blinkE.push({e:e, f:(mode == "fast"), r:r});
		}
		// start or clear
		if (blinkE.length && !blinkInterval) {
			blinkInterval = setInterval(updateBlink,250);
		} else if (!blinkE.length && blinkInterval) {
			clearInterval(blinkInterval);
			blinkInterval = null;
		}
	} // setBlink()

	// stop all
	this.stopAllBlink = function () {
		for (var i=0; i<blinkE.length; i++) {
			// stop all not reserved
			if (!blinkE[i].r) {
				updateElementBlink(blinkE[i].e,null); // null resets it
				blinkE.splice(i,1);
				i--;
			}
		}
		if (!blinkE.length && blinkInterval) {
			clearInterval(blinkInterval);
			blinkInterval = null;
		}
	} // stopAllBlink()

	// update blinking
	function updateBlink() {
		blinkC = blinkC.next(4);
		for (var i=0; i<blinkE.length; i++) {
			// not in DOM anymore?
			if (!isInDOMTree(blinkE[i].e)) {
				updateElementBlink(blinkE[i].e,null); // null resets it
				blinkE.splice(i,1);
				i--;
			}
			else if (blinkE[i].f)
				updateElementBlink(blinkE[i].e, (blinkC%2==0));
			else
				updateElementBlink(blinkE[i].e, (blinkC<2));
		}
		// if we removed stuff
		if (!blinkE.length && blinkInterval) {
			clearInterval(blinkInterval);
			blinkInterval = null;
		}
	} // updateBlink()

	// set blink on or off
	function updateElementBlink(e,b) {
		if (!e) return; // element no longer exists
		browser.setOpacity(e, (b==null)?null:(b?1:0.3));
		//if (browser.browser == "Safari" && !browser.iOS) // prevents weird artifacts on Safari
		//	e.style.webkitTransform = (b!=null)?"translateZ(0)":"";
	} // updateElementBlink()
} // Animation Class


//--------------------------------------------------------------------------------------------------------------------------
// Browser -----------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------
function Browser() {
	// devices
	this.browser = "";
	this.version = "";
	this.OS = "";

	this.iPad = false;
	this.iOS = false;

	// browser
	this.iPhoneReload = false;
	this.buttonFeedbackEnabled = true;
	this.cameraStreamEnabled = false;
	this.cameraStreamFixedSize = false;
	this.touchEventsSupported = false;
	this.rgbaSupport = false;

	this.overEnabled = true; // mouse over effect enabled?

	this.skipButtonEvents = false; // won't fire any button events (ex. while we're dragging)

	this.scrollBarSize = 0; // browser's scrollbar size

	var dataBrowser = [{ str: navigator.userAgent, substr: "Chrome",  name: "Chrome" },
	                   { str: navigator.userAgent, substr: "CriOS",  name: "Chrome" }, // Chrome on iOS
	                   { str: navigator.userAgent, substr: "OmniWeb", ver: "OmniWeb/", name: "OmniWeb" },
	                   { str: navigator.vendor, substr: "Apple", name: "Safari", ver: "Version" },
	                   { prop: window.opera, name: "Opera"},
	                   { str: navigator.vendor, substr: "iCab", name: "iCab" },
	                   { str: navigator.vendor, substr: "KDE", name: "Konqueror" },
	                   { str: navigator.userAgent, substr: "Firefox", name: "Firefox" },
	                   { str: navigator.vendor, substr: "Camino", name: "Camino" },
	                   { str: navigator.userAgent, substr: "Netscape", name: "Netscape" }, // for newer Netscapes (6+)
	                   { str: navigator.userAgent, substr: "MSIE", name: "Explorer", ver: "MSIE" },
	                   { str: navigator.userAgent, substr: "Gecko", name: "Mozilla", ver: "rv" },
	                   { str: navigator.userAgent, substr: "Mozilla", name: "Netscape", ver: "Mozilla" }]; // for older Netscapes (4-)

	var dataOS = [{ str: navigator.platform, substr: "Win", name: "Windows" },
				  { str: navigator.platform, substr: "Mac", name: "Mac" },
				  { str: navigator.userAgent, substr: "iPhone", name: "iPhone/iPod" },
			      { str: navigator.userAgent, substr: "iPod", name: "iPod" },
			      { str: navigator.userAgent, substr: "iPad", name: "iPad" },
			   	  { str: navigator.userAgent, substr: "Android", name: "Android" },
			   	  { str: navigator.platform, substr: "Linux", name: "Linux" }];

	var versionSearchString = "";

	var foo = this;

	var touched; // element touched (mousedown or touchstart)
	var touchStartX;
	var touchStartY;

	var lastButton; // last button clicked (mouseup or touchend), reset when touched changes

	// check performance, time code execution time
	var timingStart;
	var timingCheckpoint; // last checkpoint

	// scale delta, coordinates are shifted when scaled
	this.scaleDelta = 1;

	// possible button style attributes
	var buttonAttributes = {"disabled":0,
							"selected":1,
							"pressed":2,
							"checked":3,
							"mini":4,
							"error":5};

	this.retina = window.devicePixelRatio > 1;

	this.referrer = null; // hsyco referrer page url?

	this.vCompare = function (version, major, minor) {
		try {
			var sa = version.split(/[._ ]/);
			if (sa[0] > major) {
				return true;
			} else if (sa[0] == major && sa[1] >= minor) {
				return true;
			} else {
				return false;
			}
		} catch (err) {
			return false;
		}
	}

	function searchString(data) {
		for (var i=0;i<data.length;i++)	{
			var dataString = data[i].str;
			var dataProp = data[i].prop;
			versionSearchString = data[i].ver || data[i].name;
			if (dataString) {
				if (dataString.toLowerCase().indexOf(data[i].substr.toLowerCase()) != -1)
					return data[i].name;
			}
			else if (dataProp)
				return data[i].name;
		}
	}

	function searchVersion(dataString) {
		var index = dataString.indexOf(versionSearchString);
		if (index == -1) return;
		return parseFloat(dataString.substring(index+versionSearchString.length+1));
	}

	this.setCookie = function (name,value,mins) {
		if (mins) {
			var date = new Date();
			date.setTime(date.getTime()+(mins*60*1000));
			var expires = "; expires="+date.toGMTString();
		}
		else var expires = "";
		document.cookie = name+"="+value+expires+"; path=/";
	}

	this.getCookie = function (name) {
		var nameEQ = name + "=";
		var ca = document.cookie.split(';');
		for(var i=0;i < ca.length;i++) {
			var c = ca[i];
			while (c.charAt(0)==' ') c = c.substring(1,c.length);
			if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
		}
		return null;
	}

	this.delCookie = function (name) {
		this.setCookie(name,"",-1);
	}

	// get image size
	this.getImgSize = function (imgSrc) {
		var newImg = new Image();
		newImg.src = imgSrc;
		return {x:newImg.width, y:newImg.width};
	} // getImgSize()

	// insert at cursor
	this.insertAtCursor = function (field, value) {
	  // IE support
	  if (document.selection) {
	    field.focus();
	    var sel = document.selection.createRange();
	    sel.text = value;
	  } // insertAtCursor()

	  // MOZILLA/NETSCAPE support
	  else if (field.selectionStart || field.selectionStart == '0') {
	    var startPos = field.selectionStart;
	    var endPos = field.selectionEnd;
	    field.value = field.value.substring(0, startPos) + value + field.value.substring(endPos, field.value.length);
	  } else {
	    field.value += value;
	  }
	} // insertAtCursor()

	// get mouse relative position
	this.getMouseRelativePosition = function (evt,target) {
		var ep = this.getElementAbsolutePosition(target);
		var p = this.getMouseAbsolutePosition(evt,target);
		return {x:p.x-ep.x,y:p.y-ep.y};
	} // getMouseRelativePosition()

	// get absolute mouse position, if touch, first touch. target if != evt.target
	this.getMouseAbsolutePosition = function (evt,target) {
		var x,y;
		if (this.touchEventsSupported && evt.touches && evt.touches[0]) {
			x = evt.touches[0].pageX;
			y = evt.touches[0].pageY;
		} else if (evt.pageX != null && evt.pageY != null) {
			x = evt.pageX;
			y = evt.pageY;
		} else {
			x = (evt.layerX != null)?evt.layerX:evt.offsetX;
			y = (evt.layerY != null)?evt.layerY:evt.offsetY;
			var c = target || evt.target || evt.srcElement;
			var p = this.getElementAbsolutePosition(c);
			x += p.x;
			y += p.y;
		}

		// scale?
		if (this.scaleDelta && this.scaleDelta != 1) {
			x *= this.scaleDelta;
			y *= this.scaleDelta;
		}

		return {x:x, y:y};
	} // getMouseAbsolutePosition()

	// get element absolute position
	this.getElementAbsolutePosition = function (c) {
		var x = 0;
		var y = 0;
		while (c && c.offsetLeft != null && c.offsetTop != null) {
			x += c.offsetLeft;
			y += c.offsetTop;
			c = c.offsetParent || c.parentNode;
		}

		return {x:x, y:y};
	} // getElementAbsolutePosition()

	this.fixRGBA = function (c) {
		if (!this.rgbaSupport && c.indexOf("rgba")!=-1) {
			var a = c.replace("rgba(","").replace(")","").split(",");
			a.pop(); // a
			return "rgb("+a.join(",")+")";
		}
		return c;
	} // fixRGBA()

	// get currently selected text
	this.getSelectedText = function () {
		var selText = "";
		if (window.getSelection) {  // all browsers, except IE before version 9
			if (document.activeElement &&
				(document.activeElement.tagName.toLowerCase() == "textarea" ||
				document.activeElement.tagName.toLowerCase() == "input")) {
				var text = document.activeElement.value;
				selText = text.substring(document.activeElement.selectionStart, document.activeElement.selectionEnd);
			} else {
				var selRange = window.getSelection();
				selText = selRange.toString();
			}
		} else {
			if (document.selection.createRange) {       // Internet Explorer
				var range = document.selection.createRange();
				selText = range.text;
			}
		}
		return selText;
	} // getSelectedText()

	// set opacity. (null, 0-1)
	this.setOpacity = function (e,o) {
		// reset it?
		if (o == null || o == "") {
			if (foo.browser == "Explorer")
				e.style.filter = ""; // IE
			else
				e.style.opacity = "";
		} else {
			if (browser.browser == "Explorer")
				e.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity=" + Math.round(o*100) + ")"; // IE
			else
				e.style.opacity = o;
		}
	} // setOpacity()

	// prevent default event
	this.preventDefault = function (evt) {
		if (evt.returnValue)
			evt.returnValue = false;
		if (evt.preventDefault)
			evt.preventDefault();
	}

	// get full dom path
	this.getDomPath = function (e) {
		var path = [];
		do {
		    path.unshift(e.nodeName + (e.className ? ' class="' + e.className + '"' : ''));
		} while ((e.nodeName.toLowerCase() != 'html') && (e = e.parentNode));
		return path.join(" > ");
	}

	// check performance
	this.startTiming = function (str) {
		timingStart = (new Date()).getTime();
		timingCheckpoint = timingStart;
		webLog("timing "+(str?str:""));
	}
	this.checkTiming = function (str) {
		var now = (new Date()).getTime();
		webLog("time"+(str?" ["+str+"]":"")+": "+(now - timingCheckpoint)+" (total:"+(now - timingStart)+")");
		timingCheckpoint = now;
	}

	//-------------------------------------------------------------------------------------------------------------------------
	// Events -----------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------

	// we do it in two function. find a better way?
	function initImg(e) {
		if (browser.touchenabled) {
			if (!e.ontouchdown) e.ontouchdown = new Function("return false;");
		} else {
			if (!e.onmousedown) e.onmousedown = new Function("return false;");
		}
	}
	// init img, buttons, inputs, given a parent div
	this.initEvents = function (e) {
		var n = 0;
		var f;
		var x = /[mc].*?Button/;

		function initDiv(e) {
			if (e.className && x.test(e.className)) {
				// it's a button
				var foc = e.getAttribute("data-onclick"); // on click function
				var fod = e.getAttribute("data-ondown");
				var foov =  e.getAttribute("data-onover");
				var foou =  e.getAttribute("data-onout");
				// no events? don't init
				if (foc == null && fod == null && foov == null && foou == null) return;
				// init
				foo.initButtonEvents(e, {onclick:foc, ondown:fod, onover:foov, onout:foou});
				n++;
			}
		}

		function initInput(e) {
			f = e.getAttribute("data-autocomplete"); // autocomplete values
			if (f!=null) { // any values?
				foo.initAutocomplete(e, f);
			}
		}

		function initLabel(e) {
			e.onclick = function (){
				var pe = this.previousSibling;
				if (pe.nodeType == 3) pe = pe.previousSibling; // text node? try previous one
				pe.checked = !pe.checked;
				if (pe.onchange) pe.onchange();
			};
		}

		var d,i;
		// add onmousedown on images
		for (i = 0; (d = e.getElementsByTagName("img")[i]); i++) initImg(d);
		if (e.nodeName == "IMG") initImg(e);

		for (i = 0; (d = e.getElementsByTagName("div")[i]); i++) initDiv(d);
		if (e.nodeName == "DIV") initDiv(e);

		for (i = 0; (d = e.getElementsByTagName("span")[i]); i++) initDiv(d);
		if (e.nodeName == "SPAN") initDiv(e);

		for (i = 0; (d = e.getElementsByTagName("input")[i]); i++) initInput(d);
		if (e.nodeName == "DIV") initInput(e);

		for (i = 0; (d = e.getElementsByTagName("label")[i]); i++) initLabel(d);
		if (e.nodeName == "LABEL") initLabel(e);
	} // initEvents()

	// mouse wheel event
	this.initMouseWheelEvent = function (e,f) {
		if (!browser.touchEventsSupported)
			window.addEvent(browser.browser == "Firefox"?"DOMMouseScroll":"mousewheel", e, f);
	}

	// show image on load
	this.showImageOnLoad = function (img) {
		function onImageLoad() {img.style.display = "";}
		function onImageError() {}
		img.onload =  onImageLoad;
		img.onabort = onImageError;
		img.onerror = onImageError;
		img.style.display = "none"; // hide it
	} // showImageOnLoad()

	// ugly stuff. change it??
	// visible to all parents, so we can get the size
	this.changeDivVisibility = function (c) {
		var pd = [];
		while (c && c.style) { // container
			pd.push(c.style.display?c.style.display:null);
			c.style.display = "inline";
			c = c.parentNode;
		}
		return pd;
	}
	// restore visibility
	this.restoreDivVisibility = function (pd,c) {
		var t = 0;
		while (c && c.style) { // container
			c.style.display = pd[t]?pd[t]:"";
			t++;
			c = c.parentNode;
		}
	}

	//-------------------------------------------------------------------------------------------------------------------------
	// Buttons ----------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------

	// button class:
	//  [...] [cm][...]Button [color]? [any button attribute] [state] [...]

	// init button events. f is an object containing all events, options?
	this.initButtonEvents = function (e,f) {
		if (f.onclick) // default action is on up
			f.onup = f.onclick;
		if (browser.touchEventsSupported) {
			e.ontouchstart = function (event) {if (!browser.skipButtonEvents) browser.onButtonEvent(event,this,'touchstart',f.ondown)};
			e.ontouchmove = function (event) {if (!browser.skipButtonEvents) browser.onButtonEvent(event,this,'touchmove',f.onmove,f.onout)};
			e.ontouchend = function (event) {if (!browser.skipButtonEvents) browser.onButtonEvent(event,this,'touchend',f.onup)};
		} else {
			e.onmouseover = function (event) {if (!browser.skipButtonEvents) browser.onButtonEvent(event,this,'mouseover',f.onover)};
			e.onmouseout = function (event) {if (!browser.skipButtonEvents) browser.onButtonEvent(event,this,'mouseout',f.onout)};
			if (f.onmove)
				e.onmousemove = function (event) {if (!browser.skipButtonEvents) browser.onButtonEvent(event,this,'mousemove',f.onmove)};
			e.onmousedown = function (event) {if (!browser.skipButtonEvents) browser.onButtonEvent(event,this,'mousedown',f.ondown)};
			e.onmouseup = function (event) {if (!browser.skipButtonEvents) browser.onButtonEvent(event,this,'mouseup',f.onup)};
		}
		initButtonData(e);
		// on lift? when button is lifted with liftButton() (ex. when scrolling)
		if (f.onlift)
			e.btData.onLift = f.onlift;
		// temporary (???) solution for divs that require drag for more than n pixels
		if (f.dontLiftOnMove)
			e.btData.dontLiftOnMove = true;
		var d;
		for (var i = 0; (d = e.getElementsByTagName("img")[i]); i++) initImg(d);
	} // initButtonEvents()

	// clear button events
	this.clearButtonEvents = function (e) {
		if (browser.touchEventsSupported) {
			delete e.ontouchstart;
			delete e.ontouchmove;
			delete e.ontouchend;
		} else {
			delete e.onmouseover;
			delete e.onmouseout;
			delete e.onmousedown;
			delete e.onmouseup;
		}
	} // clearButtonEvents()

	// set button color
	this.setButtonColor = function (e,c) {
		e.btData.color = c;
		updateButtonClass(e); // update class based on e.btData
	} // setButtonColor()

	// enabled/disabled button
	this.enableButton = function (e,enable) {
		this.setButtonAttribute(e, "disabled", !enable);
	} // enableButton()

	// select/deselect button
	this.selectButton = function (e,select) {
		this.setButtonAttribute(e, "selected", select);
	} // selectButton()

	// set mini mode
	this.miniButton = function (e,mini) {
		this.setButtonAttribute(e, "mini", mini);
	} // miniButton()

	// get button style attribute
	this.getButtonAttribute = function (e,attrName) {
		if (!e.btData) initButtonData(e); // can happen when events are assigned manually
		return e.btData.attrs[buttonAttributes[attrName]];
	} // getButtonAttribute()

	// set button style attribute. name and value (true or false)
	this.setButtonAttribute = function (e,attrName,attrValue) {
		if (this.getButtonAttribute(e,attrName) != attrValue) {
			e.btData.attrs[buttonAttributes[attrName]] = attrValue;
			updateButtonClass(e); // update class based on e.btData
		}
	} // setButtonAttribute()

	// init button data from class
	function initButtonData(e) {
		var cs = e.className;
		var ss = "(.*\\s)?([mc][^\\s]*Button)(\\s("+buttonColorRegExp+"))?(\\s.*)?(\\sover|\\sdown)?"; // search string (button color regexp in include.js)
		var rx = new RegExp(ss);
		var matches = rx.exec(cs);

		// build data. pre, button class, attributes (binary string based on
		if (!matches || !matches[2]) // should this happen?
			var data = {pre:cs, bc:"", color:"", attrs:[], state:""};
		else
			var data = {pre:matches[1]?matches[1]:"",
						bc:matches[2],
						color:matches[4]?matches[4]:"", // r,g,b..
						attrs:[],
						state:matches[6]?matches[6].substr(1):""}; // ,over,down
		// attributes
		for (var a in buttonAttributes)
			data.attrs[buttonAttributes[a]] = (matches && matches[5])?matches[5].indexOf(" "+a)!=-1:false;

		// assign
		e.btData = data;
	}

	// update button class from its btData
	function updateButtonClass(e) {
		var d = e.btData;
		var c = d.pre+d.bc
		if (d.color)
			c += " "+d.color;
		for (var a in buttonAttributes)
			if (d.attrs[buttonAttributes[a]])
				c += " "+a;
		if (foo.buttonFeedbackEnabled && d.state)
			c += " "+d.state;
		e.className = c;
	}

	function disableAndroidLongPress(evt) {
		if (browser.OS == "Android") {
			evt.preventDefault && evt.preventDefault();
			evt.stopPropagation && evt.stopPropagation();
			evt.cancelBubble = true;
			evt.returnValue = false;
		}
	}

	// generic button event. graphic feedback and function associated to the event. f can be a string or a function(event,e). of is an additional optional function (user on touchmove for onout). clt: cancel long touch on android devices when clicking on an image
	this.onButtonEvent = function (event,e,w,f,of) {
		if (!e) return; // error?
		// event
		var evt = window.event || event;

		// touchevents or not?
		if (w == "touchstart" || w == "touchend" || w == "touchmove") {
			if (!browser.touchEventsSupported) return false;
		} else {
			if (evt)
				this.preventDefault(evt);
			if (browser.touchEventsSupported) return false;
		}

		if (!e.btData) initButtonData(e); // can happen when events are assigned manually

		var swi = e.btData.attrs[buttonAttributes["switch"]]; // behave like a switch?
		var swip = swi?e.btData.attrs[buttonAttributes["pressed"]]:false; // switch pressed
		var nswip = swip; // new switch pressed value, to notice if it changes

		var s = ""; // class to add, down/over (only if not disabled)
		if (!e.btData.attrs[buttonAttributes["disabled"]]) switch (w) {
		case "touchstart":
			touched = e;
			lastButton = null;
			touchStartX = evt.touches[0].clientX;
			touchStartY = evt.touches[0].clientY;
			s = "down";
			if (swi) nswip = !swip;
			disableAndroidLongPress(evt);
			break;
		case "touchmove":
			if (touched == e) {
				s = "down";
				if (!e.btData.dontLiftOnMove && // temporary (?) solution for divs that require drag for more than 40px
					(Math.abs(evt.touches[0].clientX - touchStartX) > 30 ||
					 Math.abs(evt.touches[0].clientY - touchStartY) > 30)) {
					  touched = null;
					  f = of; // execute optional function if any
				}
			}
			disableAndroidLongPress(evt);
			break;
		case "touchend":
			if (touched != e) {
				f = null; // won't execute the function
			} else {
				lastButton = touched; // store it
				this.preventDefault(evt);
			}
			touched = null;
			disableAndroidLongPress(evt);
			break;
		case "mouseover":
			if (touched) {
				if (touched == e)
					s = "down";
			} else if (foo.overEnabled) s = "over"; // over only on manager
			break;
		case "mousemove":
			break;
		case "mouseout":
			// if we're still on the same div, do nothing
			var reltg;
			if (evt) // no evt if we're calling onButtonEvent from code (like Login, on keydown)
				reltg = (evt.relatedTarget) ? evt.relatedTarget : evt.toElement;
			if (reltg) {
				while (reltg && reltg != e && reltg.nodeName != 'BODY')
					reltg = reltg.parentNode;
				if (reltg == e) return;
			}
			touched = null;
			break;
		case "mousedown":
			touched = e;
			lastButton = null;
			s = "down";
			if (swi) nswip = !swip;
			break;
		case "mouseup":
			if (touched != e) {
				f = null; // won't execute the function
			} else {
				lastButton = touched; // store it
			}
			touched = null;
			if (foo.overEnabled)
				s = "over"; // over only on manager
			break;
		}

		// update class based on e.btData
		if (s != e.btData.state || nswip != swip) {
			e.btData.state = s;
			if (swi)
				e.btData.attrs[buttonAttributes["pressed"]] = nswip;
			updateButtonClass(e);
		}

		// function
		if (f && f!="null" && !e.btData.attrs[buttonAttributes["disabled"]]) {
			// if f is a string, create a new function, otherwise just call it with (event,e)
			var func = (typeof(f) == "string")?new Function("event","e",f):f;
			func(event,e);
		}
		// don't prevent default to allow scrolling
		return false;
	} // onButtonEvent()

	// lift pressed button. bt or currently pressed button if any
	this.liftButton = function (bt) {
		if (touched && (!bt || bt == touched)) {
			if (touched.btData.state) {
				touched.btData.state = "";
				updateButtonClass(touched);
			}
			// on lift event?
			if (touched.btData.onLift)
				touched.btData.onLift();
			touched = null;
			return true;
		}
		return false;
	} // liftButton()

	// get touched button
	this.getTouchedButton = function () {
		return touched;
	} // getTouchedButton()

	// reset button: lifts and clear state (over)
	this.resetButton = function (bt) {
		// bt is currently touched? otherwise reset the class
		if (!this.liftButton(bt)) {
			bt.btData.state = "";
			updateButtonClass(bt);
		}
	}

	// get last clicked button
	this.getLastButton = function () {
		return lastButton;
	} // getLastButton()

	// ----------------------------------------------------

	// init autocomplete
	this.initAutocomplete = function (e,v) {
		e.onkeyup = new Function("event","browser.onAutocomplete(event,this,'"+v+"')");
	} // initAutocomplete()

	// autocomplete
	this.onAutocomplete = function (event,e,v) {
		if (!e) return; // error?
		// event
		var evt = window.event || event;

		var a;
		if (v.startsWith("array:"))
			a = eval(v.substring(6));
		else if (v.startsWith("list:") >= 0 )
			a = v.substring( 5 ).split('|');

		if (evt.keyCode == 16) return;
		if (evt.keyCode == 8) return; // don't deal with delete

		var s = e.value.toLowerCase();
		if (!s) return;

		var k;
		for (var i=0; i<a.length; i++) {
			k = a[i].toLowerCase().indexOf(s, 0);
			if (k == 0 && a[i].length>s.length) {
				e.value = a[i];
				if (e.createTextRange) {
					hRange = e.createTextRange()
					hRange.findText(a[i].substr(s.length));
					hRange.select();
				} else {
					e.setSelectionRange(s.length, a[i].length);
				}
				return;
			}
		}

	} // onAutocomplete()

	//-------------------------------------------------------------------------------------------------------------------------
	// Init -------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------

	function init() {
		foo.browser = searchString(dataBrowser) || "unknown";
		foo.version = searchVersion(navigator.userAgent) || searchVersion(navigator.appVersion) || "unknown";
		foo.OS = searchString(dataOS) || "unknown";

		foo.iOS = (foo.OS == "iPhone" || foo.OS == "iPod" || foo.OS == "iPad" || foo.OS == "iPhone/iPod");
		foo.iPad = (foo.OS == "iPad");

		// check if client is iOS or iPod
		if (foo.iOS && !foo.iPad) { // check version only on ipod/iphone
			var i = navigator.userAgent.indexOf("iPhone OS ");
			foo.iPhoneReload = !foo.vCompare(navigator.userAgent.substring(i + 10), 3, 0);
		}

		// has touch events?
		foo.touchEventsSupported = ('ontouchstart' in document.documentElement);

		// BTicino Touch device
		if (foo.browser == "Safari" && foo.OS == "Linux" && navigator.platform == "Linux armv7l")
			foo.touchEventsSupported = false;

		// cookies?
		foo.setCookie("test", "1", 1);
		foo.cookiesEnabled = foo.getCookie("test") == "1";
		foo.delCookie("test");

		// button feedback
		if (unescape(location.search).indexOf("nobuttonfeedback") != -1)
			foo.buttonFeedbackEnabled = false;

		// camera stream
		if ((foo.browser == "Firefox" || foo.browser == "Safari" || (foo.browser == "Chrome" && !foo.iOS)) &&
			unescape(location.search).indexOf("nocamstream") == -1)
			foo.cameraStreamEnabled = true;

		// check rgba support
		var scriptElement = document.getElementsByTagName('script')[0];
		var prevColor = scriptElement.style.color;
		try {
			scriptElement.style.color = 'rgba(0, 0, 0, 0.5)';
		} catch(e) {}
		foo.rgbaSupport = scriptElement.style.color != prevColor;
		scriptElement.style.color = prevColor;

		// scrollbar size
		var inner = document.createElement('p');
		inner.style.width = "100%";
		inner.style.height = "200px";

		var outer = document.createElement('div');
		outer.style.position = "absolute";
		outer.style.top = "0px";
		outer.style.left = "0px";
		outer.style.visibility = "hidden";
		outer.style.width = "200px";
		outer.style.height = "150px";
		outer.style.overflow = "hidden";
		outer.appendChild(inner);

		document.body.appendChild(outer);
		var w1 = inner.offsetWidth;
		outer.style.overflow = 'scroll';
		var w2 = inner.offsetWidth;
		if (w1 == w2) w2 = outer.clientWidth;

		document.body.removeChild(outer);

		foo.scrollBarSize = (w1 - w2);

		// referrer is other hsyco menu?
		// remove url queries
		var docloc = (document.location+"").split("?")[0];
		var docref = (document.referrer+"").split("?")[0];
		// remove login page bit
		docref = docref.replace("/pin.","/").replace("/login.","/"); // old format
		docref = docref.replace(".login",""); // new format
		// not in an iframe and not the same project
		if (window.top.location == document.location && docloc != docref) {
			if (document.referrer) {
				document.cookie = "referrer=" + encodeURIComponent(docref) + "; max-age=946080000"; // 30 years
				foo.referrer = docref;
			} else {
				document.cookie = "referrer=; expires=Thu, 01-Jan-70 00:00:01 GMT;";
				foo.referrer = null;
			}
		} else {
			// referrer: search in cookies
			var p = document.cookie.indexOf("referrer=");
			if (p != -1) {
				var b = p+9;
				var e = document.cookie.indexOf(";", b);
				if (e == -1)
					e = document.cookie.length;
				foo.referrer = decodeURIComponent(document.cookie.substring(b, e));
			}
		} // referrer
	} // init()

	init();
} // Browser()

var webLogReq;
var webLogStack;
function webLog(s) {
	function onLoaded() {
		if (webLogStack.length)
			webLogReq.open(webLogStack.splice(0,1),100);
	}

	if (!webLogReq) {
		webLogReq = new XMLReq();
		webLogReq.onLoaded = onLoaded;
		webLogReq.onError = onLoaded;
		webLogStack = [];
	}
	console.log(s);
	var url = "/x/weblog?" + (new Date()).getTime() + "." + encodeURIComponent(s);
	if (webLogReq.isReady())
		webLogReq.open(url);
	else
		webLogStack.push(url);
} // webLog()


//--------------------------------------------------------------------------------------------------------------------------
// XMLReq ------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------
function XMLReq() {
	var req = null; // request

	var status; // -1: aborted, 0: ready, 1:loading, 2:loaded

	var reqTimeout = null; // trigger timeout
	this.url = "";

	// custom event handlers
	this.onLoaded = null; // needed
	this.onStop = null; // when request is stopped. called with true if loading, false if reqTimeout
	this.onRetry = null; // before retrying on error
	this.onError = null; // any error not captured by custom error handlers

	// custom error handlers
	this.onConnectionError = null; // connection error
	this.onAuthenticate = null; // false if just logout, true if lock (so we reload)
	this.onNoAccess = null;

	this.maxWaitingTime = 0; // 0 nothing, msec to wait for an answer, else abort
	var waitTimeout = null; // abort after maxWaitingTime

	this.retryOnErrorDelay = 0; // retry delay msec (on any error), 0 does not retry
	this.maxRetries = 0; // max n of retries. after that, stops and fires onError event(s). if maxRetries is 0, custom onError event is never fired
	this.retries = 0; // current retry attempt

	var foo = this; // variable scope

	// errors
	this.ERROR_GENERAL = 0;
	this.ERROR_CONNECTION = 1;
	this.ERROR_MAXWAITTIME = 2;
	this.ERROR_LOGOUT = 3;
	this.ERROR_LOCK = 4;
	this.ERROR_NOACCESS = 5;

	this.init = function () {
		status = 0; // ready
		// init req
		if (window.XMLHttpRequest) {
			req = new XMLHttpRequest();
			req.onreadystatechange = onReadyStateChange;
			// branch for IE/Windows ActiveX version
		} else if (window.ActiveXObject) {
			req = new ActiveXObject("Microsoft.XMLHTTP");
			if (req) {
				req.onreadystatechange = onReadyStateChange;
			}
		}
	}

	// open url. url optional (no url:repeat). ms optional (ms:delay request)
	this.open = function (url, ms) {
		if (status == 1) // loading?
			foo.stop();
		status = 0; // ready
		if (url) { // no url? repeat last one stored
			foo.url = url;
			foo.retries = 0; // reset retries
		}
		if (reqTimeout) {
			clearTimeout(reqTimeout);
			reqTimeout = null;
		}
		if (waitTimeout) {
			clearTimeout(waitTimeout);
			waitTimeout = null;
		}
		if (ms) {
			reqTimeout = setTimeout(foo.open, ms);
			return;
		}
		if (!req) foo.init();
		status = 1; // loading
		try {
			req.open("GET", foo.url, true);
		} catch (err) {
			// If document is not fully active, throw an "InvalidStateError" exception and terminate the overall set of steps.
			// URL relative to base. If the algorithm returns an error, throw a "SyntaxError" exception and terminate these steps.
			onError(foo.ERROR_GENERAL);
			return;
		}
		req.send();

		// wait timeout
		if (foo.maxWaitingTime) {
			waitTimeout = setTimeout(onWaitTimeout, foo.maxWaitingTime);
		}
	}

	this.stop = function () {
		// on stop handler if loading, or about to load
		if (status == 1 || reqTimeout) {
			if (this.onStop)
				this.onStop(status == 1);
		}
		if (status == 1) { // loading
			status = -1; // stopped
			req.abort(); // will fire onReadyStateChange, status != 1, returns
			this.init(); // reinitialize req every time we abort
		}
		if (reqTimeout) {
			clearTimeout(reqTimeout);
			reqTimeout = null;
		}
		if (waitTimeout) {
			clearTimeout(waitTimeout);
			waitTimeout = null;
		}
		status = 0; // ready
	}

	// repeat. ms delay before repeating
	this.repeat = function (ms) {
		this.open(null, ms);
	}

	function onWaitTimeout() {
		if (waitTimeout) {
			clearTimeout(waitTimeout);
			waitTimeout = null;
		}
		status = -1; // aborted
		req.abort(); // will fire onReadyStateChange, status != 1, returns
		onError(foo.ERROR_MAXWAITTIME);
		foo.init(); // reinitialize req every time we abort
	}

	function onReadyStateChange() {
		// check readyState
		if (req.readyState == null) return; // does it ever happen?
		switch (req.readyState) {
		case 0: // unsent
		case 1: // open called, send not called
			//foo.open(); // retry?
			return;
		case 2: // headers received, still receiving
		case 3: // loading
			return; // not ready
		case 4: // done, completed or error
			// continue
		}

		if (waitTimeout) {
			clearTimeout(waitTimeout);
			waitTimeout = null;
		}

		// loading? (if aborted it's -1)
		if (status != 1) return;
		status = 2;

		// not "OK"
		if (req.status != 200) {
			onError(foo.ERROR_CONNECTION);
			return;
		}

		// get response text
		var res = "";
		try {
			res = req.responseText;
		} catch (err) {
			// If responseType is not the empty string or "text", throw an "InvalidStateError" exception.
			onError(foo.ERROR_GENERAL);
			return;
		}

		if (res.indexOf("authenticate") == 0 || res.indexOf("logout") == 0) {
			onError(foo.ERROR_LOGOUT);
			return;
		} else if (res.indexOf("lock") == 0) {
			onError(foo.ERROR_LOCK);
			return;
		} else if (res == "noaccess") {
			onError(foo.ERROR_NOACCESS);
			return;
		}

		if (foo.onLoaded) foo.onLoaded();
	}

	// on error. called for every error (but not on stop > abort)
	function onError(errCode) {
		// specific errors, if there's a custom handler it won't repeat automatically
		switch (errCode) {
		case foo.ERROR_CONNECTION:
			if (foo.onConnectionError) {
				foo.onConnectionError();
				return;
			}
			break;
		case foo.ERROR_LOGOUT:
			if (foo.onAuthenticate) {
				foo.onAuthenticate(false);
				return;
			}
			break;
		case foo.ERROR_LOCK:
			if (foo.onAuthenticate) {
				foo.onAuthenticate(true);
				return;
			}
			break;
		case foo.ERROR_NOACCESS:
			if (foo.onNoAccess) {
				foo.onNoAccess();
				return;
			}
			break;
		}
		// repeat?
		if (foo.retryOnErrorDelay) {
			if (!foo.maxRetries || foo.retries < foo.maxRetries) {
				foo.retries++;
				if (foo.onRetry) // before repeating, so we can change the retryOnErrorDelay
					foo.onRetry();
				if (foo.retryOnErrorDelay) {// could be changed by onRetry
					foo.repeat(foo.retryOnErrorDelay);
					return;
				}
			}
		}
		// no custom handlers, no (more) retries. generic error callback
		if (foo.onError)
			foo.onError(errCode);
	}

	// text getter
	this.getResponseText = function () {
		var res = "";
		try {
			res = req.responseText;
			return res;
		} catch (err) { // If responseType is not the empty string or "text", throw an "InvalidStateError" exception.
			return null;
		}
	}

	// xml getter
	this.getResponseXML = function () {
		var res = "";
		try {
			res = req.responseXML;
			return res;
		} catch (err) { // If responseType is not the empty string or "document", throw an "InvalidStateError" exception.
			return null;
		}
	}

	// is loading?
	this.isReady = function () {
		return status != 1;
	}
} // XMLReq Class


//--------------------------------------------------------------------------------------------------------------------------
// Login -------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------

// Login class. parent object, to restore key events
function Login(parent) {
	this.onLogin = null; // on login successful event

	var req = new XMLReq();
	var check = new XMLReq(); // "already logged in" check

	var reqMode = ""; // ,pin,puk

	var loginLabelE = document.getElementById("l_loginLabel");
	var loginCodeE = document.getElementById("l_loginCode");
	var loginKeypadE = document.getElementById("l_loginKeypad");

	var loginBtsE = {
		"1":document.getElementById("l_login1Bt"),
		"2":document.getElementById("l_login2Bt"),
		"3":document.getElementById("l_login3Bt"),
		"4":document.getElementById("l_login4Bt"),
		"5":document.getElementById("l_login5Bt"),
		"6":document.getElementById("l_login6Bt"),
		"7":document.getElementById("l_login7Bt"),
		"8":document.getElementById("l_login8Bt"),
		"9":document.getElementById("l_login9Bt"),
		"0":document.getElementById("l_login0Bt"),
		"c":document.getElementById("l_loginCBt"),
		"ok":document.getElementById("l_loginOkBt")
	};

	// current code
	var code = "";
	var codeLength = 0; // current code length

	var pin = "";
	var puk = "";

	var mode;
	var foo = this;

	// init
	function init() {
		req.retryOnErrorDelay = 250;
		req.maxRetries = 3;
		req.maxWaitingTime = 20000;
		req.onLoaded = onLoaded;
		req.onError = onError;

		check.onLoaded = onCheckLoaded;
		check.onRetry = onCheckRetry;
	} // init()

	// start
	this.start = function () {
		initCodeArea();
		getMode();
		askPin();

		req.stop();
		reqMode = "";

		check.retryOnErrorDelay = 100; // initial delay
		check.open("/x/getstate?-1."+(new Date()).getTime());

		document.onkeydown = onKeyDown;
		document.onkeyup = onKeyUp;
	} // start()

	// get mode
	function getMode() {
		// no cookie? there's an error. reload on C
		if (!browser.cookiesEnabled || !document.cookie) {
			mode = 0;
		} else {
			var c = browser.getCookie("key");
			if (c && c.charAt(0) == 'U')
				mode = "puk"; // requires puk also
			else
				mode = "pin"; // just pin
		}
	} // getMode()

	// ask pin
	function askPin() {
		loginLabelE.innerHTML = textD.loginenterpin;

		pin = "";
		puk = "";

		code = "";
		codeLength = 5;

		showCode();
		enable(true);
	} // askPin()

	// ask puk
	function askPuk() {
		loginLabelE.innerHTML = textD.loginenterpuk;

		puk = "";

		code = "";
		codeLength = 14;

		mode = "puk"; // force mode to require puk

		showCode();
		enable(true);
	} // askPuk()

	// init code area (to speed things up on iOS7
	var pinCodeE, pukCodeE;
	function initCodeArea() {
		var str = "";
		var str2 = "";
		var tw = loginCodeE.offsetWidth; // total width
		var cw = 0; // container width
		var nw = 16; // number cell width
		var nb = 1; // number cell border
		var x = 0; // cursor
		var s = 0; // space
		var i;
		var l = code.length; // current code length
		// pin
		for (i=0; i<5; i++) {
			s = i?15:0;
			x += s; // spacer
			str += "<div class='cell' style='float:left; width:"+nw+"px;"+(i?" margin-left:"+s+"px":"")+"'>&nbsp;</div>"
			x += nw+nb*2; // num width
		}
		str = "<div style='margin-left:"+Math.round((tw-x)/2)+"px;'>"+str+"</div>";

		// puk
		x = 0; // cursor
		for (i=0; i<14; i++) {
			s = (i==5 || i==9)?12:(i)?2:0; // spacer
			x += s;
			str2 += "<div class='cell' style='float:left; width:"+nw+"px;"+(i?" margin-left:"+s+"px":"")+"'>&nbsp;</div>"
			x += nw+nb*2; // num width
		}
		str2 = "<div style='margin-left:"+Math.round((tw-x)/2)+"px;'>"+str2+"</div>";

		loginCodeE.innerHTML = str+str2;
		pinCodeE = loginCodeE.childNodes[0];
		pukCodeE = loginCodeE.childNodes[1];
	} // initCodeArea()

	// show code
	function showCode() {
		var str = "";
		var i;
		var l = code.length; // current code length

		pinCodeE.style.display = !pin?"block":"none";
		pukCodeE.style.display = pin?"block":"none";

		if (!pin) {
			for (i=0; i<5; i++) {
				str = (l>i?"&#9679;":"&nbsp;"); // value
				pinCodeE.childNodes[i].innerHTML = str;
			}
		} else {
			for (i=0; i<14; i++) {
				str = (l>i?"&#9679;":"&nbsp;"); // value
				pukCodeE.childNodes[i].innerHTML = str;
			}
		}
	} // showCode()

	// enable (disabled when sending)
	function enable(enable) {
		for (var i in loginBtsE)
			browser.enableButton(loginBtsE[i], i=="c"?true:i!="ok"?enable:false); // c is always enabled, ok is always disabled when toggling
	} // enable

	// confirm
	function confirm() {
		if (pin == "") {
			pin = code;
			code = "";
			// ask for puk?
			if (mode == "puk") {
				askPuk();
				return;
			}
		} else if (!puk) {
			puk = code;
			code = "";
		}
		// send
		enable(false);
		loginLabelE.innerHTML = textD.loginwait;
		reqMode = mode;
		if (mode == "puk") {
			req.open("/x/auth?*" + pin + "*" + puk + "*." + (new Date()).getTime());
		} else {
			req.open("/x/auth?*" + pin + "*." + (new Date()).getTime());
		}
	} // confirm

	// on loaded
	function onLoaded() {
		reqMode = "";
		if (req.getResponseText().indexOf("puk") == 0) {
			askPuk();
		} else if (req.getResponseText().indexOf("authorized") == 0) {
			check.stop(); // stop the check
			if (parent) {
				document.onkeydown = parent.onKeyDown;
				document.onkeyup = parent.onKeyUp;
			}
			foo.onLogin();
		} else if (req.getResponseText().indexOf("error") == 0) {
			window.location.reload(true); // reload
		}
	} // onLoaded

	// on error
	function onError() {
		foo.start(); // reset
	} // onError

	// on check loaded
	function onCheckLoaded() {
		if (parent) {
			document.onkeydown = parent.onKeyDown;
			document.onkeyup = parent.onKeyUp;
		}
		foo.onLogin();
	} // onCheckLoaded

	// on check retry, increase time
	function onCheckRetry() {
		var d = check.retries*100;
		check.retryOnErrorDelay = d<2000?d:2000; // max 2 sec
		check.url = "/x/getstate?-1."+(new Date()).getTime(); // refresh url, new timestamp
	}

	/* no press/release if we need to hide it from others
	function pressButton(key) {
		browser.onButtonEvent(null,loginBtsE[key],"mouseover");
	}
	function releaseButton(key) {
		browser.onButtonEvent(null,loginBtsE[key],"mouseout");
	}
	 */

	// key down
	function onKeyDown(e) {
		if (!e) var e = window.event;
		// code
		var code;
		if (e.keyCode) code = e.keyCode;
		else if (e.which) code = e.which;
		// target
		var t;
		if (e.target) t = e.target;
		else if (e.srcElement) t = e.srcElement;
		if (t.nodeType == 3) // defeat Safari bug
			t = t.parentNode;

		if (e.metaKey) return true; // cmd-0...

		if (code == 8 || code == 32) { // BS, SPACE
			foo.onButton("c");
			//pressButton("c");
			filter = true;
		} else if (code == 13 || code == 35 || code == 42) { // ENTER, #, *
			foo.onButton("k");
			//pressButton("k");
			filter = true;
		} else if (code >= 48 && code <= 57) {
			foo.onButton(String.fromCharCode(code));
			//pressButton(String.fromCharCode(code));
			filter = true;
		} else if (code >= 96 && code <= 105) {
			foo.onButton(String.fromCharCode(code - 48));
			//pressButton(String.fromCharCode(code - 48));
			filter = true;
		} else if (code == 77) {
			foo.onButton("0");
			//pressButton("0");
			browser.onButtonEvent(null,loginBtsE["0"],"mousedown");
			filter = true;
		} else if (code == 74) {
			foo.onButton("1");
			//pressButton("1");
			browser.onButtonEvent(null,loginBtsE["1"],"mousedown");
			filter = true;
		} else if (code == 75) {
			foo.onButton("2");
			//pressButton("2");
			browser.onButtonEvent(null,loginBtsE["0"],"mousedown");
			filter = true;
		} else if (code == 76) {
			foo.onButton("3");
			//pressButton("3");
			filter = true;
		} else if (code == 85) {
			foo.onButton("4");
			//pressButton("4");
			filter = true;
		} else if (code == 73) {
			foo.onButton("5");
			//pressButton("5");
			filter = true;
		} else if (code == 79) {
			foo.onButton("6");
			//pressButton("6");
			filter = true;
		} else {
			filter = false;
		}

		if (filter) {
			if (e.returnValue)
				e.returnValue = false;
			if (e.preventDefault)
				e.preventDefault();
			return false;
		}
	} // onKeyDown()

	// key up
	function onKeyUp(e) {
		if (!e) var e = window.event;
		// code
		var code;
		if (e.keyCode) code = e.keyCode;
		else if (e.which) code = e.which;

		/* no pressing, so no releasing needed
		if (code == 8 || code == 32) { // BS, SPACE
			releaseButton("c");
		} else if (code == 13 || code == 35 || code == 42) { // ENTER, #, *
			releaseButton("k");
		} else if (code >= 48 && code <= 57) {
			releaseButton(String.fromCharCode(code));
		} else if (code >= 96 && code <= 105) {
			releaseButton(String.fromCharCode(code - 48));
		} else if (code == 77) {
			releaseButton("0");
		} else if (code == 74) {
			releaseButton("1");
		} else if (code == 75) {
			releaseButton("2");
		} else if (code == 76) {
			releaseButton("3");
		} else if (code == 85) {
			releaseButton("4");
		} else if (code == 73) {
			releaseButton("5");
		} else if (code == 79) {
			releaseButton("6");
		}
		*/
	} // onKeyUp()

	// on button event
	this.onButton = function (key) {
		switch (key) {
		case "c":
			if (code) { // delete one
				code = code.substr(0,code.length-1);
				browser.enableButton(loginBtsE.ok, code.length==codeLength);
				showCode();
			} else if (reqMode) { // abort request
				reqMode = "";
				req.stop();
				if (mode == "puk")
					askPuk();
				else
					askPin();
			} else if (mode == "puk") {
				askPin(); // back to ask pin
			}
			break;
		case "k":
			if (code.length == codeLength)
				confirm();
			break;
		default:
			if (reqMode == "" && code.length<codeLength) {
				code += key;
				browser.enableButton(loginBtsE.ok, code.length==codeLength);
				showCode();
			}
			break;
		}
	} // onButton()

	init();
} // Login Class


//--------------------------------------------------------------------------------------------------------------------------
// Parser ------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------

// Parser class
function Parser() {
	// parse index
	this.parseInterface = function (xml) {
		interfaceManager.selectDictionary(); // update interface with dictionary data

		//var pr = new DOMParser();
		//var xml = pr.parseFromString(interfaceManager.src, "application/xml");

		this.parseXML(xml);
	} // parseInterface()

	// parse include
	this.parseInclude = function (id,txt) {
		var c = this.parseHSC(txt, {include:true});

		interface.includes[id] = [];
		for (var i=0; i<c.objs.length; i++)
			interface.includes[id].push(c.objs[i]); // copy array
	} // parseInclude()

	// parse xml file. options: {include: return a container obj, object: return single object $(()) }
	this.parseXML = function (xml,options) {
		if (!options) options = {}; // no options

		// build internal objects
		//var cInterface = options.cInterface;
		var cPage = options.cPage;//null;
		var cPopup = options.cPopup;//null;
		var cContainer = (!options.cContainer && options.include)?dictionary.getObjInstance("container"):options.cContainer; // include is a container
		var p; // support for searches
		var cObj = null;
		var node, name, id, v,n; // obj name, id, value, attr name
		for (var i=0; i<xml.childNodes.length; i++) {
			node = xml.childNodes[i];
			name = node.nodeName;

			if (!node.getAttribute) continue; // text node?

			id = node.getAttribute("id");

			cObj = null;

			cObj = dictionary.getObjInstance(name,node.getAttribute("id"));
			if (cObj) {
				// attr values
				for (var k=0; k<node.attributes.length; k++) {
					n = node.attributes[k].name;
					v = node.attributes[k].value;
					cObj.setAttribute(n,v,true);
				}
				if (name == "interface") {
					cInterface = cObj;
				} else if (name == "page") {
					cPage = cObj;
					cInterface.addPage(cPage);
				} else if (cContainer) {
					cContainer.addObject(cObj);
				} else if (cPage) {
					cPage.addObject(cObj);
				} else if (cPopup) {
					cPopup.addObject(cObj);
				}
				interfaceManager.addObj(cObj);

				if (name == "page" || name == "interface")
					this.parseXML(node,{include:options.include, object:options.object, cInterface:cInterface, cPage:cPage});

				// object outside container?
				/*
				if (!cObj.parentObj) {
					if (cObj.obj.id == "background") { // special case for cInterface background
						cInterface.background = cObj.attrValues[0];
					}
				} else {
					// draw the object (generate the inner html)
					cObj.redraw();
				}
				*/
				
				if (cObj.redraw)
					cObj.redraw();
			}
		}

		if (options.include) {
			return cContainer;
		} else if (options.object) {
			return cObj;
		} else if (!options.cInterface) {
			cInterface.sortPages();
			cInterface.updateSize();
			return cInterface;
		}
	} // parseXML()

} // Parser()

//--------------------------------------------------------------------------------------------------------------------------
// Dictionary --------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------

function Dictionary(xml) {
	this.objs = []; // objects (all object kinds)

	// parse xml
	var root = xml.documentElement;
	var configRoot = root.getElementsByTagName("config")?root.getElementsByTagName("config")[0]:null;
	var objRoot = root.getElementsByTagName("objects")[0];
	var objNodes = objRoot.getElementsByTagName("obj");

	// config
	if (configRoot) {
		try { this.size = configRoot.getElementsByTagName("size")[0].childNodes[0].nodeValue; } catch (e) { this.size = ""; }
		if (this.size) {
			this.size = this.size.split(","); // x,y
			if (interface) {
				if (!interface.width) interface.width = parseInt(this.size[0]);
				if (!interface.height) interface.height = parseInt(this.size[1]);
			}
		}

		this.skin = configRoot.getElementsByTagName("skin")[0].childNodes[0].nodeValue;
		this.gridSizes = JSON.parse(configRoot.getElementsByTagName("grid")[0].childNodes[0].nodeValue);
		this.margin = {};

		this.colors = {};
		var setNodes = configRoot.getElementsByTagName("styles")[0].getElementsByTagName("set");
		var colorNodes,i,t,s,c;
		for (i=0; i<setNodes.length; i++) {
			s = setNodes[i].getAttribute("name");
			this.colors[s] = [];
			colorNodes = setNodes[i].getElementsByTagName("style");
			for (t=0; t<colorNodes.length; t++) {
				c = {};
				c.name = colorNodes[t].getAttribute("name");
				c.value = colorNodes[t].getAttribute("value");
				c.alt = colorNodes[t].getAttribute("alt");
				c.hidden = colorNodes[t].getAttribute("hidden");
				this.colors[s].push(c);
			}
		}

		var m,e;
		try {
			m = configRoot.getElementsByTagName("margin")[0].childNodes[0].nodeValue.split(",");
			this.margin.standard = [parseInt(m[0]),parseInt(m[1]),parseInt(m[2]),parseInt(m[3])];
		} catch (e) {
			this.margin.standard = [0,0,0,0];
		}
		try {
			m = configRoot.getElementsByTagName("margincompact")[0].childNodes[0].nodeValue.split(",");
			this.margin.compact = [parseInt(m[0]),parseInt(m[1]),parseInt(m[2]),parseInt(m[3])];
		} catch (e) {
			this.margin.compact = this.margin.standard;
		}
		try {
			m = configRoot.getElementsByTagName("marginkiosk")[0].childNodes[0].nodeValue.split(",");
			this.margin.kiosk = [parseInt(m[0]),parseInt(m[1]),parseInt(m[2]),parseInt(m[3])];
		} catch (e) {
			this.margin.kiosk = this.margin.standard;
		}
		try {
			m = configRoot.getElementsByTagName("marginpopup")[0].childNodes[0].nodeValue.split(",");
			this.margin.popup = [parseInt(m[0]),parseInt(m[1]),parseInt(m[2]),parseInt(m[3])];
		} catch (e) {
			this.margin.popup = [0,0,0,0];
		}
		this.bgStyle = configRoot.getElementsByTagName("bgstyle")[0].childNodes[0].nodeValue;
	}

	// objects
	var node,name,hidden,src,obj,attr,a,v,k,t; // object properties
	for (var i=0; i<objNodes.length; i++)  {
		node = objNodes[i]; // node

		name = node.getAttribute("name");
		hidden = node.getAttribute("hidden");
		try { src = node.getElementsByTagName("src")[0].childNodes[0].nodeValue; } catch (e) { src = ""; }

		// new object
		obj = new Obj(name,hidden,src);
		this.objs.push(obj);

		attr = node.getElementsByTagName("attr");
		for (t=0; t<attr.length; t++) {
			a = attr[t].attributes;
			v = {};
			for (k=0; k<a.length; k++)
				v[a[k].name] = a[k].value;
			if (attr[t].parentNode.nodeName == "attrgroup") {
				v.forattr = attr[t].parentNode.getAttribute("on");
				v.forvalue = attr[t].parentNode.getAttribute("for");
			}
			obj.addAttribute(v);
		}
	}

	// include obj
	/*
	var includeObj = new Obj({id:"#include", grid:"1", hidden:false, closedby:"#include"},"<div></div>","","0,0");
	includeObj.addAttr({name:"File", type:"include"});
	includeObj.hidden = true;
	this.objs.push(includeObj);
	*/

	// get object, given name
	this.getObj = function (name) {
		for (var i=0; i<this.objs.length; i++)
			if (this.objs[i].name == name)
				return this.objs[i];
	}

	// get object instance, given the object name and the id
	this.getObjInstance = function (name,id) {
		return this.getObj(name).getInstance(id);
	} // getObjInstance()

	// calculate row/column based on x/y
	this.calcRowColumn = function (x,y,grid) {
		return {c:Math.floor((x-this.gridSizes[grid-1][0])/this.gridSizes[grid-1][2])+1,
				r:Math.floor((y-this.gridSizes[grid-1][1])/this.gridSizes[grid-1][3])+1};
	} // calcRowColumn()

	// calculate x/y based on row/column
	this.calcXY = function (r,c,grid) {
		return {x:(c-1)*this.gridSizes[grid-1][2]+this.gridSizes[grid-1][0],
				y:(r-1)*this.gridSizes[grid-1][3]+this.gridSizes[grid-1][1]};
	} // calcXY()

	// get color regular expression to be used when changing a color class
	// all this stuff is elegant, but nearly useless, since it's used just to change color on button objects (light=blue, autom=gray)
	// use it (from the console) to get the string to paste in include.js
	this.getColorRegExp = function (colorSet) {
		var col = [];
		for (var i=0; i<this.colors[colorSet].length; i++) {
			col.push(this.colors[colorSet][i].value.replace(" ","\s"));
			if (this.colors[colorSet][i].alt)
				col.push(this.colors[colorSet][i].alt.replace(" ","\s"));
		}
		col = col.unique().sort(function(a, b){ return b.length - a.length; }); // ASC -> a - b; DESC -> b - a
		if (col[col.length-1]=="") col.pop();
		return col.join("|"); // glass|gr|g|b|y|r
	} // getColorRegExp()
} // Dictionary()

//--------------------------------------------------------------------------------------------------------------------------
// Classes -----------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------

// Obj attribute class
function ObjAttr(v) {
	this.name = v.name;
	this.type = v.type;
	this.defaultValue = v["default"];
	this.group = v.group;
	// values
	this.values = null;
	if (v.values)
		this.values = v.values.split(",");
	if (v.styleset) // styleset for color type attributes
		this.styleSet = v.styleset;
	// labels
	this.labels = null;
	if (v.labels)
		this.labels = v.labels.split(",");
	// optional
	this.optional = (v.optional == "true");
	// can be empty?
	this.empty = (v.empty == "true");
	// only accessible in edit? (not when adding)
	this.onlyEdit = (v.onlyedit == "true");

	// attr group
	this.forattr = v.forattr;
	this.forvalue = v.forvalue;
} // ObjAttr Class

// Obj declaration class
function Obj(name, hidden, src) {
	// name
	this.name = name;
	// source
	this.source = src;
	// other parameters
	this.hidden = (hidden == "true"); // not selectable from the palette

	// attributes
	this.attributes = [];
	var attributesByName = {}; // attributes hash map

	// is description attribute? TODO:move elsewhere?
	this.isDescrAttr = function (v) {
		return (v == "id" ||
				v == "label" ||
				v == "img" ||
				v == "device" ||
				v == "scene" ||
				v == "camera" ||
				v == "timer" ||
				v == "security" ||
				v == "music" ||
				v == "temp")
	}

	// add an attribute
	this.addAttribute = function (v) {
		var a = new ObjAttr(v)
		this.attributes.push(a);
		attributesByName[a.name] = a;
	} // addAttribute()

	// get an attribute by name
	this.getAttribute = function (n) {
		return attributesByName[n];
	} // getAttribute()

	// get instance of this object
	this.getInstance = function (id) {
		var div = document.createElement("div");
		div.innerHTML = this.source;
		var o = new Objects[this.name](); //div.firstChild); // no id needed?
		o.init(div.firstChild, this);
		o.type = this.name;
		o.setAttribute("id",id);
		return o;
	}

	this.addAttribute({name:"id", type:"id"});
} // Obj Class

////////////////////////
var interfaceManager = new function () {
	// pointers to all objects
	this.all = [];

	// pointers to objects. {id:[ ... ], ...}
	var byId = {};
	var byAddress = {};
	var byType = {};

	// generic add by function. object, which array (byId, byAddress..), attribute value (id, address..)
	function addBy(o,arr,attrv) {
		if (!arr[attrv]) // ex. byId["dummy.1"]
			arr[attrv] = [];
		arr[attrv].push(o);
	}

	// generic get by function
	function getBy(arr,value) {
		return (arr[value]?arr[value]:[]); // if no array return an empty one
	}

	// get objects by ..
	this.getByType = function (type) { return getBy(byType,type); }
	this.getByAddress = function (address) { return getBy(byAddress,address); }
	this.getById = function (id) { return getBy(byId,id); }

	// add object
	this.addObj = function (o) {
		this.all.push(o);
		if (o.obj) addBy(o,byType,o.obj.name);/* TODO every item has a type!!!! */
		if (o.getAttribute && o.getAttribute("id")) addBy(o,byId,o.getAttribute("id")); /* TODO every item has an id!!!! */
		if (o.getAttribute && o.getAttribute("address"))	addBy(o,byAddress,o.getAttribute("address"));
	}

	// get single object (first) by id
	this.getObjById = function (id) {
		return this.getById(id)[0];
	}

	// get objects by id
	this.getObjsById = function (id) {
		return this.getById(id);
	}

	// get objects by type
	this.getObjsByType = function (type) {
		return this.getByType(type);
	}

	// get all objects in the interface. parent object
	this.getAllObjs = function (p) {
		var a = [];
		var r;
		var i;
		/* TODO
		// no parent, all pages
		if (!p) {
			for (i=0; i<interface.pages.length; i++) {
				r = this.getAllObjs(interface.pages[i]);
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
		*/
		return a;
	} // getAllObjs()


	// default values
	this.name = "new";
	this.timestamp = 0; // last modified timestamp

	this.skin = "blue";
	this.language = "en";
	this.header = "HSYCO";

	this.src = "";

	this.orientationMode = false; // if true, page and menu objs will be #landscape,#portrait

	this.width = 890;
	this.height = 640;

	this.width2 = null; // only available when orientationMode is true
	this.height2 = null;

	if (dictionary && dictionary.size) { // done by Dictionary() too
		this.width = parseInt(dictionary.size[0]);
		this.height = parseInt(dictionary.size[1]);
	}

	this.locked = false; // if locked, won't send commands
	this.kiosk = ""; // kiosk mode. "","nolock","lock"
	this.scale = null;
	this.uiset = null; // initial uiset
	this.style = null; // interface style
	this.background = "";

	this.warnings = null;

	this.cameraList = "";
	this.cameraGridList = "";
	this.cameraOverlay = "";

	this.includes = {}; // include containers
	this.includesNeeded = []; // list of needed includes, to load

	this.deviceImage = true; // default

	this.status = "loading"; // loading (default when creating a interface), new (when creating a new interface), ready (set on onInterfaceLoaded)


	this.newInterface = function (ts) {
		this.timestamp = ts;
	}

	// update interface with current dictionary data
	this.selectDictionary = function () {
		/* TODO
		if (dictionary.size) {
			if (!this.width) this.width = parseInt(dictionary.size[0]);
			if (!this.height) this.height = parseInt(dictionary.size[1]);
		}
		*/
	} // selectDictionary()

	// is compact?
	this.isPageCompact = function (w,h) {
		return (w<=500);
	} // isPageCompact()


	/* TODO
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

		interface.includesNeeded.push({file:obj.attrValues[0], objs:[obj]});

		return true;
	} // addInclude()
	*/

	/*
	// reset all objects
	this.reset = function () {
		for (var i=0; i<this.pages.length; i++) {
			for (var k=0; k<this.pages[i].objs.length; k++) {
				this.pages[i].objs[k].reset();
			}
		}
	} // reset all

	this.addPageDivs = function (div) {
		for (var i=0; i<this.pages.length; i++)
			div.appendChild(this.pages[i].e);
	}
	*/
}


//--------------------------------------------------------------------------------------------------------------------------------
// Objects -----------------------------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------------------------------------

function parseBool(str) {
	if (typeof str === "string" && str.toLowerCase() == "true")
		return true;
	return (parseInt(str) > 0);
}

function parseXML(str) {
	var xmlDoc;
	if (window.DOMParser) {
		var parser = new DOMParser();
		xmlDoc = parser.parseFromString(str, "text/xml");
	}
	else {// Internet Explorer
		xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
		xmlDoc.async = false;
		xmlDoc.loadXML(str); 
	}
	return xmlDoc;
}

var Objects = {};

// common functions
Objects.Utils = {
	// add time (hh, mm, + n > hhmm)
	addTime : function (h,m,n) {
		var nh = parseInt(h, 10);
		var nm = parseInt(m, 10) + parseInt(n, 10);
		while (nm>59) {
			nh += 1;
			nm -= 60;
		}
		while (nm<0) {
			nh -= 1;
			nm += 60;
		}
		while (nh>24) {
			nh -= 24;
		}
		while (nh<0) {
			nh += 24;
		}
		h = (nh > 9?"":"0")+nh;
		m = (nm > 9?"":"0")+nm;
		return h+m;
	},
	// sub time (hh, mm, hh, mm > n mins)
	subTime: function (ah,am,bh,bm) {
		var nh = parseInt(bh, 10) - parseInt(ah, 10);
		var nm = parseInt(bm, 10) - parseInt(am, 10);
		return nh*60 + nm;
	},

	// get date string from date
	getDateValue : function (d) {
		var sd = d.getDate()+"";
		if (sd<10) sd = '0'+sd;
		var sm = (d.getMonth()+1)+"";
		if (sm<10) sm = '0'+sm;
		var sy = d.getFullYear()+"";

		return sy+sm+sd;
	},
	// add date (yyyy, mm, dd, + n > yyyymmdd)
	addDate : function (y,m,d,n) {
		var ny = parseInt(y, 10);
		var nm = parseInt(m, 10);
		var nd = parseInt(d, 10);
		var nn = parseInt(n, 10);

		var a = new Date(ny, nm-1, nd); // month is 0 based
		a.setDate(a.getDate()+nn); // +1 day

		return this.getDateValue(a);
	},
	// sub date (yyyy, mm, dd, yyyy, mm, dd > n days)
	subDate: function (ay,am,ad,by,bm,bd) {
		var ny = parseInt(ay, 10);
		var nm = parseInt(am, 10);
		var nd = parseInt(ad, 10);

		var datea = new Date(ny, nm-1, nd); // month is 0 based

		ny = parseInt(by, 10);
		nm = parseInt(bm, 10);
		nd = parseInt(bd, 10);

		var dateb = new Date(ny, nm-1, nd); // month is 0 based

		// diff between two dates / day msecs
		return Math.ceil((dateb.getTime()-datea.getTime())/(1000*60*60*24));
	},

	// get time string from h,m
	getTimeValue : function (h,m) {
		h = (h<10?'0':'')+h;
		m = (m<10?'0':'')+m;
		return h+m;
	}
}


Objects.extend = function () {
	for (var i = 1; i < arguments.length; i++) {
		Objects[arguments[i]].call(Objects[arguments[0]].prototype);
	}
}


// mixin base object
Objects.__base = function () {
	this.init = function (e, obj) {
		this.e = e;
		this.visible = true;
//		this.id = "";
		this.type = "";
		
		this.container = null;
		this.page = null;

		this.attributes = {};
		this.obj = obj; // dictionary object
		
		// default attribute values
if (!this.obj) return; // REMOVEEEEEE		
		var a;
		for (var i=0; i<this.obj.attributes.length; i++) {
			a = this.obj.attributes[i];
			if (a.defaultValue != null)
				this.setAttribute(a.name, a.defaultValue);
		}
	}

	// check absolute visibility, containers...
	this.isVisible = function () {
		return (this.visible && (!this.container || this.container.isVisible()));
	}

	this.getAttribute = function (name) {
		if (!isNaN(name) && parseInt(Number(name)) == name)
			return this.attributes[this.obj.attributes[name].name] 
		else
			return this.attributes[name];
	}

	this.updateAttribute = function (name, value) {
		if (value != null) {
			this.attributes[name] = value;
			return true;
		} else {
			return false;
		}
	}

	this.getAttributeValues = function () {
		var r = [];
		var n,v;
		for (var i=0; i<this.obj.attributes.length; i++) {
			n = this.obj.attributes[i].name;
			v = this.getAttribute(n);
			if (!v) v = ""; // not null
			if (v || !this.obj.attributes[i].optional)
				r.push({name:n, value:v});
		}
		return r;
	}

	this.setAttribute = function (name, value) {
		var v;
		// general attributes (for every kind of object)
		switch (name) {
		case "id":
			v = value;
			//this.id = value;
			this.e.setAttribute("data-id",v);
			break;
		case "visible":
			v = parseBool(value != "false" && value != false);
			// show/hide?
			if (this.visible != v) {
				this.e.style.display = v?"inline":"none";
				this.visible = v;
				// events? visibility changed? we need to check the absolute visibility
				if (v && this.onShow && this.isVisible())
					this.onShow();
				else if (!v && this.onHide && !this.isVisible())
					this.onHide();
			}
			break;
		} // switch general attributes

		return this.updateAttribute(name, v);
	} // setAttribute()

	this.redraw = function () {}
} // Objects.__base mixin

// mixin base positional object
Objects.__pos = function () {
	var toDraw; // draw on show

	var foo;

	// get label tag if any
	function getLabel() {
		var d;
		for (var i = 0; (d = foo.e.getElementsByTagName("div")[i]); i++) {
			if (d.getAttribute("name") == "label") {
				return d; // only the first
			}
		}
		for (var i = 0; (d = foo.e.getElementsByTagName("span")[i]); i++) {
			if (d.getAttribute("name") == "label") {
				return d; // only the first
			}
		}
		return null;
	}

	var _setAttribute = this.setAttribute;
	this.setAttribute = function (name, value) {
		foo = this;
		var v;
		// general attributes (for every kind of object)
		switch (name) {
		case "x":
		case "y":
			v = parseInt(value);
			this.e.style[name=="x"?"left":"top"] = v+"px";
			break;
		case "image": // just replace the first image it finds
			v = value;
			var img = this.e.getElementsByTagName("img")[0];
			if (value.indexOf("http://") == 0 || value.indexOf("https://") == 0 || value.indexOf("/"+templateName+"/pic/")==0)
				img.src = value;
			else if (this.e.getAttribute("data-imgpath")=="pic")
				img.src = "/"+templateName+"/pic/"+value;
			else
				img.src = "/"+config.path+"/img/"+value;
			break;
		case "draw":
			v = value;
			if (this.isVisible()) {
				draw(value);
			} else {
				toDraw = value;
			}
			break;
		case "label":
			v = value;
			var d = getLabel();
			if (d)
				d.innerHTML = value;
			break;
		// label
		case "fontsize":
			v = value;
			var d = getLabel();
			if (d)
				d.style.fontSize = value;
			break;
		case "fontcolor":
			v = value;
			var d = getLabel();
			if (d)
				d.style.color = value;
			break;
		case "fontfamily":
			v = value;
			var d = getLabel();
			if (d)
				d.style.fontFamily = value;
			break;
		case "fontstyle":
			v = value;
			var d = getLabel();
			if (d)
				d.style.fontStyle = value;
			break;
		case "fontweight":
			v = value;
			var d = getLabel();
			if (d)
				d.style.fontWeight = value;
			break;
		case "blink":
			v = value;
			animations.setBlink(this.e, value);
			break;
		case "opacity":
			v = value;
			browser.setOpacity(this.e, value); // 0-1
			break;
		case "focus":
			v = value;
			if (value != "false" && value != false && this.focus)
				this.focus();
			break;
		default:
			return _setAttribute.call(this, name, value);
		} // switch general attributes

		return this.updateAttribute(name, v);
	} // setAttribute()

	function draw(value) {
		var img = foo.e.getElementsByTagName("img")[0];
		var w = img.offsetWidth;
		var h = img.offsetHeight;
		if (browser.retina) {
			w += w;
			h += h;
		}
		var src = urls.draw+"?"+w+"*"+h+"*";
		if (browser.retina) {
			src += "scale(2,2);";
		}
		src += value;
		img.src = src;
	} // draw()

	this.onAfterShow = function () {
		foo = this;
		if (toDraw) {
			draw(toDraw);
			toDraw = null;
		}
	}
} // Objects.__pos mixin

// container mixin
Objects.__container = function () {
	var _init = this.init;
	this.init = function (e, obj) {
		if (_init) _init.call(this, e, obj);
		this.objects = []; // objects array
	}
	
	// add object. obj, pos (order, used in undo)
	this.addObject = function (o,pos) {
		if (pos==null)
			this.objects.push(o);
		else
			this.objects.splice(pos,0,o);
	 	// set container link
		o.container = this;
		// set page link
		if (this.page) {
			o.page = this.page;
		} else {
			o.page = this; // it's a page
		}
		this.e.appendChild(o.e);
	}
	this.removeObject = function (o) {
		for (var i=0; i<this.objects.length; i++) {
			if (this.objects[i] == o) {
				o.container = null;
				o.page = null;
				this.objects.splice(i,1);
				return;
			}
		}
		this.e.removeChild(o.e);
	}
	this.removeObjects = function () {
		for (var i=0; i<this.objects.length; i++) {
			this.objects[i].container = null;
			this.objects[i].page = null;
		}
		this.objects = [];
	}

	// show/hide
	this.onShow = function () {
		// call onShow on all page's objects
		for (var i=0; i<this.objects.length; i++)
			if (this.objects[i].onShow && this.objects[i].visible)
				this.objects[i].onShow();
	}
	var _onAfterShow = this.onAfterShow;
	this.onAfterShow = function () {
		// call onAfterShow on all page's objects
		for (var i=0; i<this.objects.length; i++)
			if (this.objects[i].onAfterShow && this.objects[i].visible)
				this.objects[i].onAfterShow();
		if (_onAfterShow) _onAfterShow(); // __base
	}
	this.onHide = function () {
		// call onHide on all page's objects
		for (var i=0; i<this.objects.length; i++) {
			if (this.objects[i].onHide && this.objects[i].visible)
				this.objects[i].onHide();
		}
	}
} // Objects.__container mixin

// page mixin
Objects.__page = function () {
	var _init = this.init;
	this.init = function (e, obj) {
		if (_init) _init.call(this, e, obj);
		// on before/after show/hide event, externally assigned
		this.onBeforeShow = null;
		this.onBeforeHide = null;
		this.onAfterShow = null;
		this.onAfterHide = null;
	}

	// show/hide
	this.show = function () {
		this.visible = true;
		document.onkeydown = null; // always reset keydown
		this.e.style.display = "inline";
	}
	this.hide = function () {
		this.visible = false;
		this.e.style.display = "none";
	}
} // Objects.__page mixin

// button mixin
Objects.__button = function () {
	var _setAttribute = this.setAttribute;
	this.setAttribute = function (name, value) {
		var v;
		// general attributes
		switch (name) {
		case "style":
			v = value;
			browser.setButtonColor(this.e, value);
			break;
		case "highlight":
			v = parseBool(value);
			browser.setButtonAttribute(this.e,"selected",(value != false && value != "false"));
			break;
		case "enabled":
			v = parseBool(value);
			browser.setButtonAttribute(this.e,"disabled",!v);
			break;
		default:
			return _setAttribute.call(this, name, value);
		} // switch general attributes

		return this.updateAttribute(name, v);
	} // setAttribute()
} // Objects.__button mixin


//////////////////////////////////////*******************************************************************
//////////////////////////////////////*******************************************************************
//////////////////////////////////////*******************************************************************






//delegate base object
Objects._base = function (parent) {
	parent.visible = true; // AUGMENT: visibility

	// check absolute visibility, containers...
	parent.isVisible = function () {
		return (this.visible && (!this.container || this.container.isVisible()));
	}

	var toDraw; // draw on show

	// get label tag if any
	function getLabel() {
		var d;
		for (var i = 0; (d = parent.e.getElementsByTagName("div")[i]); i++) {
			if (d.getAttribute("name") == "label") {
				return d; // only the first
			}
		}
		for (var i = 0; (d = parent.e.getElementsByTagName("span")[i]); i++) {
			if (d.getAttribute("name") == "label") {
				return d; // only the first
			}
		}
		return null;
	}

	this.setAttribute = function (attr, value) {
		// general attributes (for every kind of object)
		switch (attr) {
		// position
		case "pos":
			var ypos = value.indexOf("y");
			if (ypos != -1) {
				var x = value.substring(1,ypos);
				var y = value.substr(ypos+1);
				parent.e.style.left = x+"px";
				parent.e.style.top = y+"px";
				// popups? reopen
				if (client.thumbnailPopup.bt == parent)
					client.openThumbnailPopup(parent, client.thumbnailPopup.lock);
				if (client.sliderPopup.bt == parent)
					client.openSliderPopup(parent.address, parent);
			}
			break;
		case "visible":
			var v = (value != "false" && value != false);
			// show/hide?
			if (parent.visible != v) {
				parent.e.style.display = v?"inline":"none";
				parent.visible = v;
				// events? visibility changed? we need to check the absolute visibility
				if (v && parent.onShow && parent.isVisible())
					parent.onShow();
				else if (!v && parent.onHide && !parent.isVisible())
					parent.onHide();
			}
			break;
		case "img": // just replace the first image it finds
			var img = parent.e.getElementsByTagName("img")[0];
			if (value.indexOf("http://") == 0 || value.indexOf("https://") == 0 || value.indexOf("/"+templateName+"/pic/")==0)
				img.src = value;
			else if (parent.e.getAttribute("data-imgpath")=="pic")
				img.src = "/"+templateName+"/pic/"+value;
			else
				img.src = "/"+config.path+"/img/"+value;
			break;
		case "draw":
			if (parent.isVisible()) {
				draw(value);
			} else {
				toDraw = value;
			}
			break;
		case "value":
		case "text":
		case "label":
			var d = getLabel();
			if (d)
				d.innerHTML = value;
			break;
		// label
		case "fontsize":
			var d = getLabel();
			if (d)
				d.style.fontSize = value;
			break;
		case "fontcolor":
			var d = getLabel();
			if (d)
				d.style.color = value;
			break;
		case "fontfamily":
			var d = getLabel();
			if (d)
				d.style.fontFamily = value;
			break;
		case "fontstyle":
			var d = getLabel();
			if (d)
				d.style.fontStyle = value;
			break;
		case "fontweight":
			var d = getLabel();
			if (d)
				d.style.fontWeight = value;
			break;

		case "blink":
			animations.setBlink(parent.e, value);
			break;
		case "opacity":
			browser.setOpacity(parent.e, value); // 0-1
			break;
		case "focus":
			if (value != "false" && value != false && parent.focus)
				parent.focus();
			break;
		} // switch general attributes
	} // setAttribute()

	function draw(value) {
		var img = parent.e.getElementsByTagName("img")[0];
		var w = img.offsetWidth;
		var h = img.offsetHeight;
		if (browser.retina) {
			w += w;
			h += h;
		}
		var src = urls.draw+"?"+w+"*"+h+"*";
		if (browser.retina) {
			src += "scale(2,2);";
		}
		src += value;
		img.src = src;

	} // draw()

	this.onAfterShow = function () {
		if (toDraw) {
			draw(toDraw);
			toDraw = null;
		}
	}

} // Objects._base

//delegate button object
Objects._button = function (parent) {
	var enabled = true;

	this.setAttribute = function (attr, value) {
		// general attributes (for every kind of object)
		switch (attr) {
		case "color":
			browser.setButtonColor(parent.e, value);
			break;
		case "highlight":
			browser.setButtonAttribute(parent.e,"selected",(value != false && value != "false"));
			break;
		case "enabled":
			enabled = (value != false && value != "false");
			browser.setButtonAttribute(parent.e,"disabled",!enabled);
			break;
		} // switch general attributes
	} // setAttribute()
} // Objects._button

// delegate field object (field, timepanel, datepanel, submit...)
Objects._field = function (parent) {
	var sending; // sent value
	var saved = ""; // current value
	var enabled = true;
	var rollBackEnabled = true; // rollback to previous values on error

	this.e = parent.e; // so we can open a popup
	this.popups = {}; // can have associated popups

	var foo = this;

	this.page = parent.page; // so page:close works

	// showing error if couldn't send
	var errorIntervalId;
	var errorCounter = 0;

	// update saved value, so we can restore it
	this.updateValue = function () {
		saved = parent.getValue();
		sending = null; // set to null so nothing happens on command loaded
	}

	// send value. value already encoded
	this.sendValue = function (value) {
		clearError();
		sending = value;
		var url;
		var val = value.replace(/\./g,"%2E"); // remove dots;
		var id;
		if (parent.type == "timerpanel" || parent.type == "timer") {
			url = urls.timer;
		} else {
			url = urls.vrem;
			id = encodeURIComponent(parent.getAttribute("id")).replace(/\./g,"%2E");;
			if (client.projectCookie)
				id += "/"+client.projectCookie;
		}
		client.sendCommand(url,val,id,foo); // url, value, id, link
	}

	// events called externally by client
	this.onCommandLoaded = function () {
		if (sending == null) return; // sending is set to null if setAttribute received before command loaded
		parent.setValue(decodeURIComponent(sending)); // successful, sending was encoded
		foo.updateValue();
		foo.updateAllOthers(); // set all other fields with same id to saved
		sending = null; // done
	}
	this.onCommandError = function () {
		if (sending == null) return; // sending is set to null if setAttribute received before command loaded
		this.rollBack(); // resume previously saved value
	}

	this.rollBack = function () {
		if (rollBackEnabled)
			parent.setAttribute("value",saved);
		showError();
	}

	// called when changing page if commandLink is established
	this.onCommandPageHide = function () {
		var url = urls.vrem;
		var val = "/close";
		var id = encodeURIComponent(parent.getAttribute("id")).replace(/\./g,"%2E");

		client.sendCommand(url,val,id,foo);
	}

	// update all other fields with same id
	this.updateAllOthers = function () {
		var o = client.getObjsById(parent.getAttribute("id"));
		for (var i=0; i<o.length; i++) {
			if (o[i] != parent)
				o[i].setAttribute("value", saved);
		}
	}

	// show error
	function showError() {
		clearError();
		errorIntervalId = setInterval(updateError,300);
		updateError(); // update now
	}
	function updateError() {
		errorCounter++;
		parent.showError(true,errorCounter%2);
		if (errorCounter == 8)
			clearError();
	}
	function clearError() {
		if (errorIntervalId) {
			parent.showError(false);
			clearInterval(errorIntervalId);
			errorIntervalId = null; // make sure?
			errorCounter = 0;
		}
	}

	// is enabled getter
	this.isEnabled = function () {
		return enabled;
	} // isEnabled()

	// update class
	function updateClass() {
		var c = parent.e.className;
		var r = new RegExp("[mc][^B^\\s]*Button");
		// button?
		if (r.test(c)) {
			browser.setButtonAttribute(parent.e,"disabled",!enabled);
		} else { // set element css class
			r = new RegExp(parent.type+"(\\sautoSend)?(\\slinkedPanel)?(\\snoPanel)?(\\sdisabled)?");
			parent.e.className = parent.e.className.replace(r,parent.type+(parent.autoSend?" autoSend":"")+(parent.linkedField?" linkedPanel":"")+(parent.noPanel?" noPanel":"")+(enabled?"":" disabled"));
		}
	}

	this.onShow = function () {
		if (parent.linkedField) // panel?
			parent.focus();

		// auto send?
		if (parent.autoSend == null) {
			var c = parent.container;
			var s = false; // is there a submit in a parent container?
			while (c) {
				for (var i=0; i<c.objects.length; i++) {
					if (c.objects[i] instanceof Objects.submit) {
						s = true;
						break;
					}
				}
				c = c.container;
			}
			parent.autoSend = !s; // if there's no button, auto send value
		}

		updateClass();
	}
	this.onHide = function () {
		if (parent.blur) // submit doesn't have blur
			parent.blur();
		for (var id in this.popups) {
			client.closePopup(this.popups[id]); // so popups don't float, we close them
		}
	}

	// set attribute. manual redraw optimizes multiple calls (manually redraw at the end)
	this.setAttribute = function (attr, value, manual) {
		switch (attr) {
		case "panel":
			parent.noPanel = (value == false || value == "false");
			updateClass();
			break;
		case "enabled":
			enabled = (value != false && value != "false");
			updateClass();
			break;
		case "autosend":
			parent.autoSend = (value == "" || value == "auto")?null:(value != "false" && value != false);
			updateClass();
			break;
		case "error":
			if (value != false && value != "false")
				showError();
			else
				clearError();
			break;
		case "rollback":
			this.rollBack();
			break;
		case "rollbackenabled":
			rollBackEnabled = (value != false && value != "false");
			break;
		}
	} // setAttribute()
} // Objects._field

// delegate device object (button, buttonimage has-a)
Objects._device = function (parent) {
	// properties
	this.deviceFunction = null;
	this.type = null;
	this.image = null;
	this.options = null;

	parent.popups = {}; // AUGMENT: can have associated popups

	var _base = null; // base delegate

	var timeout = 500; // long press
	var timeoutId = null; // timeout for long press

	var popupTimeout = 250;
	var popupTimeoutId = null;

	var foo = this;

	// init
	function init() { // if it's possible to reload JSONTopo, this should method be public
		_base = new Objects._base(parent); // setup delegate

		if (!JSONTopo.hsycotopology.devices ||
			!JSONTopo.hsycotopology.devices[parent.address]) return;

		foo.deviceFunction = JSONTopo.hsycotopology.devices[parent.address][0];
		foo.type = JSONTopo.hsycotopology.devices[parent.address][3];
		foo.image = JSONTopo.hsycotopology.devices[parent.address][4];
		foo.options = JSONTopo.hsycotopology.devices[parent.address][5];

		if (!foo.deviceFunction || foo.type == undefined) return; // can be "", null, undefined

		var f = {};
		f.onover = foo.onOver; // won't be called on touch devices
		f.onout = foo.onOut; // called on touch devices when dist > n px
		if (!foo.options || foo.options.lastIndexOf("NOCLICK") == -1) {
			if (foo.type == "DIMMER" || foo.type == "HSHUT" || foo.type == "VSHUT") {
				f.ondown = foo.onDown;
				f.onup = foo.onUp;
			}

			f.onclick = foo.onClick;
		}
		browser.initButtonEvents(parent.e,f);
	}

	// events
	this.onOver = function () {
		if (interfaceLocked) return;
		// locked and on same bt? return
		if (client.thumbnailPopup.lock && client.thumbnailPopup.bt == parent) return;

		if (client.thumbnailPopup.bt == parent) { // already open on the same bt?
			if (client.thumbnailPopup.popup.visible)
				client.closeThumbnailPopup(5000); // refresh close timeout
		} else if (!foo.image) {
			client.closeThumbnailPopup();
		} else if (client.thumbnailPopup.bt) { // thumbnail already open on another bt
			client.closeThumbnailPopup();
			client.openThumbnailPopup(false, parent, foo.image); // show now
		} else { // delay item show
			client.openThumbnailPopup(false, parent, foo.image, 250);
		}
	} // onOver()

	this.onOut = function () {
		if (interfaceLocked) return;
		clearTimeout(timeoutId);
		if (parent != client.thumbnailPopup.bt) return;
		if (client.thumbnailPopup.lock) return;
		// delay close
		client.closeThumbnailPopup(250);
	} // onOut()

	// on down
	this.onDown = function () {
		if (interfaceLocked) return;
		clearTimeout(timeoutId);
		timeoutId = setTimeout(onLongPress, timeout);
	} // onDown()

	// on up
	this.onUp = function () {
		if (interfaceLocked) return;
		clearTimeout(timeoutId);
	} // onUp()

	// long press
	function onLongPress() {
		if (interfaceLocked) return;
		if (foo.type == "DIMMER" || foo.type == "HPOS" || foo.type == "VPOS") {
			browser.resetButton(parent.e); // lift without clicking
			if (client.sliderPopup.address != parent.address) {
				// show display popup slider
				if (parent.value > 0) { // state is on
					client.sliderPopup.address = parent.address;
				} else if (parent.value==0 && client.sliderPopup.bt != parent.e) {
					client.sliderPopup.address = parent.address;
				}
				// slider popup options
				var o = {};
				o.inverse = (foo.type == "VPOS");
				o.vertical = (foo.type != "HPOS");
				o.hideBar = (foo.type != "DIMMER");
				o.sendOnRelease = (foo.type != "DIMMER");
				if (client.sliderPopup.bt != parent.e) client.openSliderPopup(parent.address, parent, o);
			} else client.closeSliderPopup(); // close if it was already open
		}
	} // onLongPress()

	// on click
	this.onClick = function () {
		if (interfaceLocked) return;
		clearTimeout(timeoutId);
		client.focus();

		var type = JSONTopo.hsycotopology.devices[parent.address][3];

		if (foo.type != "DIMMER" && foo.type != "HPOS" && foo.type != "VPOS")
			client.closeSliderPopup();

		client.sendCommand(urls.iocommand, parent.address+"=flip");

		clearTimeout(popupTimeoutId);
		if (foo.image) {
			popupTimeoutId = setTimeout(openPopup,popupTimeout);
		} else {
			client.closeThumbnailPopup();
		}
	} // onClick()

	function openPopup() {
		client.openThumbnailPopup(true, parent, foo.image);
	}

	// AUGMENT: parent onShow,onHide events
	parent.onShow = function () {} // do nothing on show
	parent.onHide = function () {
		if (parent.e.btData) // initialized? (in _device, if !JSONTopo.hsycotopology.devices...)
			browser.resetButton(parent.e); // lift without clicking
		for (var id in this.popups) {
			client.closePopup(this.popups[id]); // so popups don't float, we close them
		}
	}

	// set attribute
	this.setAttribute = function (attr,value) {
		_base.setAttribute(attr,value);
	} // setAttribute()

	init();
} // Objects._device class

//container delegate (project, page, container has-a)
Objects._container = function (parent) {
	var foo = this;

	parent.objects = []; // AUGMENT: objects array

	// AUGMENT: extend parent
	parent.addObject = function (o) {
		this.objects.push(o);
		// set container link
		o.container = this;
		// set page link
		if (this.page)
			o.page = this.page;
		else
			o.page = this; // it's a page
	}
	parent.removeObject = function (o) {
		for (var i=0; i<this.objects.length; i++) {
			if (this.objects[i] == o) {
				o.container = null;
				o.page = null;
				this.objects.splice(i,1);
				return;
			}
		}
	}
	parent.removeObjects = function () {
		for (var i=0; i<this.objects.length; i++) {
			this.objects[i].container = null;
			this.objects[i].page = null;
		}
		this.objects = [];
	}

	// show/hide
	this.onShow = function () {
		// call onShow on all page's objects
		for (var i=0; i<parent.objects.length; i++)
			if (parent.objects[i].onShow && parent.objects[i].visible)
				parent.objects[i].onShow();
	}
	this.onAfterShow = function () {
		// call onAfterShow on all page's objects
		for (var i=0; i<parent.objects.length; i++)
			if (parent.objects[i].onAfterShow && parent.objects[i].visible)
				parent.objects[i].onAfterShow();
	}
	this.onHide = function () {
		// call onHide on all page's objects
		for (var i=0; i<parent.objects.length; i++) {
			if (parent.objects[i].onHide && parent.objects[i].visible)
				parent.objects[i].onHide();
		}
	}
} // Objects._container class


// page delegate (page, popup has-a)
Objects._page = function (parent) {
	parent.visible = false; // AUGMENT: visibility

	// on before/after show/hide event, externally assigned
	parent.onBeforeShow = null;
	parent.onBeforeHide = null;
	parent.onAfterShow = null;
	parent.onAfterHide = null;

	// check absolute visibility, containers...
	parent.isVisible = function () {
		return (this.visible && (!this.container || this.container.isVisible()));
	}

	// show/hide
	this.show = function () {
		parent.visible = true;
		document.onkeydown = null; // always reset keydown
		parent.e.style.display = "inline";
	}
	this.hide = function () {
		parent.visible = false;
		parent.e.style.display = "none";
	}
} // Objects._page class