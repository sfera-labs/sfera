# Sfera

Sfera is an open source framework for the development of smart control systems.     
It enables the integration of heterogeneous technologies (IoT devices, home/building/industrial automation, videocameras, audio/video systems, Web services etc.) and the creation of completely customizable control logic and interfaces.

Sfera is product agnostic, but it respects the peculiarities of each connected field system.     
Rather than fitting the system in a general model, Sfera’s abstraction layer is applied on the system’s properties. This allows for a fine-grained integration, with a common high-level interface to the integrated field systems.

Sfera is based on a clear-sky architecture.    
We like clouds, but we don’t want to depend on them. While perfectly capable of communicating with cloud-based services, Sfera runs on your local server and keeps your installation working even without Internet connection.

The core of Sfera is a modular, highly scalable Java framework. It can be installed on any platform supporting Java, including the Raspberry Pi.

![sfera-architecture](images/sfera-architecture.png)

Sfera can be expanded to integrate any device, protocol or service and you can install or create apps for customized, vertical solutions.    
It embeds a script engine (based on the JavaScript language) which allows to rapidly create your control logic and to modify it during runtime.
Sfera also exposes a REST API and a WebSockets protocol for remote control and configuration through custom apps, Web apps or ad-hoc interfaces.
