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
	this.onRequest = null; // when sending request

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

    /**
     * Initialize
     */
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
		if (foo.onRequest)
			foo.onRequest();
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

var clientID;

window.onload = function () {
	var req = new XMLReq("GET");

	var what = "dictionary";

	req.onLoaded = function () {
		var e = document.getElementById("contents");
		switch (what) {
		case "dictionary":
			console.log("dictionary loaded");
			what = "index";
			e.innerHTML += "dictionary loaded:<br><textarea style='width:500px; height:200px'>"+req.getResponseText()+"</textarea><br><br>";
			req.open("/"+interf+"/index.xml",100);
			break;
		case "index":
			console.log("index loaded");
			e.innerHTML += "dictionary index:<br><textarea style='width:500px; height:200px'>"+req.getResponseText()+"</textarea><br><br>";
			what = "subscribe";
			e.innerHTML += "<br><br>Subscribing...";
			req.open("/x/subscribe?nodes=*",100); // first subscribe
			break;
		case "subscribe":
			console.log("subscribe loaded");
			what = "state";
			var json = JSON.parse(req.getResponseText());
			clientID = json.id;
			e.innerHTML += "Done<br><br>ID:"+clientID+"<br><br>"; 
			req.open("/x/status/"+clientID+"?ts="+(new Date()).getTime(),100);
			break;
		case "state":
			console.log("status loaded");
			e.innerHTML += "status:<br><textarea style='width:500px; height:200px'>"+req.getResponseText()+"</textarea><br><br>";
			req.open("/x/status/"+clientID+"?ts="+(new Date()).getTime(),100);
			break;
		}
	}
	
	req.onError = function () {
		console.log("error");
		var e = document.getElementById("contents");
		e.innerHTML += "<br><br>Error.<br><br>";
	}

	req.open("/"+interf+"/dictionary.xml",100);
}

var Objects = {};