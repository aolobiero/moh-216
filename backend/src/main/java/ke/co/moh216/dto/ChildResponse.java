package ke.co.moh216.dto;

import ke.co.moh216.entity.Child;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ChildResponse(
    Long id,
    UUID qrToken,
    String fullName,
    LocalDate dateOfBirth,
    String sex,
    Long motherId,
    String motherName,
    BigDecimal birthWeightKg,
    String birthFacility,
    String neonatalHistory,
    LocalDateTime registeredAt
) {
    public static ChildResponse from(Child child) {
        return new ChildResponse(
            child.getId(),
            child.getQrToken(),
            child.getFullName(),
            child.getDateOfBirth(),
            child.getSex(),
            child.getMother().getId(),
            child.getMother().getFullName(),
            child.getBirthWeightKg(),
            child.getBirthFacility(),
            child.getNeonatalHistory(),
            child.getRegisteredAt()
        );
    }
}
