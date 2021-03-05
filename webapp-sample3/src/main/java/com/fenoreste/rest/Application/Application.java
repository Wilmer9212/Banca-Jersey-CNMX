package com.fenoreste.rest.Application;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

@ApplicationPath("services")
public class Application extends ResourceConfig {
    public Application() {
        packages("com.fenoreste.rest.services");
        register(JacksonFeature.class);
        register(RolesAllowedDynamicFeature.class);
    }
}
