package ke.co.moh216.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff_profiles")
public class StaffProfile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 100)
    private String username;
    @Column(name = "full_name", length = 150) private String fullName;
    @Column(length = 254) private String email;
    @Column(length = 30) private String phone;
    @Column(length = 100) private String designation;
    @Column(nullable = false, length = 40) private String role = "STAFF";
    @Column(name = "photo_url", length = 500) private String photoUrl;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @PrePersist @PreUpdate void touch() { updatedAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String value) { username = value; }
    public String getFullName() { return fullName; }
    public void setFullName(String value) { fullName = value; }
    public String getEmail() { return email; }
    public void setEmail(String value) { email = value; }
    public String getPhone() { return phone; }
    public void setPhone(String value) { phone = value; }
    public String getDesignation() { return designation; }
    public void setDesignation(String value) { designation = value; }
    public String getRole() { return role; }
    public void setRole(String value) { role = value; }
    public String getPhotoUrl() { return photoUrl; }
}
