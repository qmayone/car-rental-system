package carrental.domain.repository;

import carrental.domain.model.Violation;
import java.util.List;
import java.util.Optional;

public interface ViolationRepository {

    Violation save(Violation violation);
    Optional<Violation> findById(Integer id);
    List<Violation> findAll();
    void delete(Integer id);
    List<Violation> findByRentalId(Integer rentalId);
    List<Violation> findByStatus(String status);
    List<Violation> findPendingViolations();
    List<Violation> findPaidViolations();
    List<Violation> findByFineAmountGreaterThan(Integer minAmount);
    List<Violation> findByDateRange(String startDate, String endDate);
    boolean updateStatus(Integer violationId, String status);
    boolean updateFineAmount(Integer violationId, Integer fineAmount);
    boolean existsByRentalId(Integer rentalId);
    Integer getTotalFinesByRentalId(Integer rentalId);
    Integer getTotalPendingFinesByRentalId(Integer rentalId);
    List<Violation> findByRentalIdAndStatus(Integer rentalId, String status);
}