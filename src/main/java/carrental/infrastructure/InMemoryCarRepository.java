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
            Car newCar = new Car(idCounter.getAndIncrement(), car.getVin(),
                    car.getLicensePlate(), car.getBrand(), car.getModel(),
                    car.getStatus(), car.getHourlyRate());
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
                .filter(car -> car.getVin().equals(vin))
                .findFirst();
    }

    @Override
    public List<Car> findByStatus(String status) {
        return storage.values().stream()
                .filter(car -> car.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}