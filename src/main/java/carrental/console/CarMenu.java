package carrental.console;

import carrental.domain.service.CarService;
import carrental.domain.model.Car;
import java.util.List;
import java.util.Scanner;

public class CarMenu {
    private final Scanner scanner;
    private final CarService carService;

    public CarMenu(Scanner scanner, CarService carService) {
        this.scanner = scanner;
        this.carService = carService;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Car Management ---");
            System.out.println("1. Add Car");
            System.out.println("2. View All Cars");
            System.out.println("3. Find Car by ID");
            System.out.println("4. View Available Cars");
            System.out.println("5. Delete Car");
            System.out.println("0. Back to Main Menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

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
            }
        }
    }

    private void addCar() {
        try {
            System.out.print("Enter VIN: ");
            String vin = scanner.nextLine();
            System.out.print("Enter License Plate: ");
            String licensePlate = scanner.nextLine();
            System.out.print("Enter Brand: ");
            String brand = scanner.nextLine();
            System.out.print("Enter Model: ");
            String model = scanner.nextLine();
            System.out.print("Enter Status: ");
            String status = scanner.nextLine();
            System.out.print("Enter Hourly Rate: ");
            Integer hourlyRate = Integer.parseInt(scanner.nextLine());

            Car car = carService.addCar(vin, licensePlate, brand, model, status, hourlyRate);
            System.out.println("Car added successfully with ID: " + car.getCarId());
        } catch (Exception e) {
            System.out.println("Error adding car: " + e.getMessage());
        }
    }

    private void viewAllCars() {
        List<Car> cars = carService.getAllCars();
        if (cars.isEmpty()) {
            System.out.println("No cars found.");
        } else {
            System.out.println("\n--- All Cars ---");
            for (Car car : cars) {
                displayCar(car);
            }
        }
    }

    private void displayCar(Car car) {
        System.out.printf("ID: %d, %s %s, License: %s, VIN: %s, Status: %s, Rate: $%d/hour%n",
                car.getCarId(), car.getBrand(), car.getModel(),
                car.getLicensePlate(), car.getVin(), car.getStatus(), car.getHourlyRate());
    }

    private void findCarById() {
        try {
            System.out.print("Enter Car ID: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            carService.getCar(id).ifPresentOrElse(
                    this::displayCar,
                    () -> System.out.println("Car not found with ID: " + id)
            );
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }

    private void viewAvailableCars() {
        List<Car> availableCars = carService.getCarsByStatus("AVAILABLE");
        if (availableCars.isEmpty()) {
            System.out.println("No available cars found.");
        } else {
            System.out.println("\n--- Available Cars ---");
            for (Car car : availableCars) {
                displayCar(car);
            }
        }
    }

    private void deleteCar() {
        try {
            System.out.print("Enter Car ID to delete: ");
            Integer id = Integer.parseInt(scanner.nextLine());
            if (carService.deleteCar(id)) {
                System.out.println("Car deleted successfully.");
            } else {
                System.out.println("Car not found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
    }
}