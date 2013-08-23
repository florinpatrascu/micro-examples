## Quartz demo

Demonstrating the ability to schedule jobs within micro web apps using the [Quartz scheduler extension](https://github.com/florinpatrascu/micro-extensions/tree/master/quartz_scheduler). 
  
### To use it:

 - check if Micro is installed properly; see the [docs](http://micro-docs.simplegames.ca/), for more details.
 - quickly check if Micro was built and it can be used:
    `$ micro -v`; you should see something like this: `Micro 0.1.2`
 - copy the `quartz_scheduler` extension to `WEB-INF/config/extensions` or create a symbolic link
 - start this web application:
    `$ micro start` or
    `$ ./run.sh`

You can access this app at:

    `http://localhost:8080` or `http://0.0.0.0:8080`

### To stop the app

Simply press `CTRL-C` in the console where the application was started.
