package ke.co.moh216.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PregnancyRequest(
    @NotNull Long motherId,
    LocalDate lastMenstrualPeriod,
    LocalDate expectedDeliveryDate,
    @Min(0) Integer gravida,
    @Min(0) Integer parity
) {}
