package carrental.domain.service;

import carrental.domain.model.Customer;
import carrental.domain.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer addCustomer(String fullName, Long passport, Long driverLicense,
                                Long phone, String address) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }

        if (passport == null || passport <= 0) {
            throw new IllegalArgumentException("Valid passport number is required");
        }

        if (driverLicense == null || driverLicense <= 0) {
            throw new IllegalArgumentException("Valid driver license number is required");
        }

        if (phone == null || phone <= 0) {
            throw new IllegalArgumentException("Valid phone number is required");
        }

        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }

        if (customerRepository.findByDriverLicense(driverLicense).isPresent()) {
            throw new IllegalArgumentException("Customer with driver license " + driverLicense + " already exists");
        }

        if (customerRepository.findByPassport(passport).isPresent()) {
            throw new IllegalArgumentException("Customer with passport " + passport + " already exists");
        }

        Customer customer = new Customer(null, fullName, passport, driverLicense, phone, address);
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomer(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        return customerRepository.findById(id);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Optional<Customer> getCustomerByDriverLicense(Long driverLicense) {
        if (driverLicense == null || driverLicense <= 0) {
            throw new IllegalArgumentException("Valid driver license number is required");
        }
        return customerRepository.findByDriverLicense(driverLicense);
    }

    public Optional<Customer> getCustomerByPassport(Long passport) {
        if (passport == null || passport <= 0) {
            throw new IllegalArgumentException("Valid passport number is required");
        }
        return customerRepository.findByPassport(passport);
    }

    public List<Customer> findCustomersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required for search");
        }
        return customerRepository.findByNameContaining(name);
    }

    public boolean deleteCustomer(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }

        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            customerRepository.delete(id);
            return true;
        }
        return false;
    }

    public boolean updateCustomer(Integer customerId, String fullName, Long phone, String address) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }

        if (phone == null || phone <= 0) {
            throw new IllegalArgumentException("Valid phone number is required");
        }

        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Address is required");
        }

        Optional<Customer> existingCustomer = customerRepository.findById(customerId);
        if (existingCustomer.isPresent()) {
            Customer updatedCustomer = new Customer(
                    customerId,
                    fullName,
                    existingCustomer.get().getPassport(), // Passport cannot be changed
                    existingCustomer.get().getDriverLicense(), // Driver license cannot be changed
                    phone,
                    address
            );
            customerRepository.save(updatedCustomer);
            return true;
        }
        return false;
    }

    public boolean canCustomerRent(Integer customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }

        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            return false;
        }

        return true;
    }

    public boolean customerExistsByDriverLicense(Long driverLicense) {
        if (driverLicense == null || driverLicense <= 0) {
            throw new IllegalArgumentException("Valid driver license number is required");
        }
        return customerRepository.existsByDriverLicense(driverLicense);
    }
}