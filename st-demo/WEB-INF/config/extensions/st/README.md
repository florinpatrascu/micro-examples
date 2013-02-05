## StringTemplate engine

Extending Micro with support for StringTemplate rendering.

### Build from source

    # check if the `${MICRO_HOME}` environment variable is defined
    # then:

    $ cd st
    $ ant clean; ant dist
    
### Install
Copy (or create symbolic links) the `st` folder and `st.yml` file to your application extensions folder. The `extensions` folder will contain at least the following:

    extensions/
      ├── st/ 
      ├── st.yml
      └── ...
  
Edit the `application.bsh` startup controller and required the `st` extension, example:

    site.ExtensionsManager
        .require("i18N")
        .require("st"); // <-- just added

restart the app and go to: `http://localhost:8080/cache`. The following interface will be shown:

### License
**Apache License 2**, see the `LICENSE` file in this folder.
