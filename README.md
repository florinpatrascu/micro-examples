![](micro-logo.png)
 
Examples of web applications and various deployment solutions using the Micro MVC framework. Micro is still in development and it will be publicly available very soon. The entire Micro ecosystem is licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

To use Micro you need [Java 6 or later](http://www.oracle.com/technetwork/java/javase/downloads/index.html). Verify you have the `java` command available in your path. You can check this by simply typing the following command:

    $ java -version


Developers and non-developers can use Micro for web development. You **don't have to learn Java** for building a web application with Micro.
    
In this repo:
 
 - `lib` - a folder containing the latest Micro binaries, frequently updated.
 - `hello_world` - a minimalistic Micro web application
 - ...
 
and an arbitrary number of folders containing examples of Micro web applications. Each example will have a `run.sh` (or `run.bat`) command line that will start Micro in server mode and a `README.md` file describing the example.

Using the examples.

    git clone https://github.com/florinpatrascu/micro-examples.git
    cd micro-examples/hello_world
    ./run.sh

You will see the console log displaying something like this:  

     _ __ ___ ( ) ___ _ __ ___ 
    | '_ ` _ \| |/ __| '__/ _ \ 
    | | | | | | | (__| | | (_) |
    |_| |_| |_|_|\___|_|  \___/  (x.y.z)
    = a modular micro MVC Java framework
    
and you can point your browser to: `http://localhost:8080` or `http://0.0.0.0:8080`

More about Micro coming **very soon**!

**Other links:**

 - [micro](https://github.com/florinpatrascu/micro) - a modular micro MVC framework for Java web applications, the main repository.
 - [micro-docs](https://github.com/florinpatrascu/micro-docs) - the Micro documentation
 - [jrack](https://github.com/florinpatrascu/jrack) - another port of Rack to Java

