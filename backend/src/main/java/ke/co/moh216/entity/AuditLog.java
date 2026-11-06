package ke.co.moh216.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, length = 100) private String username;
    @Column(nullable = false, length = 100) private String action;
    @Column(name = "record_reference", length = 200) private String recordReference;
    @Column(name = "occurred_at", nullable = false) private LocalDateTime occurredAt;
    @PrePersist void onCreate() { occurredAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String value) { username = value; }
    public String getAction() { return action; }
    public void setAction(String value) { action = value; }
    public String getRecordReference() { return recordReference; }
    public void setRecordReference(String value) { recordReference = value; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
}
