package ke.co.moh216.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ChildRequest(
    @NotBlank @Size(max = 150) String fullName,
    @NotNull @PastOrPresent LocalDate dateOfBirth,
    @NotBlank @Size(max = 20) String sex,
    @NotNull Long motherId,
    @DecimalMin(value = "0.01") BigDecimal birthWeightKg,
    @Size(max = 150) String birthFacility,
    @Size(max = 2000) String neonatalHistory
) {
}
