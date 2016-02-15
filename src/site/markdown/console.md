# Console

The console provides for interacting with Sfera's runtime. You have commands to start/quit drivers, commands to manage user authorizations and so on.

If you ran Sfera from shell, commands will be read from standard input. 

Additionally, you can instantiate a Telnet connection to Sfera's IP address on the port specified by the `console_telnet_port` [configuration parameter](configuration.html#Parameters); after authenticating you'll be able to interact with the console:

```
$ telnet localhost 7777
Trying ::1...
Connected to localhost.
Escape character is '^]'.
User:
admin
Password:

Granted - Input your commands:
```

Installed apps/drivers may add additional ways to interact with the console.

Following is a list of some of the available console commands to control Sfera's lifecycle; other commands are described in other sections of this documentation relatively to the command purpose.

* `help`: lists the available command handlers
* `help <handler>`: lists the available commands for the specified handler
* `sys quit`: gracefully stops all Sfera processes
* `sys kill`: abruptly terminates the Java Virtual Machine running Sfera with status code '1'
* `sys state <id>`: prints the current state of the nodes. if `<id>` is not specified, all nodes are printed; if you specify an ID, only the state of the matching nodes will be printed; `<id>` can contain one wildcard '\*' character: `xxx*` prints all nodes whose ID starts with "xxx", `*yyy` prints all nodes whose ID ends with "yyy", `xxx*yyy` prints all nodes whose ID starts with "xxx" and ends with "yyy".
* `drivers quit <driver_id>`: gracefully stops the specified driver instance
* `drivers start <driver_id>`: starts the specified driver instance
* `drivers restart <driver_id>`: restarts the specified driver instance after a graceful quit
* `script eval { <script> }`: evaluates the specified script code
* `script eval <file> <line_num>`: evaluates the action of the rule defined in the specified file (relative to the scripts directory) at the specified line number. Note that the trigger event variable (`_e`) will be `null`.
