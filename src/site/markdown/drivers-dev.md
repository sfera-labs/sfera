# Drivers development

After [setting up your workspace](workspace-setup.html) for the development of a driver, you will have a project with the following structure (with names of packages and files set to your choices):

![driver-proj-structure](images/driver-proj-structure.png)

## Lifecycle

The class `MyDriver` is the starting point of your driver implementation and contains all the callback methods called during the driver lifecycle:

```Java
public class MyDriver extends Driver {

	public MyDriver(String id) {
		super(id);
	}

	@Override
	protected boolean onInit(Configuration config)
			throws InterruptedException {
		return false;
	}

	@Override
	protected boolean loop() throws InterruptedException {
		return false;
	}

	@Override
	protected void onQuit() {

	}

}
```

The constructor `MyDriver(String id)` is called only once when the drivers are instantiated during the startup phase of Sfera. No logic should be added here. The `id` parameter is set to the ID assigned to the driver instance in the configuration. It can be retrieved calling the `getId()` method.

After creation, the driver instance will follow this lifecycle:

![driver-lifecycle](images/driver-lifecycle.png)

* `onInit()`

   This method is called when starting the driver. Here you should read the driver configuration (`config` parameter) which may contain parameters such as the IP address of the field system, a user account name, or any other info needed for the system integration.   
Then you may initialize the components and data structures of your driver and try to establish a connection with the field system.   
After a successful initialization your implementation should return `true` to proceed to the `loop()` method.   
If the initialization fails (e.g. cannot connect to the field system), return `false`. The system will call `onQuit()` and then try the initialization again.

* `loop()`

   After a successful initialization, this method is called continuously as long as it returns `true`. Here you can poll the field system for state changes or wait for push notifications from it.   
Between consecutive loop calls there is no delay, so make sure to release CPU by means of thread sleeping or by executing non-busy waiting operations, to avoid having your driver consume all the CPU resources.   
If something goes wrong during your loop, simply return `false`: the driver will quit and restart from initialization.

* _`quit`_

   While your driver is running (i.e. while executing `onInit()` or `loop()`), the system may want to stop it (e.g. because of a user command). This is accomplished by sending an interrupt to the driver's thread. As you can see these callbacks can throw `InterruptedException`s, this is because in your implementation you should avoid catching them and let your driver have a graceful shutdown.   
After interruption `onQuit()` is called and the driver will remain in a _`quitted`_ state until restarted by a _`start`_ command.

* `onQuit()`

   Here you should cleanup and release your resources to ensure a graceful shutdown. After the termination of this method, the driver should be able to restart as if it was just instantiated.


There is another callback method that you can optionally override:

```Java
@Override
protected void onConfigChange(Configuration config) {
	
}
```

This method is called when a change in the driver configuration file has been detected.   
If not overridden, the default implementation will quit and restart the driver.

Try out the lifecycle of your driver: add a configuration file called `mydriver.yml` in the "config/drivers" directory with the following content:


    type: com.example.sfera.drivers.mydriver.MyDriver
    param: foo

where the value of "type" is the fully-classified name of your driver class and "param" is just a test parameter that we are going to read.   
Use the following implementation for your driver:

```Java
public class MyDriver extends Driver {

	public MyDriver(String id) {
		super(id);
	}

	@Override
	protected boolean onInit(Configuration config) 
			throws InterruptedException {
		log.info("onInit");
		String param = config.get("param", null);
		log.info("param = " + param);
		return true;
	}

	@Override
	protected boolean loop() throws InterruptedException {
		log.info("loop");
		Thread.sleep(2000);
		return true;
	}

	@Override
	protected void onQuit() {
		log.info("onQuit");
	}
}
```

Now start Sfera (menu **Run > Run History > Sfera-MyDriver**) and check the consolle. You will see your driver being initialized and then it will start looping.    
To stop Sfera type `sys quit` in the console and hit <kbd>Enter</kbd>; you will see that your driver will be quitted before shutdown.

## Events

To generate events from your driver you should instantiate [Event](apidocs/cc/sferalabs/sfera/events/Event.html) objects and post them to the system [Bus](apidocs/cc/sferalabs/sfera/events/Bus.html).

As you can see in the above project structure, in the sub-package `.events` there is a `MyDriverEvent` interface that extends the `Event` interface.   
This is a simple tag interface that should be implemented by all your event classes so that applications can easily register to all of your driver events.

Create a different class for any type of event your driver is going to generate and let them extend the standard event classes provided:

* [BooleanEvent](apidocs/cc/sferalabs/sfera/events/BooleanEvent.html): for events that can be represented by a boolean value (`true` or `false`); for instance, the "on" state of a light.
* [NumberEvent](apidocs/cc/sferalabs/sfera/events/NumberEvent.html): for events with numerical value, e.g. a temperature read by a thermostat.
* [StringEvent](apidocs/cc/sferalabs/sfera/events/StringEvent.html): for events whose value needs a textual representation, e.g. the name of the currently playing track of an audio system

If none of the above classes fits your event, extend the [BaseEvent](apidocs/cc/sferalabs/sfera/events/BaseEvent.html) class which is the simplest implementation of the `Event` interface.

Events are uniquely identified by an ID, this means that you could virtually have a single event class and instantiate it with different IDs. It is anyhow recommended to have several classes (or even interfaces) to group your events into different categories so that applications can more easily register to a subset of your driver's events.

Here are some examples of event classes for your driver:

```Java
package com.example.sfera.drivers.mydriver.events;

import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.events.NumberEvent;

public class MyNumberEvent extends NumberEvent 
		implements MyDriverEvent {

	private static final String MY_NUMBER_EVENT_ID = "num";

	public MyNumberEvent(Node source, Double value) {
		super(source, MY_NUMBER_EVENT_ID, value);
	}
}
```

```Java
package com.example.sfera.drivers.mydriver.events;

import cc.sferalabs.sfera.events.Node;
import cc.sferalabs.sfera.events.BooleanEvent;

public class MyLightEvent extends BooleanEvent 
		implements MyDriverEvent {

	public MyLightEvent(Node source, int num, Boolean on) {
		super(source, "light(" + num + ").on", on);
	}
}
```

Now you can post your event from your driver class to the system `Bus`:

```Java
Bus.post(new MyNumberEvent(this, 7.8));
Bus.post(new MyLightEvent(this, 2, true));
```

This will result in triggering the events:

```
mydriver.num = 7.8
mydriver.light(2).on = true
```

If you want your event to be posted only if its value changed from the last time it has been posted, use:

```Java
Bus.postIfChanged(new MyNumberEvent(this, 7.8));
```

As you can see, you shall always pass a reference to your driver instance (`this`) as the source node of the events so that applications handling them can get this reference and possibly call methods to issue commands for your driver.

### State events

Sfera will take care of generating some events for your driver that represent the current state in the life-cycle of the driver.

These events will have ID `<driver_id>.driverState` and the following String values:

* `init`: when the driver is being initialized, i.e. just before `onInit()` is called
* `running`: when the driver has been successfully initialized and the `loop()` cycle is starting
* `quit`: when the driver is about to get quitted, i.e. just before `onQuit()` is called

These events objects will implement the [DriverStateEvent](apidocs/cc/sferalabs/sfera/drivers/DriverStateEvent.html) tag interface and, if the driver includes a general interface for the driver's events using the name convention `<driver_package>.events.<driver_class>Event` described above (e.g. the interface `com.example.sfera.drivers.mydriver.events.MyDriverEvent`), they will dynamically implement this interface too.

## Commands

Your driver class should expose public methods that applications can call to issue commands to the field system or to perform any other operation on your driver.

For instance, you could add these methods:

```Java
public double getNum() {
	return myNumber;
}

public void setNum(double num) {
	myNumber = num;
}

public boolean setLight(int num, boolean on) {
	if (sendLightCommand(num, on)) {
		return true;
	} else {
		return false;
	}
}
```

Note that when writing scripts logic the script engine will treat your `getXY()` and `setXY(value)` methods as properties too:

```JavaScript
var x = mydriver.getNum();
// can be written as:
var x = mydriver.num; 

mydriver.setNum(7.3);
// can be written as:
mydriver.num = 7.3;
```

The `setLight()` method can be redesigned to allow a syntax that resembles the light events we saw above:

```JavaScript
mydriver.light(2).on = true;
```

to this end, simply create a method `light(int num)` which returns an object that has a public `setOn(boolean val)` method.

Your driver is now ready to be deployed. Use maven to build your jar plugin and test it in a Sfera installation dropping it in the "plugins" directory.

## Persistent data

If your driver needs to persist some data (a database, a property file, an image, ...) use as root directories the paths returned by:

* `getDriverGlobalDataDir()`: for data accessible to every instance of your driver
* `getDriverInstanceDataDir()`: for data relative to a single instance of your driver 


