package ke.co.moh216.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record AncVisitRequest(
    @NotNull LocalDate visitDate,
    @Positive BigDecimal weightKg,
    @Positive Integer systolicBp,
    @Positive Integer diastolicBp,
    String clinicalNotes,
    LocalDate followUpDate
) {}
