package carrental.domain.service;

import carrental.domain.model.Rental;
import carrental.domain.model.Car;
import carrental.domain.model.Customer;
import carrental.domain.repository.RentalRepository;
import carrental.domain.repository.CarRepository;
import carrental.domain.repository.CustomerRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
//import java.time.temporal.ChronoUnit;
import java.util.Arrays;
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

    public Rental createRental(Integer customerId, Integer carId, String dateStart,
                               String dateEnd, Integer costFact, String depositeStatus) {

        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Valid customer ID is required");
        }

        if (carId == null || carId <= 0) {
            throw new IllegalArgumentException("Valid car ID is required");
        }

        if (!isValidDate(dateStart) || !isValidDate(dateEnd)) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
        }

        if (costFact == null || costFact <= 0) {
            throw new IllegalArgumentException("Cost must be positive");
        }

        if (depositeStatus == null || depositeStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Deposit status is required");
        }

        try {
            LocalDate start = LocalDate.parse(dateStart);
            LocalDate end = LocalDate.parse(dateEnd);
            if (end.isBefore(start) || end.isEqual(start)) {
                throw new IllegalArgumentException("End date must be after start date");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
        }

        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            throw new IllegalArgumentException("Customer not found with ID: " + customerId);
        }

        Optional<Car> car = carRepository.findById(carId);
        if (!car.isPresent()) {
            throw new IllegalArgumentException("Car not found with ID: " + carId);
        }

        if (!"AVAILABLE".equals(car.get().getStatus())) {
            throw new IllegalStateException("Car is not available for rental. Current status: " + car.get().getStatus());
        }

        if (rentalRepository.isCarCurrentlyRented(carId)) {
            throw new IllegalStateException("Car is currently rented");
        }

        if (!canCustomerRent(customerId)) {
            throw new IllegalStateException("Customer is not eligible to rent a car");
        }

        Rental rental = new Rental(null, customerId, carId, dateStart, dateEnd,
                costFact, depositeStatus, "ACTIVE");

        Rental savedRental = rentalRepository.save(rental);

        carRepository.updateStatus(carId, "RENTED");

        return savedRental;
    }

    public Optional<Rental> getRental(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid rental ID");
        }
        return rentalRepository.findById(id);
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public List<Rental> getCustomerRentals(Integer customerId) {
        if (customerId == null || customerId <= 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        return rentalRepository.findByCustomerId(customerId);
    }

    public List<Rental> getCarRentals(Integer carId) {
        if (carId == null || carId <= 0) {
            throw new IllegalArgumentException("Invalid car ID");
        }
        return rentalRepository.findByCarId(carId);
    }

    public boolean completeRental(Integer rentalId) {
        if (rentalId == null || rentalId <= 0) {
            throw new IllegalArgumentException("Invalid rental ID");
        }

        Optional<Rental> rental = rentalRepository.findById(rentalId);
        if (rental.isPresent()) {
            boolean rentalUpdated = rentalRepository.updateStatus(rentalId, "COMPLETED");

            if (rentalUpdated) {
                carRepository.updateStatus(rental.get().getCarId(), "AVAILABLE");
                return true;
            }
        }
        return false;
    }

    public List<Rental> getActiveRentals() {
        return rentalRepository.findActiveRentals();
    }

    public List<Rental> getCompletedRentals() {
        return rentalRepository.findCompletedRentals();
    }

    public boolean updateDepositStatus(Integer rentalId, String depositStatus) {
        if (rentalId == null || rentalId <= 0) {
            throw new IllegalArgumentException("Invalid rental ID");
        }

        if (depositStatus == null || depositStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Deposit status is required");
        }

        List<String> validStatuses = Arrays.asList("PAID", "REFUNDED", "PENDING");
        if (!validStatuses.contains(depositStatus.toUpperCase())) {
            throw new IllegalArgumentException("Invalid deposit status. Must be: PAID, REFUNDED, or PENDING");
        }

        return rentalRepository.updateDepositStatus(rentalId, depositStatus);
    }

    private boolean canCustomerRent(Integer customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (!customer.isPresent()) {
            return false;
        }

        List<Rental> activeRentals = rentalRepository.findByCustomerIdAndStatus(customerId, "ACTIVE");
        if (activeRentals.size() >= 2) {
            return false;
        }

        return true;
    }

//    public int calculateRentalDuration(Integer rentalId) {
//        if (rentalId == null || rentalId <= 0) {
//            throw new IllegalArgumentException("Invalid rental ID");
//        }
//
//        Optional<Rental> rental = rentalRepository.findById(rentalId);
//        if (rental.isPresent()) {
//            String startDate = rental.get().getDateStart();
//            String endDate = rental.get().getDateEnd();
//
//            try {
//                LocalDate start = LocalDate.parse(startDate);
//                LocalDate end = LocalDate.parse(endDate);
//                return (int) ChronoUnit.DAYS.between(start, end);
//            } catch (DateTimeParseException e) {
//                throw new IllegalArgumentException("Invalid date format in rental: " + rentalId);
//            }
//        }
//
//        throw new IllegalArgumentException("Rental not found with ID: " + rentalId);
//    }

    public boolean isCarAvailableForRental(Integer carId) {
        if (carId == null || carId <= 0) {
            throw new IllegalArgumentException("Invalid car ID");
        }

        Optional<Car> car = carRepository.findById(carId);
        if (!car.isPresent()) {
            return false;
        }

        return "AVAILABLE".equals(car.get().getStatus()) &&
                !rentalRepository.isCarCurrentlyRented(carId);
    }

    private boolean isValidDate(String date) {
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}