![Micro Logo](https://raw.github.com/florinpatrascu/micro-examples/master/micro-logo.png)
 
Examples of web applications and various deployment solutions using the Micro MVC framework. The entire Micro ecosystem is licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

To use Micro you need [Java 6 or later](http://www.oracle.com/technetwork/java/javase/downloads/index.html). Verify you have the `java` command available in your path. You can check this by simply typing the following command:

    java -version

Developers and non-developers can use Micro for web development. You **don't have to learn Java** for building a web application with Micro.
    
In this repo:
 
 - `lib` - a folder containing the latest Micro binaries, frequently updated.
 - `hello_world` - a minimalistic Micro web application
 - `aj_demo` - a very small Micro web application demonstrating the integration with the [ActiveJDBC](https://code.google.com/p/activejdbc/) framework for ORM. ActiveJDBC is a Java implementation of the Active Record design pattern. It was inspired by ActiveRecord ORM from Ruby on Rails.
 - `micro-aj` - a Hotel booking demo app used for benchmarking the Micro framework. It is loosely modelled after the Wicket application used by Peter Thomas in his article: [Seam / JSF vs Wicket](http://ptrthomas.wordpress.com/2009/01/14/seam-jsf-vs-wicket-performance-comparison/). The app repo contains details about the stress sessions and some simple stats.<p></p>
 - *more to come*
 
and an arbitrary number of folders containing examples of Micro web applications. Each example will have a `run.sh` (or `run.bat`) command line that will start Micro in server mode and a `README.md` file describing the example.

Using the examples.

    $ git clone https://github.com/florinpatrascu/micro-examples.git
    $ cd micro-examples/hello_world
    $ micro start

You will see the console log displaying something like this:  

     _ __ ___ ( ) ___ _ __ ___ 
    | '_ ` _ \| |/ __| '__/ _ \ 
    | | | | | | | (__| | | (_) |
    |_| |_| |_|_|\___|_|  \___/  (x.y.z)
    = a modular micro MVC Java framework
    
and you can point your browser to: `http://localhost:8080` or `http://0.0.0.0:8080`

Follow us on Twitter: [@micro_mvc](http://twitter.com/micro_mvc), for Micro related news.

**Other links:**

 - [micro](https://github.com/florinpatrascu/micro) - a modular micro MVC framework for Java web applications, the main repository.
 - [micro-docs](https://github.com/florinpatrascu/micro-docs) - the Micro documentation
 - [jrack](https://github.com/florinpatrascu/jrack) - another port of Rack to Java
