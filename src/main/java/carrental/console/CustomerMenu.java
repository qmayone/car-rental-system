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
            MenuHelper.displaySectionHeader("CUSTOMER MANAGEMENT");
            System.out.println("1.  Add New Customer");
            System.out.println("2.  View All Customers");
            System.out.println("3.  Find Customer by ID");
            System.out.println("4.  Find Customer by Driver License");
            System.out.println("5.  Delete Customer");
            System.out.println("0.  Back to Main Menu");
            System.out.print("\nChoose an option: ");

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
                    System.out.println(" Invalid choice. Please try again.");
                    MenuHelper.pressEnterToContinue(scanner);
            }
        }
    }

    private void addCustomer() {
        try {
            MenuHelper.displaySubHeader("ADD NEW CUSTOMER");

            String fullName = MenuHelper.readRequiredString(scanner, "Enter Full Name: ");
            long passport = MenuHelper.readLongInput(scanner, "Enter Passport Number: ");
            long driverLicense = MenuHelper.readLongInput(scanner, "Enter Driver License Number: ");
            long phone = MenuHelper.readLongInput(scanner, "Enter Phone Number: ");
            String address = MenuHelper.readRequiredString(scanner, "Enter Address: ");

            Customer customer = customerService.addCustomer(fullName, passport, driverLicense, phone, address);
            System.out.println(" Customer added successfully with ID: " + customer.getCustomerId());

        } catch (Exception e) {
            System.out.println(" Error adding customer: " + e.getMessage());
        } finally {
            MenuHelper.pressEnterToContinue(scanner);
        }
    }

    private void viewAllCustomers() {
        MenuHelper.displaySubHeader("ALL CUSTOMERS");
        List<Customer> customers = customerService.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found in the system.");
        } else {
            System.out.printf("%-4s %-20s %-15s %-15s %-12s%n",
                    "ID", "Name", "Driver License", "Phone", "Address");
            System.out.println("-".repeat(80));

            for (Customer customer : customers) {
                String shortAddress = customer.getAddress().length() > 15 ?
                        customer.getAddress().substring(0, 12) + "..." : customer.getAddress();

                System.out.printf("%-4d %-20s %-15d %-15d %-12s%n",
                        customer.getCustomerId(),
                        customer.getFullName().length() > 20 ?
                                customer.getFullName().substring(0, 17) + "..." : customer.getFullName(),
                        customer.getDriverLicense(),
                        customer.getPhone(),
                        shortAddress);
            }
        }
        MenuHelper.pressEnterToContinue(scanner);
    }

    private void findCustomerById() {
        MenuHelper.displaySubHeader("FIND CUSTOMER BY ID");
        int id = MenuHelper.readIntInput(scanner, "Enter Customer ID: ");

        customerService.getCustomer(id).ifPresentOrElse(
                this::displayCustomerDetails,
                () -> System.out.println(" Customer not found with ID: " + id)
        );
        MenuHelper.pressEnterToContinue(scanner);
    }

    private void findCustomerByDriverLicense() {
        MenuHelper.displaySubHeader("FIND CUSTOMER BY DRIVER LICENSE");
        long driverLicense = MenuHelper.readLongInput(scanner, "Enter Driver License Number: ");

        customerService.getCustomerByDriverLicense(driverLicense).ifPresentOrElse(
                this::displayCustomerDetails,
                () -> System.out.println(" Customer not found with driver license: " + driverLicense)
        );
        MenuHelper.pressEnterToContinue(scanner);
    }

    private void deleteCustomer() {
        MenuHelper.displaySubHeader("DELETE CUSTOMER");
        int id = MenuHelper.readIntInput(scanner, "Enter Customer ID to delete: ");

        if (customerService.deleteCustomer(id)) {
            System.out.println(" Customer deleted successfully.");
        } else {
            System.out.println(" Customer not found with ID: " + id);
        }
        MenuHelper.pressEnterToContinue(scanner);
    }

    private void displayCustomerDetails(Customer customer) {
        System.out.println("\n👤 Customer Details:");
        System.out.println("  ID: " + customer.getCustomerId());
        System.out.println("  Full Name: " + customer.getFullName());
        System.out.println("  Passport: " + customer.getPassport());
        System.out.println("  Driver License: " + customer.getDriverLicense());
        System.out.println("  Phone: " + customer.getPhone());
        System.out.println("  Address: " + customer.getAddress());
    }
}