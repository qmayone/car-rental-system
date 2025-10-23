package carrental.console;

import carrental.domain.model.Violation;
import carrental.domain.service.ViolationService;
import carrental.domain.service.RentalService;

import java.util.List;
import java.util.Scanner;

public class ViolationMenu {
    private final Scanner scanner;
    private final ViolationService violationService;
    private final RentalService rentalService;

    public ViolationMenu(Scanner scanner, ViolationService violationService, RentalService rentalService) {
        this.scanner = scanner;
        this.violationService = violationService;
        this.rentalService = rentalService;
    }

    public void showMenu() {
        while (true) {
            displayViolationMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    recordViolation();
                    break;
                case "2":
                    viewAllViolations();
                    break;
                case "3":
                    findViolationById();
                    break;
                case "4":
                    viewRentalViolations();
                    break;
                case "5":
                    resolveViolation();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    pressEnterToContinue();
            }
        }
    }

    private void displayViolationMenu() {
        System.out.println("\n--- VIOLATION MANAGEMENT ---");
        System.out.println("1. Record New Violation");
        System.out.println("2. View All Violations");
        System.out.println("3. Find Violation by ID");
        System.out.println("4. View Rental Violations");
        System.out.println("5. Resolve Violation");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    private void recordViolation() {
        try {
            System.out.println("\n--- RECORD NEW VIOLATION ---");

            System.out.print("Enter Rental ID: ");
            int rentalId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Date/Time (YYYY-MM-DD HH:MM): ");
            String dateTime = scanner.nextLine();

            System.out.print("Enter Violation Description: ");
            String description = scanner.nextLine();

            System.out.print("Enter Fine Amount: ");
            int fineAmount = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Status (PENDING/PAID): ");
            String status = scanner.nextLine();

            Violation violation = violationService.recordViolation(rentalId, dateTime, description, fineAmount, status);
            System.out.println("Violation recorded successfully with ID: " + violation.getViolationId());

        } catch (Exception e) {
            System.out.println("Error recording violation: " + e.getMessage());
        } finally {
            pressEnterToContinue();
        }
    }

    private void viewAllViolations() {
        System.out.println("\n--- ALL VIOLATIONS ---");
        List<Violation> violations = violationService.getAllViolations();

        if (violations.isEmpty()) {
            System.out.println("No violations found in the system.");
        } else {
            System.out.println("ID | Rental ID | Date/Time | Description | Fine | Status");
            System.out.println("---------------------------------------------------------");

            for (Violation violation : violations) {
                System.out.printf("%-3d | %-9d | %-10s | %-20s | $%-4d | %s%n",
                        violation.getViolationId(), violation.getRentalId(),
                        violation.getDateTime(),
                        truncateString(violation.getDescription(), 20),
                        violation.getFineAmount(), violation.getStatus());
            }
        }
        pressEnterToContinue();
    }

    private void findViolationById() {
        System.out.println("\n--- FIND VIOLATION BY ID ---");
        System.out.print("Enter Violation ID: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            Violation violation = violationService.getViolation(id).orElse(null);
            if (violation != null) {
                displayViolationDetails(violation);
            } else {
                System.out.println("Violation not found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
        pressEnterToContinue();
    }

    private void viewRentalViolations() {
        System.out.println("\n--- RENTAL VIOLATIONS ---");
        System.out.print("Enter Rental ID: ");

        try {
            int rentalId = Integer.parseInt(scanner.nextLine());
            List<Violation> violations = violationService.getViolationsByRentalId(rentalId);

            if (violations.isEmpty()) {
                System.out.println("No violations found for rental ID: " + rentalId);
            } else {
                System.out.println("Violations for Rental ID: " + rentalId);
                for (Violation violation : violations) {
                    displayViolationDetails(violation);
                    System.out.println("---");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
        pressEnterToContinue();
    }

    private void resolveViolation() {
        System.out.println("\n--- RESOLVE VIOLATION ---");
        System.out.print("Enter Violation ID to resolve: ");

        try {
            int violationId = Integer.parseInt(scanner.nextLine());
            if (violationService.resolveViolation(violationId)) {
                System.out.println("Violation resolved successfully.");
            } else {
                System.out.println("Violation not found with ID: " + violationId);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
        pressEnterToContinue();
    }

    private void displayViolationDetails(Violation violation) {
        System.out.println("\nViolation Details:");
        System.out.println("  ID: " + violation.getViolationId());
        System.out.println("  Rental ID: " + violation.getRentalId());
        System.out.println("  Date/Time: " + violation.getDateTime());
        System.out.println("  Description: " + violation.getDescription());
        System.out.println("  Fine Amount: $" + violation.getFineAmount());
        System.out.println("  Status: " + violation.getStatus());
    }

    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }

    private void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");
        scanner.nextLine();
    }
}