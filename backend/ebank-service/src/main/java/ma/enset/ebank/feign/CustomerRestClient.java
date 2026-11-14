package ma.enset.ebank.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ma.enset.ebank.models.Customer;

@FeignClient(name = "CUSTOMER-SERVICE", path = "/customers")
public interface CustomerRestClient {

    @GetMapping("/{id}")
    Customer findCustomerById(@PathVariable("id") Long id);
}
