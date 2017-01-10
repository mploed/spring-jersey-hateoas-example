package com.innoq.spring.endpoints;

import com.mercateo.common.rest.schemagen.JsonHyperSchema;
import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.link.LinkMetaFactory;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/")
@Component
public class RootEndpoint {

    private final LinkFactory<CustomerEndpoint> customerLinkFactory;

    public RootEndpoint(@Named("customerLinkFactory") LinkFactory<CustomerEndpoint> customerLinkFactory) {
        this.customerLinkFactory = customerLinkFactory;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectWithSchema<Void> getRoot() {
        Optional<Link> customersLink = customerLinkFactory.forCall(CustomerRel.CUSTOMERS,
                r -> r.index());

        return ObjectWithSchema.create(null,
                JsonHyperSchema.from(customersLink));
    }
}
