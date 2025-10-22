package carrental.domain.service;

import carrental.domain.model.Car;
import carrental.domain.repository.CarRepository;
import java.util.List;
import java.util.Optional;

public class CarServise {
    private final CarRepository carRepository;

    public CarServise(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car addCar(String vin, String licensePlate, String brand,
                      String model, String status, Integer hourlyRate) {
        if (carRepository.findByVin(vin).isPresent()) {
            throw new IllegalArgumentException("Car with VIN " + vin + " already exists");
        }

        Car car = new Car(null, vin, licensePlate, brand, model, status, hourlyRate);
        return carRepository.save(car);
    }

    public Optional<Car> getCar(Integer id) {
        return carRepository.findById(id);
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getCarsByStatus(String status) {
        return carRepository.findByStatus(status);
    }

    public boolean deleteCar(Integer id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            carRepository.delete(id);
            return true;
        }
        return false;
    }
}