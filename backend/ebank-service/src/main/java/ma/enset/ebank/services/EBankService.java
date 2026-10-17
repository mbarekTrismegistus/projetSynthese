package ma.enset.ebank.services;

import ma.enset.ebank.entities.BankAccount;
import ma.enset.ebank.feign.CustomerRestClient;
import ma.enset.ebank.models.Customer;
import ma.enset.ebank.repositories.BankAccountRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EBankService {
    private final BankAccountRepository repository;
    private final CustomerRestClient customers;
    private final CircuitBreakerFactory<?, ?> circuitBreakers;

    public EBankService(BankAccountRepository repository, CustomerRestClient customers, CircuitBreakerFactory<?, ?> circuitBreakers) {
        this.repository = repository; this.customers = customers; this.circuitBreakers = circuitBreakers;
    }

    @Tool(description = "Return all bank accounts, including their customer when available")
    public List<BankAccount> getAllBankAccounts() { return repository.findAll().stream().map(this::enrich).toList(); }

    @Tool(description = "Find one bank account by its identifier")
    public BankAccount getBankAccountById(@ToolParam(description = "Bank account identifier") String id) {
        return enrich(repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account not found: " + id)));
    }

    @Tool(description = "List all bank accounts for a customer identifier")
    public List<BankAccount> getAccountsByCustomerId(@ToolParam(description = "Customer identifier") Long customerId) {
        return repository.findByCustomerId(customerId).stream().map(this::enrich).toList();
    }

    @Tool(description = "Create a bank account with balance, type and customerId")
    public BankAccount saveBankAccount(@ToolParam(description = "Account to save") BankAccount account) {
        if (account.getCustomerId() == null) throw new IllegalArgumentException("customerId is required");
        Customer customer = customers.findCustomerById(account.getCustomerId());
        if (customer == null || customer.id() == null) throw new IllegalArgumentException("Customer not found: " + account.getCustomerId());
        if (account.getId() == null || account.getId().isBlank()) account.setId(UUID.randomUUID().toString());
        if (account.getCreatedAt() == null) account.setCreatedAt(LocalDateTime.now());
        account.setCustomer(customer);
        return repository.save(account);
    }

    private BankAccount enrich(BankAccount account) { account.setCustomer(lookupCustomer(account.getCustomerId())); return account; }
    private Customer lookupCustomer(Long id) {
        return circuitBreakers.create("customer-service").run(() -> customers.findCustomerById(id), error ->
                new Customer(id, "not available", "not available"));
    }
}
