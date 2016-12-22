package com.innoq.spring.endpoints;

import com.innoq.spring.domain.Customer;
import com.innoq.spring.repo.CustomerRepository;
import com.mercateo.common.rest.schemagen.JerseyResource;
import com.mercateo.common.rest.schemagen.JsonHyperSchema;
import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.link.LinkMetaFactory;
import com.mercateo.common.rest.schemagen.link.relation.Rel;
import com.mercateo.common.rest.schemagen.link.relation.RelType;
import com.mercateo.common.rest.schemagen.link.relation.Relation;
import com.mercateo.common.rest.schemagen.types.ListResponse;
import com.mercateo.common.rest.schemagen.types.ListResponseBuilder;
import com.mercateo.common.rest.schemagen.types.ListResponseBuilderCreator;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerEndpoint implements JerseyResource{
    private LinkMetaFactory linkMetaFactory;

    private ListResponseBuilderCreator listResponseBuilderCreator;

    private CustomerRepository customerRepository;

    public CustomerEndpoint() {
    }

    @Inject
    public CustomerEndpoint(LinkMetaFactory linkMetaFactory, ListResponseBuilderCreator listResponseBuilderCreator, CustomerRepository customerRepository) {
        this.linkMetaFactory = linkMetaFactory;
        this.listResponseBuilderCreator = listResponseBuilderCreator;
        this.customerRepository = customerRepository;
    }

    @GET
    @Path("/{id}")
    public ObjectWithSchema<Customer> getCustomer(@PathParam("id")Long id) {
        System.out.println("in GET_CUSTOMER via GET");
        Customer customer = customerRepository.findOne(id);

        LinkFactory<CustomerEndpoint> customerLinkFactory = linkMetaFactory.createFactoryFor(CustomerEndpoint.class);
        Optional<Link> self = customerLinkFactory.forCall(Rel.SELF, r -> r.getCustomer(customer.getId()));
        Optional<Link> delete = customerLinkFactory.forCall(Rel.DELETE, r-> r.delete(customer.getId()));
        Optional<Link> update = customerLinkFactory.forCall(Rel.UPDATE, r-> r.update(customer.getId(), customer));

        return ObjectWithSchema.create(customer, JsonHyperSchema.from(self, delete, update));
    }
    @GET
    public ListResponse<Customer> index() {
        System.out.println("in INDEX via GET");
        List<Customer> customerList = customerRepository.findAll();
        LinkFactory<CustomerEndpoint> customerLinkFactory = linkMetaFactory.createFactoryFor(CustomerEndpoint.class);
        Optional<Link> createLink = customerLinkFactory.forCall(Relation.of("create", RelType.OTHER), r -> r.addCustomer(new Customer()));
        Optional<Link> selfLink = customerLinkFactory.forCall(Relation.of("self", RelType.SELF), r -> r.index());
        return listResponseBuilderCreator.<Customer, Customer>builder()
                .withList(customerList)
                .withContainerLinks(createLink, selfLink)
                .withElementMapper(this::create)
                .build();
    }

    private ObjectWithSchema<Customer> create(Customer customer) {
        LinkFactory<CustomerEndpoint> customerLinkFactory = linkMetaFactory.createFactoryFor(CustomerEndpoint.class);
        Optional<Link> self = customerLinkFactory.forCall(Rel.SELF, r -> r.getCustomer(customer.getId()));
        return ObjectWithSchema.create(customer, JsonHyperSchema.from(self));
    }

    @POST
    @Path("/{id}")
    public Response update(@PathParam("id")Long id, Customer customer) {
        System.out.println("in UPDATE via POST");
        customerRepository.save(customer);
        return Response.status(Response.Status.OK).build();
    }
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id")Long id) {
        System.out.println("in DELETE via DELETE");
        customerRepository.delete(id);
        return Response.status(Response.Status.OK).build();
    }

    @PUT
    public Response addCustomer(Customer customer) {
        System.out.println("in ADD via PUT");
        customerRepository.save(customer);
        return Response.status(Response.Status.CREATED).build();
    }
}
