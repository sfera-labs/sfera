# Remote API

Sfera exposes a remote HTTP and WebSocket API to monitor and control the system state and its nodes.
The API is available at the "/api" path of the Web server on the HTTP/HTTPS ports defined in the [system configuration](configuration.html#System).

Before using the remote API make sure to [configure the remote access](configuration.html#Remote_access).

The communication flow follows these steps:

* [Login](#Login): the client sends its credentials and initiates a session
* [Connection](#Connection): the client can establish several connections under the same session 
* [Subscription](#Subscription): each connection can subscribe to a set of nodes to monitor
* [State](#State): the client receives the updates from its subscriptions
* [Events](#Events): the client can trigger events on the server
* [Commands](#Commands): the client can perform commands on nodes
* [Logout](#Logout): the client ends the session

**N.B.** The following HTTP requests include parameters as query strings for GET requests; you can equally POST those parameters. Don't forget to URL-encode when needed.

### Login

To begin a remote API session you need to authenticate. To this end, perform a request to:

    /api/login?user=<username>&password=<p4ssw0rd>

If the authentication is successful the response will be the following JSON object:

    { "result": "ok" }

and the HTTP response will contain a "Set-Cookie" header with a "session" attribute to be included in the subsequent requests as a cookie.

If the login fails an error will be returned:

    { "error": <error_message> }

### Logout
To invalidate the current session send a request to:

    /api/logout
    
and wait for a successful response:

    { "result": "ok" }


### Connection
##### HTTP
To open a connection perform the following request:

    /api/connect

The response will contain your connection ID, for instance:

    { "cid": "766" }

This parameter will be used in all the subsequent requests to identify the connection

##### WebSocket
If you are going to use WebSocket messages, after login, you need to open a WebSocket connection to:

    ws://<host>:<http_port>/api/websocket
    
or, if using TLS/SSL:

    wss://<host>:<https_port>/api/websocket

Upon connection the server will send this message:

    {
        "type": "connection",
        "connectionId": <cid>,
        "pingInterval": <interval>,
        "responseTimeout": <timeout>
    }

The values of `pingInterval` and `responseTimeout` will correspond respectively to the [configuration parameters](configuration.html#Parameters) `ws_ping_interval` and `ws_response_timeout`.

After connection the server will send ping messages with the specified time interval and will expect a pong response within the specified timeout.

The client can use these parameters to monitor the responsiveness of the server.

Both ping and pong messages consist in single `&` characters.

The `connectionId` parameter identifies the created connection and must be used only in case you need to reestablish the connection after a communication error. To this end add the `cid` parameter to the connection request:

    ws://<host>:<http_port>/api/websocket?cid=<cid>

### Subscription
To monitor the system state you must send a subscription request specifying what you are interested in.

The request contains the specification of the nodes you want to subscribe to: use the wildcard `*` if you want to receive the complete system state or specify a list of nodes IDs separated by `;`. You can also use the syntax `foo.*` to receive all the nodes whose ID starts with `foo.`.

##### HTTP
Send a request to:

    /api/subscribe?cid=<cid>&nodes=<specification>

and wait for a successful response:

    { "result": "ok" }

##### WebSocket
Send a subscription message:

    {
        "action": "subscribe",
        "tag": <random_id>,
        "nodes": <specification>
    }

A successful response message will be sent back:
    
    {
        "type": "reply",
        "action": "subscribe",
        "tag": <tag_sent_by_client>,
        "result": "ok"
    }

### State
##### HTTP
After a subscription request you can poll the state of the nodes performing a request to:

    /api/state?cid=<cid>&ack=<ack_val>&timeout=<poll_timeout>

The response will include a set of all the node events that have occurred since the last acknowledged state request.

The `ack` parameter is a numerical value used to acknowledge the reception of the previous state. A value greater than the one included in the last request represents an acknowledgment.    
If a request fails, just send another state request with the same `ack` value, otherwise, increment its value for the next request.

The `timeout` parameter specifies how many seconds the server must wait before responding to the request if there is no state update to be sent. The server will send a response as soon as there is a new update available or the timeout has expired.   
A request with a `timeout` value greater than zero will therefore behave as a long-polling request, while if `0` is specified or the `timeout` parameter is omitted the request will return immediately in any case.

The response body will be a JSON object with the following structure:

    {
        "result": {
            "<event_id_1>": <val_1>,
            "<event_id_2>": <val_2>,
            ...
            "<event_id_N>": <val_N>
        }
    }

##### WebSocket
After subscribing, the server will send asynchronous messages every time there are events from the subscribed nodes. The messages will have the following structure:

    { 
        "type": "event", 
        "nodes": { 
            "<event_id_1>": <val_1>,
            "<event_id_2>": <val_2>,
            ...
            "<event_id_N>": <val_N>
        }
    }

### Events
To trigger general events to be handled by your control logic, perform an event request.
This will result in an event being posted on the system bus with ID `http.<event_id>` and a `String` value corresponding to the specified one.

The generated event will be an instance of the `RemoteApiEvent` class holding a reference to the request that generated the event.

##### HTTP

    /api/event?cid=<cid>&id=<event_id>&value=<event_value>

the server will reply:

    { "result": "ok" }

##### WebSocket

    { 
        "action": "event", 
        "tag": <random_id>, 
        "id": "<event_id>", 
        "value": <event_value>
    }

the server will reply:
    
    {
        "type": "reply",
        "action": "event", 
        "tag": <tag_sent_by_client>,
        "result": "ok"
    }

##### Example

Here is an example of an event request and a script handling it:

_HTTP Request:_

    /api/event?foo=200

_WebSocket Request:_

    { 
        "action": "event", 
        "tag": 235448, 
        "id": "foo", 
        "value": "200"
    }

_Script:_

    http.foo == "200" : {
        if (_e.user.isInRole("admin")) {
            // do something...
        }
    }

### Commands
To execute an action on a node instance (e.g. call a method of a driver) send a command request.

The specified command corresponds to a method call on a node with the same syntax used for [scripts](scripts.html#Drivers).

It is possible to use the variables `httpRequest` and `connectionId` as method parameters. They will be respectively bound to the `HttpServletRequest` object created to handle the request and to the `String` corresponding to the connection ID.

##### HTTP

    /api/command?cid=<cid>&cmd=<command>

Response:

    { "result": <value_returned_by_command> }

##### WebSocket

    { 
        "action": "command", 
        "tag": <random_id>, 
        "cmd": "<command>"
    }

Response:

    {
        "type": "reply", 
        "action": "command", 
        "tag": <tag_sent_by_client>, 
        "result": <value_returned_by_command>
    }

##### Example
_HTTP:_

```
/api/command?cid=766&cmd=myLights.light(2).setLevel(50)
```

```
/api/command?cid=766&cmd=myNode.doSomething(httpRequest, connectionId)
```

_WebSocket:_

```
{ 
    "action": "command", 
    "tag": 1010101, 
    "cmd": "myLights.light(2).setLevel(50)"
}
```

```
{ 
    "action": "command", 
    "tag": 1010101, 
    "cmd": "myNode.doSomething(httpRequest, connectionId)"
}
```

_Script for `myNode`:_

    var myNode = new ScriptNode("myNode");
    myNode.doSomething = function(req, cid) {
        log.info("Hi, " + req.remoteUser + ". Your connection ID is: " + cid);
        return "All good";
    };
