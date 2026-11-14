package ma.enset.customer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.enset.customer.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
