package ke.co.moh216.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "mothers")
public class Mother {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @org.hibernate.annotations.Generated(
        event = org.hibernate.generator.EventType.INSERT
    )
    @Column(name = "qr_token", nullable = false, insertable = false, updatable = false)
    private UUID qrToken;

    public UUID getQrToken() {
        return qrToken;
    }


    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(length = 100)
    private String county;

    @Column(name = "health_facility", length = 150)
    private String healthFacility;

    @Column(name = "sub_county", length = 100)
    private String subCounty;

    @Column(name = "last_menstrual_period")
    private LocalDate lastMenstrualPeriod;

    @Column(name = "expected_delivery_date")
    private LocalDate expectedDeliveryDate;

    private Integer gravida;

    private Integer parity;

    @Column(name = "registered_at", nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    @PrePersist
    public void onCreate() {
        registeredAt = LocalDateTime.now();
    }

    public Mother() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCounty() { return county; }
    public void setCounty(String county) { this.county = county; }

    public String getHealthFacility() { return healthFacility; }
    public void setHealthFacility(String healthFacility) { this.healthFacility = healthFacility; }

    public String getSubCounty() { return subCounty; }
    public void setSubCounty(String subCounty) { this.subCounty = subCounty; }

    public LocalDate getLastMenstrualPeriod() { return lastMenstrualPeriod; }
    public void setLastMenstrualPeriod(LocalDate lastMenstrualPeriod) {
        this.lastMenstrualPeriod = lastMenstrualPeriod;
    }

    public LocalDate getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public void setExpectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public Integer getGravida() { return gravida; }
    public void setGravida(Integer gravida) { this.gravida = gravida; }

    public Integer getParity() { return parity; }
    public void setParity(Integer parity) { this.parity = parity; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
}
