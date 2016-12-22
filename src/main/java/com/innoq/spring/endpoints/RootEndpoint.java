package com.innoq.spring.endpoints;

import com.mercateo.common.rest.schemagen.JsonHyperSchema;
import com.mercateo.common.rest.schemagen.link.LinkMetaFactory;
import com.mercateo.common.rest.schemagen.link.relation.Rel;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/")
public class RootEndpoint {
    @Inject
    private LinkMetaFactory linkMetaFactory;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectWithSchema getRoot() {
        Optional<Link> customersLink = linkMetaFactory.createFactoryFor(CustomerEndpoint.class).forCall(CustomerRel.CUSTOMERS,
                r -> r.index());

        return ObjectWithSchema.create("",
                JsonHyperSchema.from(customersLink));

    }
}
