package carrental.infrastructure;

import carrental.domain.model.Violation;
import carrental.domain.repository.ViolationRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryViolationRepository implements ViolationRepository {
    private final Map<Integer, Violation> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public Violation save(Violation violation) {
        if (violation.getViolationId() == null) {
            Violation newViolation = new Violation(
                    idCounter.getAndIncrement(),
                    violation.getRentalId(),
                    violation.getDateTime(),
                    violation.getDescription(),
                    violation.getFineAmount(),
                    violation.getStatus()
            );
            storage.put(newViolation.getViolationId(), newViolation);
            return newViolation;
        } else {
            storage.put(violation.getViolationId(), violation);
            return violation;
        }
    }

    @Override
    public Optional<Violation> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Violation> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Integer id) {
        storage.remove(id);
    }

    @Override
    public List<Violation> findByRentalId(Integer rentalId) {
        return storage.values().stream()
                .filter(violation -> violation.getRentalId().equals(rentalId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Violation> findByStatus(String status) {
        return storage.values().stream()
                .filter(violation -> violation.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<Violation> findPendingViolations() {
        return findByStatus("PENDING");
    }

    @Override
    public List<Violation> findPaidViolations() {
        return findByStatus("PAID");
    }

    @Override
    public List<Violation> findByFineAmountGreaterThan(Integer minAmount) {
        return storage.values().stream()
                .filter(violation -> violation.getFineAmount() > minAmount)
                .collect(Collectors.toList());
    }

    @Override
    public List<Violation> findByDateRange(String startDate, String endDate) {
        try {
            LocalDate rangeStart = LocalDate.parse(startDate);
            LocalDate rangeEnd = LocalDate.parse(endDate);

            return storage.values().stream()
                    .filter(violation -> {
                        try {
                            String violationDateStr = violation.getDateTime().substring(0, 10); // Extract YYYY-MM-DD
                            LocalDate violationDate = LocalDate.parse(violationDateStr);
                            return !violationDate.isBefore(rangeStart) && !violationDate.isAfter(rangeEnd);
                        } catch (DateTimeParseException e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (DateTimeParseException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean updateStatus(Integer violationId, String status) {
        Optional<Violation> violationOpt = findById(violationId);
        if (violationOpt.isPresent()) {
            Violation violation = violationOpt.get();
            Violation updatedViolation = new Violation(
                    violation.getViolationId(),
                    violation.getRentalId(),
                    violation.getDateTime(),
                    violation.getDescription(),
                    violation.getFineAmount(),
                    status
            );
            storage.put(violationId, updatedViolation);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateFineAmount(Integer violationId, Integer fineAmount) {
        Optional<Violation> violationOpt = findById(violationId);
        if (violationOpt.isPresent()) {
            Violation violation = violationOpt.get();
            Violation updatedViolation = new Violation(
                    violation.getViolationId(),
                    violation.getRentalId(),
                    violation.getDateTime(),
                    violation.getDescription(),
                    fineAmount,
                    violation.getStatus()
            );
            storage.put(violationId, updatedViolation);
            return true;
        }
        return false;
    }

    @Override
    public boolean existsByRentalId(Integer rentalId) {
        return storage.values().stream()
                .anyMatch(violation -> violation.getRentalId().equals(rentalId));
    }

    @Override
    public Integer getTotalFinesByRentalId(Integer rentalId) {
        return storage.values().stream()
                .filter(violation -> violation.getRentalId().equals(rentalId))
                .mapToInt(Violation::getFineAmount)
                .sum();
    }

    @Override
    public Integer getTotalPendingFinesByRentalId(Integer rentalId) {
        return storage.values().stream()
                .filter(violation -> violation.getRentalId().equals(rentalId) &&
                        "PENDING".equalsIgnoreCase(violation.getStatus()))
                .mapToInt(Violation::getFineAmount)
                .sum();
    }

    @Override
    public List<Violation> findByRentalIdAndStatus(Integer rentalId, String status) {
        return storage.values().stream()
                .filter(violation -> violation.getRentalId().equals(rentalId) &&
                        violation.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    /**
     * Gets the total revenue from paid fines
     */
    public Integer getTotalRevenueFromFines() {
        return storage.values().stream()
                .filter(violation -> "PAID".equalsIgnoreCase(violation.getStatus()))
                .mapToInt(Violation::getFineAmount)
                .sum();
    }

    public void initializeWithSampleData() {
        if (storage.isEmpty()) {
            save(new Violation(null, 1, "2024-12-11 14:30", "Speeding ticket - 15mph over limit", 150, "PAID"));
            save(new Violation(null, 1, "2024-12-12 10:00", "Parking in no-parking zone", 75, "PENDING"));
            save(new Violation(null, 3, "2024-12-15 11:45", "Late return - 2 hours late", 50, "PENDING"));
            save(new Violation(null, 2, "2024-12-14 09:00", "Car returned with empty gas tank", 35, "PAID"));
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