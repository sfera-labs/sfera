# Quick start

If you can't wait to try Sfera, here is how to get a ready-to-use installation:

* Download and unzip [this file](http://sfera.sferalabs.cc/docs/resources/sfera-quick-start.zip) 
* Open your shell/terminal/cmd application, `cd` into the sfera directory and launch Sfera:

    ~ $ cd sfera
    ~/sfera $ sudo java -jar sfera.jar

If you're running into problems, ensure you have all the [requirements installed](installation.html).

This installation includes the [Web App](../apps/cc.sferalabs.sfera.apps.webapp/latest) with two sample interfaces, the [Dummy driver](../drivers/cc.sferalabs.sfera.drivers.dummy/1.0.0/) that triggers some random events, and some control logic examples:
    
After launching Sfera you can access the Web App wIDE at 'http://localhost:8080/wide/' and the two sample interfaces at 'http://localhost:8080/big/' (desktop/tablet interface) and 'http://localhost:8080/small/' (mobile interface) using the credentials:

Username: `admin`   
Password: `admin`   

Explore the file structure, play with the scripts and the interfaces. Then try adding new drivers that you can download from [here](../drivers/).

To start with, you may try the *Telegram* and/or the *Google Calendar* drivers which do not require any hardware, then you may add any of the integrated platform that you own.

Enjoy Sfera!
