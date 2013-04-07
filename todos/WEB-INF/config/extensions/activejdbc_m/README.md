## `activejdbc` extension 

Extending Micro with support for the [Activejdbc](http://code.google.com/p/activejdbc/); AJ. ActiveJDBC is a Java implementation of the Active Record design pattern.

### Build from source

    # check if the `${MICRO_HOME}` environment variable is defined
    # then:

    $ cd activejdbc_m
    $ ant clean; ant dist
    
### Installation
The integration with the ActiveJDBC (AJ) library is robust enough to be used for serious db heavy lifting but we're still trying to optimize the installation procedure.

Copy (or create symbolic links) the `activejdbc_m.yml` file and the `activejdbc_m` folder to your application's extensions folder. The `extensions` folder will contain at least the following:

    extensions/
      ├── activejdbc_m.yml
      ├── activejdbc_m/
      └── ...

Copy the `build_models.xml` from the distribution folder to your `WEB-INF` folder and rename it to `build.xml`. This file will help you instrument your class files; details below. 

### ActiveJDBC and the Micro web applications

This version is using a database connection pool, with the help of: [BoneCP](http://jolbox.com/). A future release will allow you to connect your web application to a datasource using JNDI.
  
Create a database connection configuration file: `WEB-INF/config/db.yml`, and describe your connectors, example:

    production:
      driver: org.h2.Driver
      url: jdbc:h2:~/mydb_production
      user: sa
      password:
      pool: 15      
      
    development:
      driver: org.h2.Driver
      url: jdbc:h2:~/mydb_development
      user: sa
      password:
      pool: 5
      
    test:
      driver: org.h2.Driver
      url: jdbc:h2:~/mydb_test
      user: sa
      password:
      pool: 1      

Micro will select the connector corresponding to its running mode. 

Edit the `application.bsh` startup controller and require the `activejdbc_m` extension, example:

    site.ExtensionsManager
        .require("i18N")
        .require("activejdbc_m");

Create a new folder in your app path: `WEB-INF/models`, and add or create your model classes there. You will have to instrument your models before using them. ActiveJDBC requires instrumentation of class files after they are compiled. You can read more about this procedure by following this link: [What is instrumentation?](https://code.google.com/p/activejdbc/wiki/Instrumentation)

With our setup you can compile and instrument your models as simple as this:

    $ cd WEB-INF
    $ ant

You can find a full Hotel Booking demo web application on Github, in the `micro-examples` repository: [Micro-AJ](https://github.com/florinpatrascu/micro-examples/tree/master/micro-aj).

### License
**Apache License 2**, see the `LICENSE` file in this folder.
