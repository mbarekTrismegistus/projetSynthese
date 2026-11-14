package ma.enset.ebank.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import ma.enset.ebank.models.Customer;

@Entity
public class BankAccount {

    @Id
    private String id;
    private LocalDateTime createdAt;
    private double balance;
    private String type;
    private Long customerId;
    @Transient
    private Customer customer;

    protected BankAccount() {
    }

    public BankAccount(String id, LocalDateTime createdAt, double balance, String type, Long customerId) {
        this.id = id;
        this.createdAt = createdAt;
        this.balance = balance;
        this.type = type;
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public double getBalance() {
        return balance;
    }

    public String getType() {
        return type;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
