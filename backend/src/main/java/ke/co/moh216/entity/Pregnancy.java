package ke.co.moh216.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pregnancies")
public class Pregnancy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mother_id", nullable = false)
    private Mother mother;

    private LocalDate lastMenstrualPeriod;
    private LocalDate expectedDeliveryDate;
    private Integer gravida;
    private Integer parity;

    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    @PrePersist
    void onCreate() {
        recordedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Mother getMother() { return mother; }
    public void setMother(Mother mother) { this.mother = mother; }
    public LocalDate getLastMenstrualPeriod() { return lastMenstrualPeriod; }
    public void setLastMenstrualPeriod(LocalDate value) { lastMenstrualPeriod = value; }
    public LocalDate getExpectedDeliveryDate() { return expectedDeliveryDate; }
    public void setExpectedDeliveryDate(LocalDate value) { expectedDeliveryDate = value; }
    public Integer getGravida() { return gravida; }
    public void setGravida(Integer value) { gravida = value; }
    public Integer getParity() { return parity; }
    public void setParity(Integer value) { parity = value; }
}
