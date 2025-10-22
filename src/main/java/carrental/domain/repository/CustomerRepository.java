package carrental.domain.repository;

import carrental.domain.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(Integer id);
    List<Customer> findAll();
    void delete(Integer id);
    Optional<Customer> findByDriverLicense(Long driverLicense);
}