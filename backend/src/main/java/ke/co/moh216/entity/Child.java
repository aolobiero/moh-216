package ke.co.moh216.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "children")
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @org.hibernate.annotations.Generated(
        event = org.hibernate.generator.EventType.INSERT
    )
    @Column(name = "qr_token", nullable = false, insertable = false, updatable = false)
    private UUID qrToken;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 20)
    private String sex;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mother_id", nullable = false)
    private Mother mother;

    @Column(name = "birth_weight_kg", precision = 5, scale = 2)
    private BigDecimal birthWeightKg;

    @Column(name = "birth_facility", length = 150)
    private String birthFacility;

    @Column(name = "neonatal_history", length = 2000)
    private String neonatalHistory;

    @Column(name = "registered_at", nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    @PrePersist
    public void onCreate() {
        registeredAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public UUID getQrToken() { return qrToken; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public Mother getMother() { return mother; }
    public void setMother(Mother mother) { this.mother = mother; }
    public BigDecimal getBirthWeightKg() { return birthWeightKg; }
    public void setBirthWeightKg(BigDecimal birthWeightKg) { this.birthWeightKg = birthWeightKg; }
    public String getBirthFacility() { return birthFacility; }
    public void setBirthFacility(String birthFacility) { this.birthFacility = birthFacility; }
    public String getNeonatalHistory() { return neonatalHistory; }
    public void setNeonatalHistory(String neonatalHistory) { this.neonatalHistory = neonatalHistory; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
}
