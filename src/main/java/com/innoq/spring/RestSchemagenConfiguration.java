package com.innoq.spring;

import com.mercateo.common.rest.schemagen.types.ListResponseBuilderCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestSchemagenConfiguration {
    @Bean
    public ListResponseBuilderCreator listResponseBuilderCreator() {
        return new ListResponseBuilderCreator();
    }
}
