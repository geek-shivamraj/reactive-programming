package com.reactive.web;

import com.reactive.web.entity.Customer;
import com.reactive.web.repository.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

@Slf4j
public class CustomerRepositoryTest extends AbstractTest {

    @Autowired
    private CustomerRepository customerRepository;

    // Task: Find All Customers whose email ending with "ke@gmail.com"
    @Test
    public void findByEmailEndingWith() {
        customerRepository.findByEmailEndingWith("ke@gmail.com")
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike@gmail.com", c.getEmail()))
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                .expectComplete().verify();
    }

    @Test
    public void findAll() {
        customerRepository.findAll()
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create).expectNextCount(10)
                .expectComplete().verify();
    }

    @Test
    public void findById() {
        customerRepository.findById(2)
                //.findById(3)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
                .expectComplete().verify();
    }

    @Test
    public void findByName() {
        customerRepository.findByName("jake")
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                .expectComplete().verify();
    }

    @Test
    public void insertAndDeleteCustomer() {

        // insert
        var customer = new Customer();
        customer.setName("marshal");
        customer.setEmail("marshal@gmail.com");
        customerRepository.save(customer)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertNotNull(c.getId()))
                .expectComplete()
                .verify();

        // count
        customerRepository.count()
                .doOnNext(c -> log.info("Count: {}", c))
                .as(StepVerifier::create)
                .expectNext(11L)
                .expectComplete()
                .verify();

        // delete
        customerRepository.deleteById(11)
                .then(customerRepository.count())
                .doOnNext(c -> log.info("Count after delete: {}", c))
                .as(StepVerifier::create)
                .expectNext(10L)
                .expectComplete()
                .verify();

    }

    @Test
    public void updateCustomer() {

        customerRepository.findByName("ethan")
                .doOnNext(c -> log.info("Existing record: {}", c))
                .doOnNext(c -> c.setName("noel"))
                .flatMap(c -> customerRepository.save(c))
                .doOnNext(c -> log.info("Updated record: {}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("noel", c.getName()))
                .expectComplete()
                .verify();

    }
}
