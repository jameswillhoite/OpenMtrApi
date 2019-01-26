package com.openmtr_api;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;

/*
OpenMtr-API
Authors James Willhoite, Jenny Franklin, Matt Thomas
PSTCC Capstone 2019
 */

//Import required
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@Path("/")
public class OpenmtrApiMainConfig extends Application {
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(com.openmtr_api.OpenmtrApiMain.class);
        return classes;
    }
}
