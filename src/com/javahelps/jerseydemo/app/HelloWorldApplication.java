package com.javahelps.jerseydemo.app;

import org.glassfish.jersey.server.ResourceConfig;

public class HelloWorldApplication extends ResourceConfig {

	public HelloWorldApplication() {
		packages("com.javahelps.jerseydemo.services");
	}
}
