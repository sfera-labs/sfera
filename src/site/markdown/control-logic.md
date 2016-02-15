# Control logic

Each component of Sfera's architecture (driver instances, apps, core services, ...) constitutes a _**node**_ of the framework.   
Nodes may generate _**events**_ and send them on a common _**bus**_ which dispatches such events to the _**listeners**_ that subscribed for specific events.

Nodes are identified by unique IDs and they are the top of a hierarchical structure of _**sub-nodes**_ representing their modules or properties. Events are identified by IDs representing the node/sub-node that generated them and a **_value_**.

Let's illustrate this concept with an example:    
Consider a driver for a lighting system able to address several lights, control their dimming level and monitor the health of the bulbs.   
An instance of this driver (with ID `myLights`) can be represented by this tree:


    - myLights
        - light1
            - level (0-100)
            - healthy (true/false)
    
        ...
    
        - light<N>
            - level (0-100)
            - healthy (true/false)


Whenever the dimming level of a light changes, the driver will generate an event whose ID will be, for instance, `myLights.light(1).level` and its value an integer corresponding to the new dimming level.

The value of an event can be any (Java) object with its own properties.

You can write custom control logic triggered by these events by:

* [Writing scripts](scripts.html): to quickly and efficiently create logic modifiable on the fly
* [Developing apps](apps-dev.html): to write high performance code that extends the core functionalities of Sfera

These two options provide for a flexible approach to address different problems with different solutions.    
An experienced Java developer may decide to code all its custom logic in apps, but will most likely appreciate the convenience of scripts for some tasks.    
On the other hand, if software development is not your business, scripts will definitely be your weapon of choice, and will offer you all the tools you need for creating any kind of customization.    
In any case, scripts and apps are fully interoperable and integrated, you can seamlessly combine them at your convenience.