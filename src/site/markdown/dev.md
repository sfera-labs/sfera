# Developing with Sfera

It is possible to extend Sfera's capabilities by adding plugins to your installation.

Plugins are bundles that can contain drivers and/or apps.

Drivers are the bridge between Sfera and the field devices. A field device can be, for instance, an IoT product, a security system, a BUS network or even something more abstract as weather information gathered from the Internet.    
A driver implements the communication protocol of a specific field device and generates events when the state of the device changes and/or provides methods to send commands to change the state of the device.

Apps are the logical connection between driver instances, Sfera components and/or user interfaces. Apps implement the logic that reacts to events and send commands to drivers and can, in turn, generate logical events for other apps to handle.

To develop your own plugins [setup your workspace](workspace-setup.html) and then refer to the [driver](drivers-dev.html) or [app](apps-dev.html) development sections for implementation details.