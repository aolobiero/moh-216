package ke.co.moh216.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "anc_visits")
public class AncVisit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pregnancy_id", nullable = false)
    private Pregnancy pregnancy;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;
    private BigDecimal weightKg;
    private Integer systolicBp;
    private Integer diastolicBp;

    @Column(length = 4000)
    private String clinicalNotes;
    private LocalDate followUpDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime recordedAt;

    @PrePersist
    void onCreate() {
        recordedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Pregnancy getPregnancy() { return pregnancy; }
    public void setPregnancy(Pregnancy value) { pregnancy = value; }
    public LocalDate getVisitDate() { return visitDate; }
    public void setVisitDate(LocalDate value) { visitDate = value; }
    public BigDecimal getWeightKg() { return weightKg; }
    public void setWeightKg(BigDecimal value) { weightKg = value; }
    public Integer getSystolicBp() { return systolicBp; }
    public void setSystolicBp(Integer value) { systolicBp = value; }
    public Integer getDiastolicBp() { return diastolicBp; }
    public void setDiastolicBp(Integer value) { diastolicBp = value; }
    public String getClinicalNotes() { return clinicalNotes; }
    public void setClinicalNotes(String value) { clinicalNotes = value; }
    public LocalDate getFollowUpDate() { return followUpDate; }
    public void setFollowUpDate(LocalDate value) { followUpDate = value; }
}
