package carrental.console;

import carrental.domain.model.Car;
import carrental.domain.service.CarServise;

import java.util.List;
import java.util.Scanner;

public class CarMenu {
    private final Scanner scanner;
    private final CarServise carService;

    public CarMenu(Scanner scanner, CarServise carService) {
        this.scanner = scanner;
        this.carService = carService;
    }

    public void showMenu() {
        while (true) {
            displayCarMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addCar();
                    break;
                case "2":
                    viewAllCars();
                    break;
                case "3":
                    findCarById();
                    break;
                case "4":
                    viewAvailableCars();
                    break;
                case "5":
                    deleteCar();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    pressEnterToContinue();
            }
        }
    }

    private void displayCarMenu() {
        System.out.println("\n--- CAR MANAGEMENT ---");
        System.out.println("1. Add New Car");
        System.out.println("2. View All Cars");
        System.out.println("3. Find Car by ID");
        System.out.println("4. View Available Cars");
        System.out.println("5. Delete Car");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    private void addCar() {
        try {
            System.out.println("\n--- ADD NEW CAR ---");

            System.out.print("Enter VIN (17 characters): ");
            String vin = scanner.nextLine();

            System.out.print("Enter License Plate: ");
            String licensePlate = scanner.nextLine();

            System.out.print("Enter Brand: ");
            String brand = scanner.nextLine();

            System.out.print("Enter Model: ");
            String model = scanner.nextLine();

            System.out.print("Enter Status (AVAILABLE/RENTED/MAINTENANCE): ");
            String status = scanner.nextLine();

            System.out.print("Enter Hourly Rate: ");
            int hourlyRate = Integer.parseInt(scanner.nextLine());

            Car car = carService.addCar(vin, licensePlate, brand, model, status, hourlyRate);
            System.out.println("Car added successfully with ID: " + car.getCarId());

        } catch (Exception e) {
            System.out.println("Error adding car: " + e.getMessage());
        } finally {
            pressEnterToContinue();
        }
    }

    private void viewAllCars() {
        System.out.println("\n--- ALL CARS ---");
        List<Car> cars = carService.getAllCars();

        if (cars.isEmpty()) {
            System.out.println("No cars found in the system.");
        } else {
            System.out.println("ID | VIN | License | Brand | Model | Status | Rate/hr");
            System.out.println("------------------------------------------------------");

            for (Car car : cars) {
                System.out.printf("%-3d | %-17s | %-10s | %-10s | %-10s | %-10s | $%d%n",
                        car.getCarId(), car.getVin(), car.getLicensePlate(),
                        car.getBrand(), car.getModel(), car.getStatus(), car.getHourlyRate());
            }
        }
        pressEnterToContinue();
    }

    private void findCarById() {
        System.out.println("\n--- FIND CAR BY ID ---");
        System.out.print("Enter Car ID: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            Car car = carService.getCar(id).orElse(null);
            if (car != null) {
                displayCarDetails(car);
            } else {
                System.out.println("Car not found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
        pressEnterToContinue();
    }

    private void viewAvailableCars() {
        System.out.println("\n--- AVAILABLE CARS ---");
        List<Car> availableCars = carService.getCarsByStatus("AVAILABLE");

        if (availableCars.isEmpty()) {
            System.out.println("No available cars at the moment.");
        } else {
            System.out.println("ID | License | Brand | Model | Rate/hr");
            System.out.println("--------------------------------------");

            for (Car car : availableCars) {
                System.out.printf("%-3d | %-10s | %-10s | %-10s | $%d%n",
                        car.getCarId(), car.getLicensePlate(), car.getBrand(),
                        car.getModel(), car.getHourlyRate());
            }
        }
        pressEnterToContinue();
    }

    private void deleteCar() {
        System.out.println("\n--- DELETE CAR ---");
        System.out.print("Enter Car ID to delete: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (carService.deleteCar(id)) {
                System.out.println("Car deleted successfully.");
            } else {
                System.out.println("Car not found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
        pressEnterToContinue();
    }

    private void displayCarDetails(Car car) {
        System.out.println("\nCar Details:");
        System.out.println("  ID: " + car.getCarId());
        System.out.println("  VIN: " + car.getVin());
        System.out.println("  License Plate: " + car.getLicensePlate());
        System.out.println("  Brand: " + car.getBrand());
        System.out.println("  Model: " + car.getModel());
        System.out.println("  Status: " + car.getStatus());
        System.out.println("  Hourly Rate: $" + car.getHourlyRate());
    }

    private void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}