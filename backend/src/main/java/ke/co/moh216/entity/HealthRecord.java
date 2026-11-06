package ke.co.moh216.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_records")
public class HealthRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "mother_id")
    private Mother mother;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "child_id")
    private Child child;
    @Column(name = "record_type", nullable = false, length = 60) private String recordType;
    @Column(nullable = false, length = 200) private String title;
    @Column(name = "record_date", nullable = false) private LocalDate recordDate;
    @Column(name = "clinical_notes", length = 4000) private String clinicalNotes;
    @Column(name = "author_username", nullable = false, length = 100) private String authorUsername;
    @Column(nullable = false) private boolean sensitive;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt;
    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public Mother getMother() { return mother; } public void setMother(Mother value) { mother = value; }
    public Child getChild() { return child; } public void setChild(Child value) { child = value; }
    public String getRecordType() { return recordType; } public void setRecordType(String value) { recordType = value; }
    public String getTitle() { return title; } public void setTitle(String value) { title = value; }
    public LocalDate getRecordDate() { return recordDate; } public void setRecordDate(LocalDate value) { recordDate = value; }
    public String getClinicalNotes() { return clinicalNotes; } public void setClinicalNotes(String value) { clinicalNotes = value; }
    public String getAuthorUsername() { return authorUsername; } public void setAuthorUsername(String value) { authorUsername = value; }
    public boolean isSensitive() { return sensitive; } public void setSensitive(boolean value) { sensitive = value; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
