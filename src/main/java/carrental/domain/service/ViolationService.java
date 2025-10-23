package carrental.domain.service;

import carrental.domain.model.Violation;
import carrental.domain.model.Rental;
import carrental.domain.repository.ViolationRepository;
import carrental.domain.repository.RentalRepository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ViolationService {
    private final ViolationRepository violationRepository;
    private final RentalRepository rentalRepository;

    public ViolationService(ViolationRepository violationRepository,
                            RentalRepository rentalRepository) {
        this.violationRepository = violationRepository;
        this.rentalRepository = rentalRepository;
    }

    public Violation recordViolation(Integer rentalId, String dateTime,
                                     String description, Integer fineAmount, String status) {
        if (rentalId == null || rentalId <= 0) {
            throw new IllegalArgumentException("Valid rental ID is required");
        }

        if (!isValidDateTime(dateTime)) {
            throw new IllegalArgumentException("Invalid date-time format. Use YYYY-MM-DD HH:MM");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Violation description is required");
        }

        if (fineAmount == null || fineAmount < 0) {
            throw new IllegalArgumentException("Fine amount must be non-negative");
        }

        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status is required");
        }

        Optional<Rental> rental = rentalRepository.findById(rentalId);
        if (!rental.isPresent()) {
            throw new IllegalArgumentException("Rental not found with ID: " + rentalId);
        }

        List<String> validStatuses = Arrays.asList("PENDING", "PAID", "RESOLVED");
        if (!validStatuses.contains(status.toUpperCase())) {
            throw new IllegalArgumentException("Invalid status. Must be: PENDING, PAID, or RESOLVED");
        }

        Violation violation = new Violation(null, rentalId, dateTime, description, fineAmount, status);
        return violationRepository.save(violation);
    }

    public Optional<Violation> getViolation(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid violation ID");
        }
        return violationRepository.findById(id);
    }

    public List<Violation> getAllViolations() {
        return violationRepository.findAll();
    }

    public List<Violation> getViolationsByRentalId(Integer rentalId) {
        if (rentalId == null || rentalId <= 0) {
            throw new IllegalArgumentException("Invalid rental ID");
        }
        return violationRepository.findByRentalId(rentalId);
    }

    public List<Violation> getPendingViolations() {
        return violationRepository.findPendingViolations();
    }

    public List<Violation> getPaidViolations() {
        return violationRepository.findPaidViolations();
    }

    public boolean resolveViolation(Integer violationId) {
        if (violationId == null || violationId <= 0) {
            throw new IllegalArgumentException("Invalid violation ID");
        }

        Optional<Violation> violation = violationRepository.findById(violationId);
        if (violation.isPresent()) {
            return violationRepository.updateStatus(violationId, "PAID");
        }
        return false;
    }

    public boolean updateFineAmount(Integer violationId, Integer newFineAmount) {
        if (violationId == null || violationId <= 0) {
            throw new IllegalArgumentException("Invalid violation ID");
        }

        if (newFineAmount == null || newFineAmount < 0) {
            throw new IllegalArgumentException("Fine amount must be non-negative");
        }

        Optional<Violation> violation = violationRepository.findById(violationId);
        if (violation.isPresent()) {
            return violationRepository.updateFineAmount(violationId, newFineAmount);
        }
        return false;
    }

    public boolean deleteViolation(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid violation ID");
        }

        Optional<Violation> violation = violationRepository.findById(id);
        if (violation.isPresent()) {
            violationRepository.delete(id);
            return true;
        }
        return false;
    }

    public Integer getTotalFinesForRental(Integer rentalId) {
        if (rentalId == null || rentalId <= 0) {
            throw new IllegalArgumentException("Invalid rental ID");
        }

        return violationRepository.getTotalFinesByRentalId(rentalId);
    }

    public Integer getTotalPendingFinesForRental(Integer rentalId) {
        if (rentalId == null || rentalId <= 0) {
            throw new IllegalArgumentException("Invalid rental ID");
        }

        return violationRepository.getTotalPendingFinesByRentalId(rentalId);
    }

    public boolean hasPendingViolations(Integer rentalId) {
        if (rentalId == null || rentalId <= 0) {
            throw new IllegalArgumentException("Invalid rental ID");
        }

        List<Violation> pendingViolations = violationRepository.findByRentalIdAndStatus(rentalId, "PENDING");
        return !pendingViolations.isEmpty();
    }

    public List<Violation> getViolationsWithFineGreaterThan(Integer minAmount) {
        if (minAmount == null || minAmount < 0) {
            throw new IllegalArgumentException("Minimum amount must be non-negative");
        }
        return violationRepository.findByFineAmountGreaterThan(minAmount);
    }

    public List<Violation> getViolationsInDateRange(String startDate, String endDate) {
        if (startDate == null || startDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Valid start date is required");
        }

        if (endDate == null || endDate.trim().isEmpty()) {
            throw new IllegalArgumentException("Valid end date is required");
        }

        if (!isValidDate(startDate) || !isValidDate(endDate)) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
        }

        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            if (end.isBefore(start)) {
                throw new IllegalArgumentException("End date must be after start date");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
        }

        return violationRepository.findByDateRange(startDate, endDate);
    }

    public boolean resolveAllViolationsForRental(Integer rentalId) {
        if (rentalId == null || rentalId <= 0) {
            throw new IllegalArgumentException("Invalid rental ID");
        }

        List<Violation> pendingViolations = violationRepository.findByRentalIdAndStatus(rentalId, "PENDING");
        boolean allResolved = true;

        for (Violation violation : pendingViolations) {
            boolean resolved = violationRepository.updateStatus(violation.getViolationId(), "PAID");
            if (!resolved) {
                allResolved = false;
            }
        }

        return allResolved;
    }

    private boolean isValidDateTime(String dateTime) {
        return dateTime != null && dateTime.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}");
    }

    private boolean isValidDate(String date) {
        return date != null && date.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}