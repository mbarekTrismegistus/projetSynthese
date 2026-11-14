package ma.enset.ebank.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.enset.ebank.entities.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {

    List<BankAccount> findByCustomerId(Long customerId);
}
