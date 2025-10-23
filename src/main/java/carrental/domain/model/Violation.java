package carrental.domain.model;

public class Violation {
    private final Integer violationId;
    private final Integer rentalId;
    private final String dateTime;
    private final String description;
    private final Integer fineAmount;
    private final String status;

    public Violation(Integer violationId, Integer rentalId, String dateTime,
                     String description, Integer fineAmount, String status) {
        this.violationId = violationId;
        this.rentalId = rentalId;
        this.dateTime = dateTime;
        this.description = description;
        this.fineAmount = fineAmount;
        this.status = status;
    }

    // Getters
    public Integer getViolationId() { return violationId; }
    public Integer getRentalId() { return rentalId; }
    public String getDateTime() { return dateTime; }
    public String getDescription() { return description; }
    public Integer getFineAmount() { return fineAmount; }
    public String getStatus() { return status; }
}