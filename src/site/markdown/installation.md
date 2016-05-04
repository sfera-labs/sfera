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

TODO