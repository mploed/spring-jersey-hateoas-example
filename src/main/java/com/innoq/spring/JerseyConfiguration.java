package com.innoq.spring;

import com.innoq.spring.endpoints.CustomerEndpoint;
import com.innoq.spring.endpoints.RootEndpoint;
import com.mercateo.common.rest.schemagen.link.injection.LinkFactoryResourceConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class JerseyConfiguration extends ResourceConfig {
    public JerseyConfiguration() {


        register(RootEndpoint.class);
        register(CustomerEndpoint.class);
        LinkFactoryResourceConfig.configure(this);
    }

}
