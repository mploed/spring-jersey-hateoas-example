package com.innoq.spring.endpoints;

import static com.mercateo.common.rest.schemagen.util.OptionalUtil.collect;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

import com.innoq.spring.domain.Customer;
import com.innoq.spring.repo.CustomerRepository;
import com.mercateo.common.rest.schemagen.JerseyResource;
import com.mercateo.common.rest.schemagen.JsonHyperSchema;
import com.mercateo.common.rest.schemagen.JsonHyperSchemaCreator;
import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.link.LinkMetaFactory;
import com.mercateo.common.rest.schemagen.link.relation.Rel;
import com.mercateo.common.rest.schemagen.types.ListResponse;
import com.mercateo.common.rest.schemagen.types.ListResponseBuilderCreator;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
public class CustomerEndpoint implements JerseyResource{
    private final LinkMetaFactory linkMetaFactory;

    private final ListResponseBuilderCreator listResponseBuilderCreator;

    private final CustomerRepository customerRepository;

    private final JsonHyperSchemaCreator hyperSchemaCreator;

    private final LinkFactory<CustomerEndpoint> customerLinkFactory;

    public CustomerEndpoint(LinkMetaFactory linkMetaFactory, ListResponseBuilderCreator listResponseBuilderCreator,
            CustomerRepository customerRepository, JsonHyperSchemaCreator hyperSchemaCreator,
            LinkFactory<CustomerEndpoint> customerLinkFactory) {
        this.linkMetaFactory = linkMetaFactory;
        this.listResponseBuilderCreator = listResponseBuilderCreator;
        this.customerRepository = customerRepository;
        this.hyperSchemaCreator = hyperSchemaCreator;
        this.customerLinkFactory = customerLinkFactory;
    }

    @GET
    @Path("/{id}")
    public ObjectWithSchema<Customer> getCustomer(@PathParam("id")Long id) {
        System.out.println("in GET_CUSTOMER via GET");

        Customer customer = customerRepository.findOne(id);

        final JsonHyperSchema hyperSchema = hyperSchemaCreator.from(collect(
                customerLinkFactory.forCall(Rel.SELF, r -> r.getCustomer(customer.getId())),
                customerLinkFactory.forCall(Rel.DELETE, r -> r.delete(customer.getId())),
                customerLinkFactory.forCall(Rel.UPDATE, r -> r.update(customer.getId(), null))
        ));

        return ObjectWithSchema.create(customer, hyperSchema);
    }
    @GET
    public ListResponse<Customer> index() {
        System.out.println("in INDEX via GET");

        List<Customer> customerList = customerRepository.findAll();

        final List<Link> containerLinks = collect(
                customerLinkFactory.forCall(Rel.SELF, r -> r.index()),
                customerLinkFactory.forCall(Rel.CREATE, r -> r.addCustomer(null))
        );

        return listResponseBuilderCreator.<Customer, Customer>builder()
                .withList(customerList)
                .withContainerLinks(containerLinks.stream().toArray(Link[]::new))
                .withElementMapper(this::create)
                .build();
    }

    private ObjectWithSchema<Customer> create(Customer customer) {
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
