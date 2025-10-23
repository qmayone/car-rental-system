package carrental.domain.repository;

import carrental.domain.model.Rental;
import java.util.List;
import java.util.Optional;

public interface RentalRepository {

    Rental save(Rental rental);
    Optional<Rental> findById(Integer id);
    List<Rental> findAll();
    void delete(Integer id);
    List<Rental> findByCustomerId(Integer customerId);
    List<Rental> findByCarId(Integer carId);
    List<Rental> findActiveRentals();
    List<Rental> findCompletedRentals();
    List<Rental> findByStatus(String status);
    boolean isCarCurrentlyRented(Integer carId);
    boolean hasActiveRentals(Integer customerId);
    boolean updateStatus(Integer rentalId, String status);
    boolean updateDepositStatus(Integer rentalId, String depositStatus);
    List<Rental> findByDateRange(String startDate, String endDate);
    List<Rental> findByCustomerIdAndStatus(Integer customerId, String status);
}