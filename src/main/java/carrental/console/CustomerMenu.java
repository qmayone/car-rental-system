package carrental.console;

import carrental.domain.model.Customer;
import carrental.domain.service.CustomerService;

import java.util.List;
import java.util.Scanner;

public class CustomerMenu {
    private final Scanner scanner;
    private final CustomerService customerService;

    public CustomerMenu(Scanner scanner, CustomerService customerService) {
        this.scanner = scanner;
        this.customerService = customerService;
    }

    public void showMenu() {
        while (true) {
            displayCustomerMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addCustomer();
                    break;
                case "2":
                    viewAllCustomers();
                    break;
                case "3":
                    findCustomerById();
                    break;
                case "4":
                    findCustomerByDriverLicense();
                    break;
                case "5":
                    deleteCustomer();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    pressEnterToContinue();
            }
        }
    }

    private void displayCustomerMenu() {
        System.out.println("\n--- CUSTOMER MANAGEMENT ---");
        System.out.println("1. Add New Customer");
        System.out.println("2. View All Customers");
        System.out.println("3. Find Customer by ID");
        System.out.println("4. Find Customer by Driver License");
        System.out.println("5. Delete Customer");
        System.out.println("0. Back to Main Menu");
        System.out.print("Choose an option: ");
    }

    private void addCustomer() {
        try {
            System.out.println("\n--- ADD NEW CUSTOMER ---");

            System.out.print("Enter Full Name: ");
            String fullName = scanner.nextLine();

            System.out.print("Enter Passport Number: ");
            long passport = Long.parseLong(scanner.nextLine());

            System.out.print("Enter Driver License Number: ");
            long driverLicense = Long.parseLong(scanner.nextLine());

            System.out.print("Enter Phone Number: ");
            long phone = Long.parseLong(scanner.nextLine());

            System.out.print("Enter Address: ");
            String address = scanner.nextLine();

            Customer customer = customerService.addCustomer(fullName, passport, driverLicense, phone, address);
            System.out.println("Customer added successfully with ID: " + customer.getCustomerId());

        } catch (Exception e) {
            System.out.println("Error adding customer: " + e.getMessage());
        } finally {
            pressEnterToContinue();
        }
    }

    private void viewAllCustomers() {
        System.out.println("\n--- ALL CUSTOMERS ---");
        List<Customer> customers = customerService.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found in the system.");
        } else {
            System.out.println("ID | Name | Driver License | Phone | Address");
            System.out.println("--------------------------------------------");

            for (Customer customer : customers) {
                System.out.printf("%-3d | %-20s | %-15d | %-15d | %s%n",
                        customer.getCustomerId(),
                        truncateString(customer.getFullName(), 20),
                        customer.getDriverLicense(),
                        customer.getPhone(),
                        truncateString(customer.getAddress(), 20));
            }
        }
        pressEnterToContinue();
    }

    private void findCustomerById() {
        System.out.println("\n--- FIND CUSTOMER BY ID ---");
        System.out.print("Enter Customer ID: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            Customer customer = customerService.getCustomer(id).orElse(null);
            if (customer != null) {
                displayCustomerDetails(customer);
            } else {
                System.out.println("Customer not found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
        pressEnterToContinue();
    }

    private void findCustomerByDriverLicense() {
        System.out.println("\n--- FIND CUSTOMER BY DRIVER LICENSE ---");
        System.out.print("Enter Driver License Number: ");

        try {
            long driverLicense = Long.parseLong(scanner.nextLine());
            Customer customer = customerService.getCustomerByDriverLicense(driverLicense).orElse(null);
            if (customer != null) {
                displayCustomerDetails(customer);
            } else {
                System.out.println("Customer not found with driver license: " + driverLicense);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid license number format.");
        }
        pressEnterToContinue();
    }

    private void deleteCustomer() {
        System.out.println("\n--- DELETE CUSTOMER ---");
        System.out.print("Enter Customer ID to delete: ");

        try {
            int id = Integer.parseInt(scanner.nextLine());
            if (customerService.deleteCustomer(id)) {
                System.out.println("Customer deleted successfully.");
            } else {
                System.out.println("Customer not found with ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        }
        pressEnterToContinue();
    }

    private void displayCustomerDetails(Customer customer) {
        System.out.println("\nCustomer Details:");
        System.out.println("  ID: " + customer.getCustomerId());
        System.out.println("  Full Name: " + customer.getFullName());
        System.out.println("  Passport: " + customer.getPassport());
        System.out.println("  Driver License: " + customer.getDriverLicense());
        System.out.println("  Phone: " + customer.getPhone());
        System.out.println("  Address: " + customer.getAddress());
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