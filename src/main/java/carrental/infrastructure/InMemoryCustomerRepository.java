package carrental.infrastructure;

import carrental.domain.model.Customer;
import carrental.domain.repository.CustomerRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryCustomerRepository implements CustomerRepository {
    private final Map<Integer, Customer> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public Customer save(Customer customer) {
        if (customer.getCustomerId() == null) {
            Customer newCustomer = new Customer(
                    idCounter.getAndIncrement(),
                    customer.getFullName(),
                    customer.getPassport(),
                    customer.getDriverLicense(),
                    customer.getPhone(),
                    customer.getAddress()
            );
            storage.put(newCustomer.getCustomerId(), newCustomer);
            return newCustomer;
        } else {
            storage.put(customer.getCustomerId(), customer);
            return customer;
        }
    }

    @Override
    public Optional<Customer> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Integer id) {
        storage.remove(id);
    }

    @Override
    public Optional<Customer> findByDriverLicense(Long driverLicense) {
        return storage.values().stream()
                .filter(customer -> customer.getDriverLicense().equals(driverLicense))
                .findFirst();
    }

    @Override
    public Optional<Customer> findByPassport(Long passport) {
        return storage.values().stream()
                .filter(customer -> customer.getPassport().equals(passport))
                .findFirst();
    }

    @Override
    public Optional<Customer> findByPhone(Long phone) {
        return storage.values().stream()
                .filter(customer -> customer.getPhone().equals(phone))
                .findFirst();
    }

    @Override
    public boolean existsByDriverLicense(Long driverLicense) {
        return storage.values().stream()
                .anyMatch(customer -> customer.getDriverLicense().equals(driverLicense));
    }

    @Override
    public boolean existsByPassport(Long passport) {
        return storage.values().stream()
                .anyMatch(customer -> customer.getPassport().equals(passport));
    }

    @Override
    public List<Customer> findByNameContaining(String name) {
        String searchTerm = name.toLowerCase();
        return storage.values().stream()
                .filter(customer -> customer.getFullName().toLowerCase().contains(searchTerm))
                .collect(Collectors.toList());
    }

    public void initializeWithSampleData() {
        if (storage.isEmpty()) {
            save(new Customer(null, "John Smith", 1234567890L, 9876543210L, 5550101234L, "123 Main St, New York"));
            save(new Customer(null, "Maria Garcia", 2345678901L, 8765432109L, 5550102345L, "456 Oak Ave, Los Angeles"));
            save(new Customer(null, "David Johnson", 3456789012L, 7654321098L, 5550103456L, "789 Pine Rd, Chicago"));
            save(new Customer(null, "Sarah Williams", 4567890123L, 6543210987L, 5550104567L, "321 Elm St, Houston"));
            save(new Customer(null, "Robert Brown", 5678901234L, 5432109876L, 5550105678L, "654 Maple Dr, Phoenix"));
        }
    }

    public void clear() {
        storage.clear();
        idCounter.set(1);
    }

    public int size() {
        return storage.size();
    }

    public List<Customer> findByCity(String city) {
        return storage.values().stream()
                .filter(customer -> customer.getAddress().toLowerCase().contains(city.toLowerCase()))
                .collect(Collectors.toList());
    }
}