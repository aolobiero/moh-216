package ke.co.moh216.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ModuleRequests {
    private ModuleRequests() {}
    public record HealthRecordRequest(
        Long motherId, Long childId,
        @NotBlank @Size(max = 60) String recordType,
        @NotBlank @Size(max = 200) String title,
        @NotNull LocalDate recordDate,
        @Size(max = 4000) String clinicalNotes,
        boolean sensitive) {}
    public record AppointmentRequest(
        Long motherId, Long childId,
        @NotBlank @Size(max = 40) String appointmentType,
        @Size(max = 200) String facilityName,
        @Size(max = 100) String providerUsername,
        @NotNull LocalDateTime startsAt,
        LocalDateTime endsAt,
        @Size(max = 30) String status,
        @Size(max = 2000) String notes) {}
    public record ImmunizationRequest(
        @NotNull Long childId,
        @NotBlank @Size(max = 150) String vaccineName,
        @Size(max = 80) String doseLabel,
        @NotNull LocalDate administeredDate,
        @Size(max = 2000) String notes) {}
}
