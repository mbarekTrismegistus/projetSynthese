package ma.enset.customer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import ma.enset.customer.entities.Customer;
import ma.enset.customer.repositories.CustomerRepository;

@SpringBootApplication
@EnableDiscoveryClient
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner seedCustomers(CustomerRepository customers) {
        return args -> {
            if (customers.count() == 0) {
                customers.save(new Customer(null, "Mohammed", "mohammed@gmail.com"));
                customers.save(new Customer(null, "Iman", "iman@gmail.com"));
                customers.save(new Customer(null, "Yassine", "yassine@gmail.com"));
            }
        };
    }
}
