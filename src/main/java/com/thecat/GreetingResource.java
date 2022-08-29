package com.thecat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/greeting")
public class GreetingResource {


    @ConfigProperty( name="greeting.message" )
    private String greetingMessage;

    @ConfigProperty( name="rhug.message" )
    private String rhugMessage;

    @ConfigProperty( name="knative.serving.message" )
    private String servingMessage;


    @GET
    @Path("/")
    @Produces(MediaType.TEXT_PLAIN)
    public String greeting() {
        return this.greetingMessage;
    }

    @GET
    @Path("/rhug")
    @Produces(MediaType.TEXT_PLAIN)
    public String rhug() {
        return this.rhugMessage; 
    }

    @GET
    @Path("/serverless")
    @Produces(MediaType.TEXT_PLAIN)
    public String serving() {
        return this.servingMessage; 
    }
}