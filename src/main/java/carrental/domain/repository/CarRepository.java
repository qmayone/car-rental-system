package carrental.domain.repository;

import carrental.domain.model.Car;
import java.util.List;
import java.util.Optional;

public interface CarRepository {
    Car save(Car car);
    Optional<Car> findById(Integer id);
    List<Car> findAll();
    void delete(Integer id);
    Optional<Car> findByVin(String vin);
    List<Car> findByStatus(String status);
}