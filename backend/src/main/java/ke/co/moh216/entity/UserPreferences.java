package ke.co.moh216.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_preferences")
public class UserPreferences {
    @Id @Column(length = 100) private String username;
    @Column(nullable = false, length = 20) private String theme = "light";
    @Column(name = "text_size", nullable = false, length = 20) private String textSize = "normal";
    @Column(nullable = false, length = 20) private String language = "en";
    @Column(name = "anc_reminders", nullable = false) private boolean ancReminders = true;
    @Column(name = "immunization_reminders", nullable = false) private boolean immunizationReminders = true;
    @Column(name = "pnc_reminders", nullable = false) private boolean pncReminders = true;
    @Column(name = "missed_appointment_notifications", nullable = false) private boolean missedAppointmentNotifications = true;
    @Column(name = "updated_at", nullable = false) private LocalDateTime updatedAt;
    @PrePersist @PreUpdate void touch() { updatedAt = LocalDateTime.now(); }
    public String getUsername() { return username; }
    public void setUsername(String value) { username = value; }
    public String getTheme() { return theme; }
    public void setTheme(String value) { theme = value; }
    public String getTextSize() { return textSize; }
    public void setTextSize(String value) { textSize = value; }
    public String getLanguage() { return language; }
    public void setLanguage(String value) { language = value; }
    public boolean isAncReminders() { return ancReminders; }
    public void setAncReminders(boolean value) { ancReminders = value; }
    public boolean isImmunizationReminders() { return immunizationReminders; }
    public void setImmunizationReminders(boolean value) { immunizationReminders = value; }
    public boolean isPncReminders() { return pncReminders; }
    public void setPncReminders(boolean value) { pncReminders = value; }
    public boolean isMissedAppointmentNotifications() { return missedAppointmentNotifications; }
    public void setMissedAppointmentNotifications(boolean value) { missedAppointmentNotifications = value; }
}
