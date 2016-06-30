# Workspace setup

## Install JDK 8
* Download and install the latest version of the Java SE Development Kit 8 (JDK) from http://www.oracle.com/technetwork/java/javase/downloads/index.html

## Install Eclipse
* Download and install the latest version of the Eclipse IDE for Java Developers from https://www.eclipse.org/downloads/
* Make sure the version you installed includes support for Maven, otherwise install a newer version or install the [M2Eclipse plugin](http://eclipse.org/m2e/)

## Install Maven (optional)
If you prefer using Maven via command line, install it:

* Download Maven 3 from https://maven.apache.org/download.cgi
* Extract the archive (e.g. apache-maven-3.3.3-bin.tar.gz) in the directory you want to install it (e.g. /usr/local/apache-maven)
* Set the JAVA_HOME, M2_HOME, M2 and PATH environment variables, for instance:    
  `JAVA_HOME=$(/usr/libexec/java_home -v 1.8)`    
  `M2_HOME=/usr/local/apache-maven/apache-maven-3.3.3`    
  `M2=$M2_HOME/bin`    
  `PATH=$M2:$PATH`    
    * On Mac:
        * open Terminal
        * `cd ~`
        * `nano .bash_profile`
        * paste in:    
          `export M2_HOME=/usr/local/apache-maven/apache-maven-3.3.3`    
          `export M2=$M2_HOME/bin`    
          `export PATH=$M2:$PATH`    
          `export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)`    
        * exit and save: <kbd>Ctrl</kbd>+<kbd>X</kbd> then <kbd>Y</kbd>
        * quit and reopen Terminal

## Project setup
Follow these steps to create the base project structure using our maven archetypes:

1. Open Eclipse and select your workspace directory
2. Go to menu **File > New > Project...**
3. Select **Maven > Maven Project** and click **Next >**
4. Make sure **Create a simple project** is **unchecked**
5. Select your favorite project location and click **Next >**
6. If it is the first time you develop a plugin go the next step to import our archetypes, otherwise go to step 11
7. Click on **Configure...**
8. Click on **Add Remote Catalog...**
9. Set **Catalog File** to `http://sfera.sferalabs.cc/mvn-repo/archetype-catalog.xml` and **Description** to `Sfera`
10. Click on **OK**, and then **OK** again
11. Select the "Sfera" catalog
12. Select the "driver-archetype" or the "app-archetype" depending on what you are going to develop and click **Next >**
13. Set the group id for your plugin. We recommend using the syntax `<your_domain>.sfera.drivers` (e.g. `com.example.sfera.drivers`) for drivers and the syntax `<your_domain>.sfera.apps` (e.g. `com.example.sfera.apps`) for apps
14. Set the artifact id (e.g. `mydriver` or `myapp`)
15. For the package use the concatenation of group and artifact id (e.g. `com.example.sfera.driver.mydriver`)
16. Set the driver/app name property: this will be the name of your driver/app class too, so use the camel-case notation (e.g. `MyDriver` or `MyApp`)
17. Add a description of what your plugin does
18. Click on **Finish**

Now you should see a new project in Eclipse named as your artifact id (e.g. "mydriver").   

If at this point you get the error "Missing artifact com.sun:tools:jar:1.4.2" in your project, try this:

* Open Eclipse preferences
    * On Mac: **Eclipse > Preferences...**
    * On Windows: **Window > Preferences**
* From the side panel select **Java > Installed JREs** and make sure the selected JRE points at the installed JDK 8
* Then go to the sub-menu **Java > Installed JREs > Execution Environments** and mark this JDK as compatible with JavaSE-1.8
* Close the preferences clicking on **OK**
* Right-click on your project and select **Maven > Update Project...**

If the problem is still there, you need to update Eclipse launch configuration:

* Close Eclipse
* Open your Eclipse installation directory
* Locate and open the file `eclipse.ini`
* Add the `-vm` option **before the `-vmargs` option** specifying the path to your JDK, for instance:    
  `...`    
  `-vm`    
  `E:\Program Files\Java\jdk1.8.0_92\bin`    
  `-vmargs`    
  `...`    
* Open Eclipse
* Right-click on your project and select **Maven > Update Project...**

Test that everything is setup correctly by launching Sfera: go to menu **Run > Run History > Sfera-\<plugin name\>**

You should see some logs in your Console window.     
To stop Sfera type `sys quit` in the console and hit <kbd>Enter</kbd>.

        
