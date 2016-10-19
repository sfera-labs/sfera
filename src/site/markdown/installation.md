# Install Sfera

The installation of Sfera simply consists of downloading [Sfera's jar file](downloads.html) and adding the needed configuration files and plugins.

## Requirements

Sfera requires Java Runtime Environment (JRE) 8. If you don't have it already, download it from http://www.oracle.com/technetwork/java/javase/downloads/index.html and install it on your platform. Make sure the `JAVA_HOME` environment variable is properly set.

## Files structure

A sfera installation has the following file structure:

![installation-files-struct](images/installation-files-struct.png)

The `config` directory contains a YAML file for system configuration (`sfera.yml`), a directory for 
driver instances configuration (`config/drivers`) and one for apps (`config/apps`).    
Each driver instance has a separate YAML configuration file (`driver1.yml`, `driver2.yml`), there can be several instances of the same driver type.    
Apps have a single configuration file for type (`com.example.app1.yml`), since there cannot be more instances of the same app. More details on this in [Configuration](configuration.html).

The `plugins` directory is where you drop your plugins jars containing drivers and/or apps.

In the `scripts` folder you can add events files containing your control logic; there can be subfolders to create different scopes (more on this in [Scripts](scripts.html)).

The `logs` directory is the default location for log files. See details in [Configuration](configuration.html#Logging).

`data` is where the system, drivers and applications persist their data.

## Run Sfera

From the installation directory `sfera` run the command:

    java -jar sfera.jar

Type `sys quit` to interrupt Sfera.

### Auto-start

#### systemd

[systemd](https://freedesktop.org/wiki/Software/systemd/) is an init system used in most Linux distributions to bootstrap the user space and manage all processes subsequently. On your Raspberry Pi, you might want to use this method.

Create a start-up script in your sfera installation directory:

    cd <sfera_installation_dir>
    nano start-up.sh
    
and paste in the following content:

    #!/bin/bash
    STDOUT_LOG_DIR=logs/
    STDOUT_LOG_FILE=out.log
    
    STDOUT_LOG_PATH=$STDOUT_LOG_DIR$STDOUT_LOG_FILE
    
    cd "$( dirname "$0" )"
    mkdir -p $STDOUT_LOG_DIR
    echo "=======================================" >> $STDOUT_LOG_PATH
    date >> $STDOUT_LOG_PATH
    echo "---------------------------------------" >> $STDOUT_LOG_PATH
    cp sfera.jar .sfera.jar
    java -jar .sfera.jar >> $STDOUT_LOG_PATH 2>&1
    
When this script is run it will:

* create a log record in `logs/out.log` with the current date and time
* make a copy of sfera's jar and run the new copy. This is useful in case a new version of the jar is uploaded while sfera is running (e.g. using the WebApp file manager); the current version will continue running until restarted, which is when the new jar will be run.
* redirect any eventual output (standard output and standard error) to `logs/out.log`

Make this script executable:

    sudo chmod +x start-up.sh
    
Now create a new systemd service called `sfera`:

    sudo nano /etc/systemd/system/sfera.service
    
and paste in this:

    [Unit]
    Description=Sfera
    
    [Service]
    Type=simple
    ExecStart=/home/pi/sfera/start-up.sh
    Restart=always
    
    [Install]
    WantedBy=multi-user.target
    
Make sure to replace `/home/pi/sfera/start-up.sh` with the path of your start-up script previously created.

Enable the service:

    sudo systemctl enable sfera.service
    
On reboot sfera will be automatically started and it will be restated any time the process quits.

You can use:

    sudo systemctl start sfera
    sudo systemctl stop sfera
    
to respectively start and stop sfera manually, and:

    sudo systemctl status sfera
    
to check the service state.

In case you want to disable sfera's service:

    sudo systemctl disable sfera