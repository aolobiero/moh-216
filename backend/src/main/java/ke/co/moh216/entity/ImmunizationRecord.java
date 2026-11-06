package ke.co.moh216.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "immunization_records")
public class ImmunizationRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "child_id", nullable = false) private Child child;
    @Column(name = "vaccine_name", nullable = false, length = 150) private String vaccineName;
    @Column(name = "dose_label", length = 80) private String doseLabel;
    @Column(name = "administered_date", nullable = false) private LocalDate administeredDate;
    @Column(name = "provider_username", length = 100) private String providerUsername;
    @Column(length = 2000) private String notes;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public Child getChild() { return child; } public void setChild(Child v) { child = v; }
    public String getVaccineName() { return vaccineName; } public void setVaccineName(String v) { vaccineName = v; }
    public String getDoseLabel() { return doseLabel; } public void setDoseLabel(String v) { doseLabel = v; }
    public LocalDate getAdministeredDate() { return administeredDate; } public void setAdministeredDate(LocalDate v) { administeredDate = v; }
    public String getProviderUsername() { return providerUsername; } public void setProviderUsername(String v) { providerUsername = v; }
    public String getNotes() { return notes; } public void setNotes(String v) { notes = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
