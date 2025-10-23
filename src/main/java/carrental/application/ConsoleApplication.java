package carrental.application;

import carrental.domain.repository.*;
import carrental.domain.service.*;
import carrental.infrastructure.*;
import carrental.console.ConsoleUi;

public class ConsoleApplication {
    public static void main(String[] args) {
        CarRepository carRepository = new InMemoryCarRepository();
        CustomerRepository customerRepository = new InMemoryCustomerRepository();
        RentalRepository rentalRepository = new InMemoryRentalRepository();
        ViolationRepository violationRepository = new InMemoryViolationRepository();

        CarServise carService = new CarServise(carRepository);
        CustomerService customerService = new CustomerService(customerRepository);
        RentalService rentalService = new RentalService(rentalRepository, carRepository, customerRepository);
        ViolationService violationService = new ViolationService(violationRepository, rentalRepository);

        ConsoleUi consoleUI = new ConsoleUi(carService, customerService, rentalService, violationService);
        consoleUI.start();
    }
}