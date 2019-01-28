package com.openmtr_api.app;

import org.glassfish.jersey.server.ResourceConfig;

public class OpenMtrApiApplication extends ResourceConfig {

	public OpenMtrApiApplication() {
		packages("com.openmtr_api.services");
	}
}
