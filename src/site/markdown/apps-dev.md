# Apps development

After [setting up your workspace](workspace-setup.html) for the development of an app, you will have a project with the following structure (with names of packages and files set to your choices):

![app-proj-structure](images/app-proj-structure.png)

## Lifecycle

The class `MyApp` is the starting point of your implementation and contains all the callback methods called during the app lifecycle:

```Java
public class MyApp extends Application {

	@Override
	protected void onEnable(Configuration config) {

	}

	@Override
	protected void onDisable() {

	}

}
```

As you can see there are only two callbacks:

* `onEnable()`

   This method is called when the app is started. Here you should read the app configuration (`config` parameter) and initialize components and data structures for your app.   


* `onDisable()`

   Here you should cleanup and release the resources used by your app to ensure a graceful shutdown.


There is another callback method that you can optionally override:

```Java
@Override
protected void onConfigChange(Configuration config) {
	
}
```

This method is called when a change in the app configuration file has been detected.   
If not overridden, the default implementation will quit and restart the app.

Try out the lifecycle of your app: add a configuration file called `com.example.sfera.apps.myapp.MyApp.yml` (i.e. the fully-classified name of your app class with extension ".yml") in the "config/apps" directory with the following content:

```
param: foo
```

Use the following implementation for your app:

```Java
public class MyApp extends Application {

	@Override
	protected void onEnable(Configuration config) {
		log.info("onEnable");
		String param = config.get("param", null);
		log.info("param = " + param);
	}

	@Override
	protected void onDisable() {
		log.info("onDisable");
	}

}
```

Now start Sfera (menu **Run > Run History > Sfera-MyApp**) and check the console. You will see your app being enabled.    
To stop Sfera type `sys quit` in the console and hit <kbd>Enter</kbd>; you will see that your app will be disabled before shutdown.

## Events handling

In most cases, the main part of an application is to create logic reacting to events. To this end your application needs to subscribe to the classes of events it is going to handle.

To subscribe to a specific event type add a method to your class like this:

```Java
import com.google.common.eventbus.Subscribe;

public class MyApp extends Application {

	@Override
	protected void onEnable(Configuration config) {
		log.info("onEnable");
		String param = config.get("param", null);
		log.info("param = " + param);
	}
	
	@Subscribe
	public void logSystemState(SystemStateEvent event) {
		log.info("SystemStateEvent: {} = {}", event.getId(), event.getValue());
	}

	@Override
	protected void onDisable() {
		log.info("onDisable");
	}

}
```

The above example will add a log entry any time there is a `SystemStateEvent` event.    

You can add as many events-handling methods as you need, they all need to be `public void`, marked with the `@Subscribe` tag and have a single parameter whose type determines the classes of events the method is going to receive.    

The parameter type must be of a class or interface extending the `Event` interface. An events-handling method whose parameter has type `X` will receive all the events whose types extends/implements `X`. Therefore, if you use `Event` as type for you method, it will receive all the posted events.    

If you run Sfera using the above implementation of your app you will see an entry log reporting `SystemStateEvent: system.state = ready` as soon as Sfera will finish to initialize all its components.

## Nodes

Applications are not nodes themselves, but they can instantiate nodes that expose methods and can be used as sources for events.    

For instance, you can add a `MyNode` class to your package and use this implementation:

```Java
public class MyNode extends Node {

	public MyNode() {
		super("myNode");
	}

	public String bar(String foo) {
		return foo + "bar";
	}

}
```

When instantiated (i.e. when calling `new MyNode()`) this node will be added to the list of all the globally accessible nodes. You will be able to call its public methods from scripts:

```JavaScript
system.state == 'ready' : {
	log.info(myNode.bar("foo"));
}
```

or recall it from other Java applications:

```Java
MyNode myNode = (MyNode) Nodes.get("myNode");
log.info(myNode.bar("foo"));
```

Moreover, you can use this node from your app as source for custom events.   
For instance, create your event class:

```Java
public class MyNodeEvent extends StringEvent {

	public MyNodeEvent(MyNode source, String id, String value) {
		super(source, id, value);
	}
	
}
```

(You can find more details about creating events [here](drivers-dev.html#Events).)

and modify your app as follows:

```Java
public class MyApp extends Application {

	private MyNode node;

	@Override
	protected void onEnable(Configuration config) {
		log.info("onEnable");
		node = new MyNode();
		Bus.post(new MyNodeEvent(node, "state", "enabled"));
	}

	@Override
	protected void onDisable() {
		log.info("onDisable");
		if (node != null) {
			node.destroy();
		}
	}

}
```

The node is created when the app is enabled, an event is triggered to signal the 'enabled' state of the app and the node is eventually destroyed when the app is disabled.

## Persistent data

If your app needs to persist some data (a database, a property file, an image, ...) use the path returned by `getAppDataDir()` as root directory.