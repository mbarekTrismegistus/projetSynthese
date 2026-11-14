package ma.enset.ebank.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ma.enset.ebank.entities.BankAccount;
import ma.enset.ebank.services.EBankService;

@RestController
@RequestMapping("/accounts")
public class BankAccountRestController {

    private final EBankService service;

    public BankAccountRestController(EBankService service) {
        this.service = service;
    }

    @GetMapping
    public List<BankAccount> all() {
        return service.getAllBankAccounts();
    }

    @GetMapping("/{id}")
    public BankAccount byId(@PathVariable String id) {
        return service.getBankAccountById(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<BankAccount> byCustomer(@PathVariable Long customerId) {
        return service.getAccountsByCustomerId(customerId);
    }

    @PostMapping
    public BankAccount create(@RequestBody BankAccount account) {
        return service.saveBankAccount(account);
    }
}
