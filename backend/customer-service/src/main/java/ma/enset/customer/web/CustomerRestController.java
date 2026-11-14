package ma.enset.customer.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.enset.customer.entities.Customer;
import ma.enset.customer.services.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {

    private final CustomerService service;

    public CustomerRestController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Customer> all() {
        return service.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer byId(@PathVariable Long id) {
        return service.findCustomerById(id);
    }

    @PostMapping
    public Customer create(@RequestBody Customer customer) {
        return service.saveCustomer(customer);
    }
}
