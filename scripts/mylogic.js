

// local variable
var phoneBook = {3280022333, 3338899777, 9876655444};

globalVar = "blabla";

// predefined callback
function event(e) {
	if (e.node == someNode) {
		if (e.someAttribute == somethig) {
			...
		}
	}

	else if (e.node == gsm && clock.simpleTime > 0800) {
		if (e.name == "sms") {
			if (isInMyPhoneBook(e.sender)) {
				if (e.body.contains("hello") && gsm.signal > 10) {
					gsm.send(e.sender, "hello to you");
				}
			}
		}
	}

	else if (e.node.type == "knx") { // executed for each node of type "knx"
		if (e.datapoint.type == 4 && e.value > 0) {
			log(WARNING, "myTag", "Window " + e.datapoint.name + " is opening");
		}
	}
}

// local function
function isInMyPhoneBook(number) {
	return phoneBook.indexOf(number) >= 0;
}

// knx.1 = 0 && lop.ciao = blue
if ((e.node == knx && e.node == "1" )|| (e.node == lop && e.datapoint == "ciao") {
	if ((e.node == knx && e.value == 0 && lop.ciao == blue) || (e.node == lop && e.value == blue && knx.1 == 0)) {
		...
	}
}

event [: guard] : { ... f(e) ... }

knx.light.1 AND knx.light.1 = 0 AND time > 0800 : ...
knx.light.1 == 0 : time > 0800 : ...

gsm.sms.222555888 = "ciao" : ...

gsm.sms : clock.simpleTime > 0800 : {
	if (isInMyPhoneBook(e.sender)) {
		if (e.body.contains("hello") && gsm.signal > 10) {
			gsm.send(e.sender, "hello to you");
		}
	}
}

knx.light.1 || clock : { ... f(e) ... }





