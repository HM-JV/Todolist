package com.humanbooster.todolist;

import io.dropwizard.Application;
import  io.dropwizard.setup.Bootstrap;
import  io.dropwizard.setup.Environment;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

    // Classe principale nécéssaire a l'éxecution de JAVA.
    public static void main(String[] args) throws Exception {
        new HelloWorldApplication().run(args);
    }

    @Override
    public void run(HelloWorldConfiguration configuration,Environment environment) {
        final HelloWorldResource resource = new HelloWorldResource();
        environment.jersey().register(resource);
        // nothing to do yet
    }
}

