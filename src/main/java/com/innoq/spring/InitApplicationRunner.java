package com.innoq.spring;

import com.innoq.spring.domain.Adress;
import com.innoq.spring.domain.Customer;
import com.innoq.spring.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitApplicationRunner implements ApplicationRunner {

    private CustomerRepository customerRepository;

    @Autowired
    public InitApplicationRunner(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Customer c1 = new Customer("Michael", "Plöd", new Adress("Teststr. 12", "Nürnberg", "90427"));
        Customer c2 = new Customer("Stefan", "Strauber", new Adress("Karolinenstrasse 166", "München", "80331"));
        Customer c3 = new Customer("Katharina", "Brunner", new Adress("Schonhoverstr. 1", "Düsseldorf", "40789"));
        customerRepository.save(c1);
        customerRepository.save(c2);
        customerRepository.save(c3);
    }
}
