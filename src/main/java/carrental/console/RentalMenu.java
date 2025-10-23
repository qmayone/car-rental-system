package carrental.console;

import carrental.domain.model.Rental;
import carrental.domain.model.Car;
import carrental.domain.service.RentalService;
import carrental.domain.service.CarServise;
import carrental.domain.service.CustomerService;

import java.util.List;
import java.util.Scanner;

public class RentalMenu {
    private final Scanner scanner;
    private final RentalService rentalService;
    private final CarServise carService;
    private final CustomerService customerService;

    public RentalMenu(Scanner scanner, RentalService rentalService,
                      CarServise carService, CustomerService customerService) {
        this.scanner = scanner;
        this.rentalService = rentalService;
        this.carService = carService;
        this.customerService = customerService;
    }

    public void showMenu() {
        while (true) {
            displayRentalMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createRental();
                    break;
                case "2":
                    viewAllRentals();
                    break;
                case "3":
                    viewCustomerRentals();
                    break;
                case "4":
                    completeRental();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    pressEnterToContinue();
            }
        }
    }

    private void displayRentalMenu() {
        System.out.println("\n--- RENTAL MANAGEMENT ---");
        System.out.println("1. Create New Rental");
        System.out.println("2. View All Rentals");
        System.out.println("3. View Customer Rentals");
        System.out.println("4. Complete Rental");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    private void createRental() {
        try {
            System.out.println("\n--- CREATE NEW RENTAL ---");

            // Показать доступные автомобили
            List<Car> availableCars = carService.getCarsByStatus("AVAILABLE");
            if (availableCars.isEmpty()) {
                System.out.println("No available cars for rental.");
                pressEnterToContinue();
                return;
            }

            System.out.println("Available Cars:");
            for (Car car : availableCars) {
                System.out.printf("  ID: %d, %s %s, License: %s, Rate: $%d/hour%n",
                        car.getCarId(), car.getBrand(), car.getModel(),
                        car.getLicensePlate(), car.getHourlyRate());
            }

            System.out.print("Enter Customer ID: ");
            int customerId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Car ID: ");
            int carId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Start Date (YYYY-MM-DD): ");
            String dateStart = scanner.nextLine();

            System.out.print("Enter End Date (YYYY-MM-DD): ");
            String dateEnd = scanner.nextLine();

            System.out.print("Enter Actual Cost: ");
            int costFact = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Deposit Status (PAID/REFUNDED): ");
            String depositeStatus = scanner.nextLine();

            Rental rental = rentalService.createRental(customerId, carId, dateStart, dateEnd, costFact, depositeStatus);
            System.out.println("Rental created successfully with ID: " + rental.getRentalId());

        } catch (Exception e) {
            System.out.println("Error creating rental: " + e.getMessage());
        } finally {
            pressEnterToContinue();
        }
    }

    private void viewAllRentals() {
        System.out.println("\n--- ALL RENTALS ---");
        List<Rental> rentals = rentalService.getAllRentals();

        if (rentals.isEmpty()) {
            System.out.println("No rentals found in the system.");
        } else {
            System.out.println("ID | Customer ID | Car ID | Start Date | End Date | Cost | Deposit | Status");
            System.out.println("----------------------------------------------------------------------------");

            for (Rental rental : rentals) {
                System.out.printf("%-3d | %-11d | %-6d | %-10s | %-8s | $%-4d | %-8s | %s%n",
                        rental.getRentalId(), rental.getCustomerId(), rental.getCarId(),
                        rental.getDateStart(), rental.getDateEnd(), rental.getCostFact(),
                        rental.getDepositeStatus(), rental.getStatus());
            }
        }
        pressEnterToContinue();
    }

    private void viewCustomerRentals() {
        System.out.println("\n--- CUSTOMER RENTALS ---");
        System.out.print("Enter Customer ID: ");

        try {
            int customerId = Integer.parseInt(scanner.nextLine());
            List<Rental> rentals = rentalService.getCustomerRentals(customerId);

            if (rentals.isEmpty()) {
                System.out.println("No rentals found for customer ID: " + customerId);
            } else {
                System.out.println("Rentals for Customer ID: " + customerId);
                System.out.println("ID | Car ID | Start Date | End Date | Cost | Status");
                System.out.println("---------------------------------------------------");

                for (Rental rental : rentals) {
                    System.out.printf("%-3d | %-6d | %-10s | %-8s | $%-4d | %s%n",
                            rental.getRentalId(), rental.getCarId(),
                            rental.getDateStart(), rental.getDateEnd(), rental.getCostFact(),
                            rental.getStatus());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
        pressEnterToContinue();
    }

    private void completeRental() {
        System.out.println("\n--- COMPLETE RENTAL ---");
        System.out.print("Enter Rental ID to complete: ");

        try {
            int rentalId = Integer.parseInt(scanner.nextLine());
            if (rentalService.completeRental(rentalId)) {
                System.out.println("Rental completed successfully.");
            } else {
                System.out.println("Rental not found with ID: " + rentalId);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
        pressEnterToContinue();
    }

    private void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}