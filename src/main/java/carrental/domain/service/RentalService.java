package carrental.domain.service;

import carrental.domain.model.Rental;
import carrental.domain.model.Car;
import carrental.domain.model.Customer;
import carrental.domain.repository.RentalRepository;
import carrental.domain.repository.CarRepository;
import carrental.domain.repository.CustomerRepository;
import java.util.List;
import java.util.Optional;

public class RentalService {
    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;

    public RentalService(RentalRepository rentalRepository,
                         CarRepository carRepository,
                         CustomerRepository customerRepository) {
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
    }

    public Rental createRental(Integer customerId, Integer carId, Integer dateStart,
                               Integer dateEnd, Integer costFact, String depositeStatus) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        Optional<Car> car = carRepository.findById(carId);

        if (customer.isEmpty() || car.isEmpty()) {
            throw new IllegalArgumentException("Customer or car not found");
        }

        Rental rental = new Rental(null, customerId, carId, dateStart, dateEnd,
                costFact, depositeStatus, "ACTIVE");
        return rentalRepository.save(rental);
    }

    public List<Rental> getCustomerRentals(Integer customerId) {
        return rentalRepository.findByCustomerId(customerId);
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public boolean completeRental(Integer rentalId) {
        Optional<Rental> rental = rentalRepository.findById(rentalId);
        if (rental.isPresent()) {
            Rental updatedRental = new Rental(rentalId, rental.get().getCustomerId(),
                    rental.get().getCarId(), rental.get().getDateStart(),
                    rental.get().getDateEnd(), rental.get().getCostFact(),
                    rental.get().getDepositeStatus(), "COMPLETED");
            rentalRepository.save(updatedRental);
            return true;
        }
        return false;
    }
}