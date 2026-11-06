package ke.co.moh216.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public final class SettingsRequests {
    private SettingsRequests() {}
    public record Profile(@Size(max = 150) String fullName, @Email @Size(max = 254) String email,
                          @Size(max = 30) String phone, @Size(max = 100) String designation) {}
    public record Facility(@Size(max = 200) String facilityName, @Size(max = 30) String mflCode,
                           @Size(max = 100) String county, @Size(max = 100) String subCounty,
                           @Size(max = 200) String location, @Size(max = 30) String contactPhone,
                           @Email @Size(max = 254) String contactEmail,
                           @Size(max = 150) String administratorName) {}
    public record Preferences(String theme, String textSize, String language,
                              Boolean ancReminders, Boolean immunizationReminders,
                              Boolean pncReminders, Boolean missedAppointmentNotifications) {}
}
