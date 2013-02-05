## StringTemplate demo

This is a very simple web application demonstrating the integration between Micro and [StringTemplate](http://www.stringtemplate.org/), a Java template engine.

### How to use it:

Micro must be properly configured. You can check this with:

    $ micro -v
    
see the [docs](http://micro-docs.simplegames.ca/) for more details.

### Install the st-demo web application

    git clone https://github.com/florinpatrascu/micro-examples.git
    cd micro-examples/st-demo
    ./run.sh

Micro is now started and it is using the embedded Jetty web server. To access the web application, point your browser at: `http://localhost:8080` or `http://0.0.0.0:8080`