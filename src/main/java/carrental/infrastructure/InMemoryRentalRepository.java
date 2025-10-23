package carrental.infrastructure;

import carrental.domain.model.Rental;
import carrental.domain.repository.RentalRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryRentalRepository implements RentalRepository {
    private final Map<Integer, Rental> storage = new ConcurrentHashMap<>();
    private final AtomicInteger idCounter = new AtomicInteger(1);

    @Override
    public Rental save(Rental rental) {
        if (rental.getRentalId() == null) {
            Rental newRental = new Rental(
                    idCounter.getAndIncrement(),
                    rental.getCustomerId(),
                    rental.getCarId(),
                    rental.getDateStart(),
                    rental.getDateEnd(),
                    rental.getCostFact(),
                    rental.getDepositeStatus(),
                    rental.getStatus()
            );
            storage.put(newRental.getRentalId(), newRental);
            return newRental;
        } else {
            storage.put(rental.getRentalId(), rental);
            return rental;
        }
    }

    @Override
    public Optional<Rental> findById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Rental> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void delete(Integer id) {
        storage.remove(id);
    }

    @Override
    public List<Rental> findByCustomerId(Integer customerId) {
        return storage.values().stream()
                .filter(rental -> rental.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findByCarId(Integer carId) {
        return storage.values().stream()
                .filter(rental -> rental.getCarId().equals(carId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findActiveRentals() {
        return storage.values().stream()
                .filter(rental -> "ACTIVE".equalsIgnoreCase(rental.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findCompletedRentals() {
        return storage.values().stream()
                .filter(rental -> "COMPLETED".equalsIgnoreCase(rental.getStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Rental> findByStatus(String status) {
        return storage.values().stream()
                .filter(rental -> rental.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isCarCurrentlyRented(Integer carId) {
        return storage.values().stream()
                .anyMatch(rental -> rental.getCarId().equals(carId) &&
                        "ACTIVE".equalsIgnoreCase(rental.getStatus()));
    }

    @Override
    public boolean hasActiveRentals(Integer customerId) {
        return storage.values().stream()
                .anyMatch(rental -> rental.getCustomerId().equals(customerId) &&
                        "ACTIVE".equalsIgnoreCase(rental.getStatus()));
    }

    @Override
    public boolean updateStatus(Integer rentalId, String status) {
        Optional<Rental> rentalOpt = findById(rentalId);
        if (rentalOpt.isPresent()) {
            Rental rental = rentalOpt.get();
            Rental updatedRental = new Rental(
                    rental.getRentalId(),
                    rental.getCustomerId(),
                    rental.getCarId(),
                    rental.getDateStart(),
                    rental.getDateEnd(),
                    rental.getCostFact(),
                    rental.getDepositeStatus(),
                    status
            );
            storage.put(rentalId, updatedRental);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateDepositStatus(Integer rentalId, String depositStatus) {
        Optional<Rental> rentalOpt = findById(rentalId);
        if (rentalOpt.isPresent()) {
            Rental rental = rentalOpt.get();
            Rental updatedRental = new Rental(
                    rental.getRentalId(),
                    rental.getCustomerId(),
                    rental.getCarId(),
                    rental.getDateStart(),
                    rental.getDateEnd(),
                    rental.getCostFact(),
                    depositStatus,
                    rental.getStatus()
            );
            storage.put(rentalId, updatedRental);
            return true;
        }
        return false;
    }

    @Override
    public List<Rental> findByDateRange(String startDate, String endDate) {
        try {
            LocalDate rangeStart = LocalDate.parse(startDate);
            LocalDate rangeEnd = LocalDate.parse(endDate);

            return storage.values().stream()
                    .filter(rental -> {
                        try {
                            LocalDate rentalStart = LocalDate.parse(rental.getDateStart());
                            return !rentalStart.isBefore(rangeStart) && !rentalStart.isAfter(rangeEnd);
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
    public List<Rental> findByCustomerIdAndStatus(Integer customerId, String status) {
        return storage.values().stream()
                .filter(rental -> rental.getCustomerId().equals(customerId) &&
                        rental.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public void initializeWithSampleData() {
        if (storage.isEmpty()) {
            save(new Rental(null, 1, 1, "2024-12-10", "2024-12-12", 150, "REFUNDED", "COMPLETED"));
            save(new Rental(null, 2, 2, "2024-12-13", "2024-12-15", 120, "PAID", "COMPLETED"));
            save(new Rental(null, 3, 3, "2024-12-14", "2024-12-16", 176, "PAID", "ACTIVE"));
            save(new Rental(null, 1, 4, "2024-12-11", "2024-12-13", 300, "REFUNDED", "COMPLETED"));
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