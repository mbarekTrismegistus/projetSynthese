package ma.enset.customer.web;

import ma.enset.customer.entities.Customer;
import ma.enset.customer.services.CustomerService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {
    private final CustomerService service;
    public CustomerRestController(CustomerService service) { this.service = service; }
    @GetMapping public List<Customer> all() { return service.getAllCustomers(); }
    @GetMapping("/{id}") public Customer byId(@PathVariable Long id) { return service.findCustomerById(id); }
    @PostMapping public Customer create(@RequestBody Customer customer) { return service.saveCustomer(customer); }
}
