package carrental.domain.model;

public class Customer {
    private final Integer customerId;
    private final String fullName;
    private final Long passport;
    private final Long driverLicense;
    private final Long phone;
    private final String address;

    public Customer(Integer customerId, String fullName, Long passport,
                    Long driverLicense, Long phone, String address) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.passport = passport;
        this.driverLicense = driverLicense;
        this.phone = phone;
        this.address = address;
    }

    // Getters
    public Integer getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public Long getPassport() { return passport; }
    public Long getDriverLicense() { return driverLicense; }
    public Long getPhone() { return phone; }
    public String getAddress() { return address; }
}