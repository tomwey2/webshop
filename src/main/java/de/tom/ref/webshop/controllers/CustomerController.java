package de.tom.ref.webshop.controllers;

import de.tom.ref.webshop.entities.Customer;
import de.tom.ref.webshop.errorhandling.CustomerNotFoundException;
import de.tom.ref.webshop.repositories.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/customers")
public class CustomerController {
    Logger log = LogManager.getLogger(CustomerController.class);

    @Autowired
    CustomerRepository repository;

    @GetMapping("")
    public List<Customer> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    Customer getById(@PathVariable Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @GetMapping("/email/{email}")
    Customer getByEmail(@PathVariable String email) {
        return repository
                .findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(email));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/create",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Customer post(@RequestBody Customer newCustomer) {
        Customer customer = create(newCustomer.getFirstName(), newCustomer.getLastName(),
                newCustomer.getEmail(), newCustomer.getPassword(),
                newCustomer.getAddressLine1(), newCustomer.getAddressLine2(),
                newCustomer.getCity(), newCustomer.getPostalCode(), newCustomer.getCountry());
        log.debug("Call POST /createCustomer/{}", customer);
        return repository.save(customer);
    }

    @PutMapping("/{id}")
    Customer put(@RequestBody Customer object, @PathVariable Integer id) {
        return repository.findById(id)
                .map(customer -> {
                    customer.setFirstName(object.getFirstName());
                    customer.setLastName(object.getLastName());
                    // ... TODO
                    return repository.save(customer);
                }).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @DeleteMapping("/{id}")
    void deleteById(@PathVariable Integer id) {
        repository.deleteById(id);
    }

    public Customer create(String firstName, String lastName, String email, String password,
                           String addressLine1, String addressLine2,
                           String city, String postalcode, String country) {
        return new Customer(firstName, lastName, email, password,
                addressLine1, addressLine2, city, postalcode, country);
    }

}
