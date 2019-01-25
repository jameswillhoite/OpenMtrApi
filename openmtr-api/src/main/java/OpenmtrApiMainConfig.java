package com.openmtrapi.api;

/*
OpenMtr-API
Authors James Willhoite, Jenny Franklin, Matt Thomas
PSTCC Capstone 2019
 */

//Import required
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


public class OpenmtrApiMainConfig extends Application {
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(com.openmtrapi.api.OpenmtrApiMain.class);
        return classes;
    }
}
