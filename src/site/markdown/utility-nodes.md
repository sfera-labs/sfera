# Utility nodes

Sfera comes with a set of nodes that provide utility functions for common use tasks. Such nodes are by default bound to a variable with the same name in the global scope of scripts and can be retrieved in your Java apps via the `Nodes.get(id)` method.


## System

The `system` node simply generates some handy events:

<table>
  <tr>
    <th>Event ID</th>
    <th>Java class</th>
    <th>Value</th>
    <th>Description</th>
  </tr>
  <tr>
    <td rowspan="3">system.state</td>
    <td rowspan="3"><a href="apidocs/cc/sferalabs/sfera/core/events/SystemStateEvent.html">SystemStateEvent</a></td>
    <td>"start"</td>
    <td>Sfera has started</td>
  </tr>
  <tr>
    <td>"ready"</td>
    <td>Sfera has loaded all the modules</td>
  </tr>
  <tr>
    <td>"quit"</td>
    <td>Sfera id about to stop</td>
  </tr>
  <tr>
    <td>system.plugins</td>
    <td><a href="apidocs/cc/sferalabs/sfera/core/events/PluginsEvent.html">PluginsEvent</a></td>
    <td>"reload"</td>
    <td>Plugins have been reloaded</td>
  </tr>
</table>

## Scheduler

The `scheduler` node exposes a set of methods to schedule events to be triggered. Refer to the [Scheduler class JavaDoc](apidocs/cc/sferalabs/sfera/time/Scheduler.html) for the complete list of available methods.

The scheduler will trigger events which are instances of the class [SchedulerEvent](apidocs/cc/sferalabs/sfera/time/SchedulerEvent.html) with the ID (prefixed by `scheduler.`) and the value specified when scheduled. 

Example:

```
system.state == "ready" : {
    scheduler.repeat("hello", "world", 2000, 1000);
    scheduler.repeat("hello", "moon", 3000, 2000);
}

scheduler.hello : {
	// ...
}

scheduler.hello == "world" : {
	// ...
}

scheduler.hello == "moon" : {
	// ...
}
```

## Database

The `db` node exposes a set of methods to persist and retrieve data in Sfera's database. This node is linked to a [Database](apidocs/cc/sferalabs/sfera/data/Database.html) instance which uses the key/value model to store/retrieve data, where both the key and the value are Strings.

This node is meant to be used to save your control logic state between system reboots.

Example:

```
system.state == "ready" : {
    var foo = db.get("foo");
}

some.event : {
	db.set("foo", _e.value);
}
```