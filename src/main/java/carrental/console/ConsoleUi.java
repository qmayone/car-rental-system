package carrental.console;

import carrental.domain.service.CarServise;
import carrental.domain.service.CustomerService;
import carrental.domain.service.RentalService;
import carrental.domain.service.ViolationService;

import java.util.Scanner;

public class ConsoleUi {
    private final Scanner scanner;
    private final CarMenu carMenu;
    private final CustomerMenu customerMenu;
    private final RentalMenu rentalMenu;
    private final ViolationMenu violationMenu;

    public ConsoleUi(CarServise carService,
                     CustomerService customerService,
                     RentalService rentalService,
                     ViolationService violationService) {
        this.scanner = new Scanner(System.in);
        this.carMenu = new CarMenu(scanner, carService);
        this.customerMenu = new CustomerMenu(scanner, customerService);
        this.rentalMenu = new RentalMenu(scanner, rentalService, carService, customerService);
        this.violationMenu = new ViolationMenu(scanner, violationService, rentalService);
    }

    public void start() {
        displayWelcomeMessage();

        while (true) {
            displayMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    carMenu.showMenu();
                    break;
                case "2":
                    customerMenu.showMenu();
                    break;
                case "3":
                    rentalMenu.showMenu();
                    break;
                case "4":
                    violationMenu.showMenu();
                    break;
                case "0":
                    System.out.println("\nThank you for using Car Rental System. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    pressEnterToContinue();
            }
        }
    }

    private void displayWelcomeMessage() {
        System.out.println("==========================================");
        System.out.println("      CAR RENTAL MANAGEMENT SYSTEM");
        System.out.println("==========================================");
        System.out.println("Welcome to the Car Rental Management System!");
    }

    private void displayMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Car Management");
        System.out.println("2. Customer Management");
        System.out.println("3. Rental Management");
        System.out.println("4. Violation Management");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}