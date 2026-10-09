package ma.enset.customer.services;

import ma.enset.customer.entities.Customer;
import ma.enset.customer.repositories.CustomerRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository repository;
    public CustomerService(CustomerRepository repository) { this.repository = repository; }

    @Tool(description = "Return the list of all customers")
    public List<Customer> getAllCustomers() { return repository.findAll(); }

    @Tool(description = "Find one customer by its identifier")
    public Customer findCustomerById(@ToolParam(description = "Customer identifier") Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found: " + id));
    }

    @Tool(description = "Create a customer with a name and email")
    public Customer saveCustomer(@ToolParam(description = "Customer to create") Customer customer) {
        customer.setId(null);
        return repository.save(customer);
    }
}
