package carrental.domain.model;

public class Rental {
    private final Integer rentalId;
    private final Integer customerId;
    private final Integer carId;
    private final Integer dateStart;
    private final Integer dateEnd;
    private final Integer costFact;
    private final String depositeStatus;
    private final String status;

    public Rental(Integer rentalId, Integer customerId, Integer carId,
                  Integer dateStart, Integer dateEnd, Integer costFact,
                  String depositeStatus, String status) {
        this.rentalId = rentalId;
        this.customerId = customerId;
        this.carId = carId;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.costFact = costFact;
        this.depositeStatus = depositeStatus;
        this.status = status;
    }

    // Getters
    public Integer getRentalId() { return rentalId; }
    public Integer getCustomerId() { return customerId; }
    public Integer getCarId() { return carId; }
    public Integer getDateStart() { return dateStart; }
    public Integer getDateEnd() { return dateEnd; }
    public Integer getCostFact() { return costFact; }
    public String getDepositeStatus() { return depositeStatus; }
    public String getStatus() { return status; }
}