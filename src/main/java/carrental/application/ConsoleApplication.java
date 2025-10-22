package carrental.application;

import carrental.domain.repository.*;
import carrental.domain.service.*;
import carrental.infrastructure.*;
import carrental.console.ConsoleUi;

public class ConsoleApplication {
    public static void main(String[] args) {
        // Initialize repositories
        CarRepository carRepository = new InMemoryCarRepository();
        CustomerRepository customerRepository = new InMemoryCustomerRepository();
        RentalRepository rentalRepository = new InMemoryRentalRepository();
        ViolationRepository violationRepository = new InMemoryViolationRepository();

        // Initialize services
        CarService carService = new CarService(carRepository);
        CustomerService customerService = new CustomerService(customerRepository);
        RentalService rentalService = new RentalService(rentalRepository, carRepository, customerRepository);
        ViolationService violationService = new ViolationService(violationRepository, rentalRepository);

        // Initialize and start UI
        ConsoleUI consoleUI = new ConsoleUI(carService, customerService, rentalService, violationService);
        consoleUI.start();
    }
}