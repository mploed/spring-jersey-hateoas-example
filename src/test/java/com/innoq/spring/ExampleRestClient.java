package com.innoq.spring;

import com.innoq.spring.domain.Adress;
import com.innoq.spring.domain.Customer;
import com.mercateo.rest.hateoas.client.ClientStarter;
import com.mercateo.rest.hateoas.client.ListResponse;
import com.mercateo.rest.hateoas.client.Response;

import java.util.Optional;


public class ExampleRestClient {
    public static void main(String[] args) {

        Response<Object> rootResource = new ClientStarter().create("http://localhost:8080", Object.class);

        Optional<ListResponse<Customer>> customersListResource = rootResource
                .prepareNextWithResponse(Customer.class).callListWithRel("customers");

        ListResponse<Customer> customerListResponse = customersListResource.get();

        //Insert von Simone Hummel
        Customer c1 = new Customer("Simone", "Hummel", new Adress("Bienenweg. 2c", "Wespenstein", "6"));

        customerListResponse.prepareNextWithResponse(Void.class).withRequestObject(c1).callWithRel("create");

        //Optional<ListResponse<Customer>> customerListResponse = customersListResource.get().prepareNextWithResponse(Customer.class).callListWithRel("self");
        //Delete
        Optional<Response<Customer>> customerAtIndex1 = customerListResponse.get(1).get().prepareNextWithResponse(Customer.class).callWithRel("self");
        customerAtIndex1.get().prepareNextWithResponse(Void.class).callWithRel("delete");


        //Update
        Optional<Response<Customer>> customerAtIndex0 = customerListResponse.get(0).get().prepareNextWithResponse(Customer.class).callWithRel("self");

        Customer customer = customerAtIndex0.get().getResponseObject().get();
        System.out.println(customer);
        customer.setLastName("Semmler");
        customer.setName("Thorsten");
        customerAtIndex0.get().prepareNextWithResponse(Void.class).withRequestObject(customer).callWithRel("update");

        Optional<ListResponse<Customer>> neueCustomerListResource = customerListResponse.prepareNextWithResponse(Customer.class).callListWithRel("self");
        neueCustomerListResource.get();

    }
}
