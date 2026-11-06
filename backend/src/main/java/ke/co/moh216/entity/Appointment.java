package ke.co.moh216.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "mother_id") private Mother mother;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "child_id") private Child child;
    @Column(name = "appointment_type", nullable = false, length = 40) private String appointmentType;
    @Column(name = "facility_name", length = 200) private String facilityName;
    @Column(name = "provider_username", length = 100) private String providerUsername;
    @Column(name = "starts_at", nullable = false) private LocalDateTime startsAt;
    @Column(name = "ends_at") private LocalDateTime endsAt;
    @Column(nullable = false, length = 30) private String status = "SCHEDULED";
    @Column(length = 2000) private String notes;
    @Column(name = "created_by", nullable = false, length = 100) private String createdBy;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public Mother getMother() { return mother; } public void setMother(Mother v) { mother = v; }
    public Child getChild() { return child; } public void setChild(Child v) { child = v; }
    public String getAppointmentType() { return appointmentType; } public void setAppointmentType(String v) { appointmentType = v; }
    public String getFacilityName() { return facilityName; } public void setFacilityName(String v) { facilityName = v; }
    public String getProviderUsername() { return providerUsername; } public void setProviderUsername(String v) { providerUsername = v; }
    public LocalDateTime getStartsAt() { return startsAt; } public void setStartsAt(LocalDateTime v) { startsAt = v; }
    public LocalDateTime getEndsAt() { return endsAt; } public void setEndsAt(LocalDateTime v) { endsAt = v; }
    public String getStatus() { return status; } public void setStatus(String v) { status = v; }
    public String getNotes() { return notes; } public void setNotes(String v) { notes = v; }
    public String getCreatedBy() { return createdBy; } public void setCreatedBy(String v) { createdBy = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
