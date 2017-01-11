package com.innoq.spring;

import javax.inject.Named;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.innoq.spring.endpoints.CustomerEndpoint;
import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.link.LinkMetaFactory;
import com.mercateo.common.rest.schemagen.plugin.FieldCheckerForSchema;
import com.mercateo.common.rest.schemagen.plugin.MethodCheckerForLink;
import com.mercateo.rest.schemagen.spring.JerseyHateoasConfiguration;

@Configuration
@Import(JerseyHateoasConfiguration.class)
public class RestSchemagenConfiguration {

    @Bean
    @Named("customerLinkFactory")
    LinkFactory<CustomerEndpoint> stationsResourceLinkFactory(LinkMetaFactory linkMetaFactory) {
        return linkMetaFactory.createFactoryFor(CustomerEndpoint.class);
    }

    @Bean
    public FieldCheckerForSchema fieldCheckerForSchema() {
        return (field, callContext) -> true;
    }

    @Bean
    public MethodCheckerForLink methodCheckerForLink() {
        return scope -> true;
    }
}
