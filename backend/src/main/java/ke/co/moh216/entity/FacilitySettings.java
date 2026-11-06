package ke.co.moh216.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "facility_settings")
public class FacilitySettings {
    @Id private Short id = 1;
    @Column(name = "facility_name", length = 200) private String facilityName;
    @Column(name = "mfl_code", length = 30) private String mflCode;
    @Column(length = 100) private String county;
    @Column(name = "sub_county", length = 100) private String subCounty;
    @Column(length = 200) private String location;
    @Column(name = "contact_phone", length = 30) private String contactPhone;
    @Column(name = "contact_email", length = 254) private String contactEmail;
    @Column(name = "administrator_name", length = 150) private String administratorName;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @PrePersist @PreUpdate void touch() { updatedAt = LocalDateTime.now(); }
    public Short getId() { return id; }
    public void setId(Short value) { id = value; }
    public String getFacilityName() { return facilityName; }
    public void setFacilityName(String value) { facilityName = value; }
    public String getMflCode() { return mflCode; }
    public void setMflCode(String value) { mflCode = value; }
    public String getCounty() { return county; }
    public void setCounty(String value) { county = value; }
    public String getSubCounty() { return subCounty; }
    public void setSubCounty(String value) { subCounty = value; }
    public String getLocation() { return location; }
    public void setLocation(String value) { location = value; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String value) { contactPhone = value; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String value) { contactEmail = value; }
    public String getAdministratorName() { return administratorName; }
    public void setAdministratorName(String value) { administratorName = value; }
}
