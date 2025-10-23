package carrental.infrastructure;

import carrental.domain.model.Car;
import carrental.domain.repository.CarRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryCarRepository implements CarRepository {
    private final Map<Integer, Car> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public Car save(Car car) {
        if (car.getCarId() == null) {
            Car newCar = new Car(
                    idCounter.getAndIncrement(),
                    car.getVin(),
                    car.getLicensePlate(),
                    car.getBrand(),
                    car.getModel(),
                    car.getStatus(),
                    car.getHourlyRate()
            );
            storage.put(newCar.getCarId(), newCar);
            return newCar;
        } else {
            storage.put(car.getCarId(), car);
            return car;
        }
    }

    @Override
    public Optional<Car> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Integer id) {
        storage.remove(id);
    }

    @Override
    public Optional<Car> findByVin(String vin) {
        return storage.values().stream()
                .filter(car -> car.getVin().equalsIgnoreCase(vin))
                .findFirst();
    }

    @Override
    public List<Car> findByStatus(String status) {
        return storage.values().stream()
                .filter(car -> car.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<Car> findByBrand(String brand) {
        return storage.values().stream()
                .filter(car -> car.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByVin(String vin) {
        return storage.values().stream()
                .anyMatch(car -> car.getVin().equalsIgnoreCase(vin));
    }

    @Override
    public boolean updateStatus(Integer carId, String status) {
        Optional<Car> carOpt = findById(carId);
        if (carOpt.isPresent()) {
            Car car = carOpt.get();
            Car updatedCar = new Car(
                    car.getCarId(),
                    car.getVin(),
                    car.getLicensePlate(),
                    car.getBrand(),
                    car.getModel(),
                    status,
                    car.getHourlyRate()
            );
            storage.put(carId, updatedCar);
            return true;
        }
        return false;
    }

    public void initializeWithSampleData() {
        if (storage.isEmpty()) {
            save(new Car(null, "1HGCM82633A123456", "ABC123", "Toyota", "Camry", "AVAILABLE", 25));
            save(new Car(null, "2FMDK3GC5DBA45678", "XYZ789", "Honda", "Civic", "AVAILABLE", 20));
            save(new Car(null, "1G1ZE5ST1GF123789", "DEF456", "Ford", "Focus", "RENTED", 22));
            save(new Car(null, "5YJSA1CN5DFP12345", "GHI789", "Tesla", "Model 3", "MAINTENANCE", 50));
            save(new Car(null, "WAUZZZ8V3KA123456", "JKL012", "Audi", "A4", "AVAILABLE", 45));
        }
    }

    public void clear() {
        storage.clear();
        idCounter.set(1);
    }

    public int size() {
        return storage.size();
    }
}