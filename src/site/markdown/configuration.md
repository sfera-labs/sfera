# Configuration

Sfera configuration files are [YAML](http://yaml.org/) files containing a map of key-value pairs corresponding respectively to parameters' names and their values.

## System
The system configuration file is `config/sfera.yml`

### Parameters

| Parameter | Value type | Default value | Description |
| --------- | ---------- | ------------- | ----------- |
| `http_port` | Integer |  | If set the Web server will accept HTTP connections on the specified port. The value must be an available port number on the server |
| `https_port` | Integer |  | If set the Web server will accept HTTPS connections on the specified port. The value must be an available port number on the server. Enabling HTTPS requires a SSL key store file for the certificate to be placed in `data/http/sfera.keys`. If this file is not found, a self-signed certificate will be automatically generated |
| `keystore_password` | String | | If HTTPS is enabled and a custom certificate is used (`data/http/sfera.keys`), this parameter must be set to the key store password |
| `keymanager_password` | String | | If HTTPS is enabled and a custom certificate is used (`data/http/sfera.keys`), this parameter must be set to the password (if any) for the specific key within the key store |
| `http_max_threads` | Integer | 128 * _CPUs_ | Max number of threads created by the thread pool used by the Web server to process requests. The default value is equal to 128 times the number of processors available to the Java virtual machine |
| `http_min_threads` | Integer | 8 | Min number of threads kept ready by the thread pool used by the Web server to process requests |
| `http_threads_idle_timeout` | Integer | 60000 | Max thread idle time in milliseconds. Threads that are idle for longer than this period may be stopped |
| `http_session_max_inactive` | Integer | 3600 | Max period of inactivity, after which a session is invalidated, in seconds |
| `ws_ping_interval` | Integer | 10000 | Time interval in milliseconds for ping messages in WebSocket connections |
| `ws_response_timeout` | Integer | 5000 | Max waiting time in milliseconds for WebSocket responses after which the connection is closed by the server |
| `console_telnet_port` | Integer |  | If set the [Telnet console](console.html) will be enabled on the specified port. The value must be an available number port on the server |

## Users and access control
Sfera defines access rules based on user authentication and roles-based authorization. Each user is identified by a **username**, has a **password** and optional **roles**.

Sfera requires the user to have the role "admin" for some services, such as console access and some restricted remote API.

Installed apps can introduce custom roles for authorization purpose; for instance, the [Web App](../apps/cc.sferalabs.sfera.apps.webapp/latest) adds roles for restricting access to interfaces.

To add a user use the [console command](console.html) `access add`:

    access add <username> <password> [<role1> <role2> ... <roleN>]

for instance:

    access add john Ch4ng3M3 admin

To remove an existing user use the [console command](console.html) `access remove`:

    access remove <username>
    
For other commands:

    help access

## Drivers

Drivers configuration files are contained in the directory `config/drivers/`. Each file corresponds to an instance of a driver whose ID is set to the name of the file (without `.yml` extension).   
Driver instances IDs must be valid JavaScript variable names, we recommend using only lower and upper case simple letters ([a-zA-Z]), numbers ([0-9]) and underscores (_).

The configuration parameters of a driver are described in the driver documentation.

The only parameter in common to every driver which must be always included is `type`, whose value must be set to the fully-qualified name of the driver class.

Example:

    type: com.example.sfera.drivers.mydriver.MyDriver

There can be multiple instances of drivers of the same type.

## Apps

Apps configuration files are contained in the directory `config/apps/`. Each file corresponds to an installed app and the name of the file must correspond to the fully-qualified name of the app class (plus the `.yml` extension).

The configuration parameters of an app are described in the app documentation.

There cannot be multiple instances of the same app.

## Logging
Sfera uses [Log4j 2](http://logging.apache.org/log4j/2.x/) as logging framework. The default configuration creates a log file in the `logs` directory called `sfera.log`.   
The file is daily cleared after being compressed and saved in the directory `logs/<year>/` with the name `sfera-<year>-<month>-<day>.log.gz`.

The default configuration corresponds to the following log4j2 XML configuration:

```XML
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <RollingRandomAccessFile name="SferaLog" fileName="logs/sfera.log" 
                filePattern="logs/%d{yyyy}/sfera-%d{yyyy-MM-dd}-%i.log.gz">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} %level %thread %logger: %msg%n" />
            <TimeBasedTriggeringPolicy />
        </RollingRandomAccessFile>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="SferaLog" />
        </Root>
    </Loggers>
</Configuration>
```

It is possible to override this configuration by adding a file named `log4j2.xml` in the `config` directory with your customized configuration.