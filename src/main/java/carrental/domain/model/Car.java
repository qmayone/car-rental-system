package carrental.domain.model;

public class Car {
    private final Integer carId;
    private final String vin;
    private final String licensePlate;
    private final String brand;
    private final String model;
    private final String status;
    private final Integer hourlyRate;

    public Car(Integer carId, String vin, String licensePlate, String brand,
               String model, String status, Integer hourlyRate) {
        this.carId = carId;
        this.vin = vin;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.status = status;
        this.hourlyRate = hourlyRate;
    }

    // Геттеры...
    public Integer getCarId() { return carId; }
    public String getVin() { return vin; }
    public String getLicensePlate() { return licensePlate; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getStatus() { return status; }
    public Integer getHourlyRate() { return hourlyRate; }
}