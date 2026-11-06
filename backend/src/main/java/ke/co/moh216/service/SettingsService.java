package ke.co.moh216.service;

import ke.co.moh216.dto.SettingsRequests;
import ke.co.moh216.entity.*;
import ke.co.moh216.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class SettingsService {
    private final StaffProfileRepository profiles;
    private final FacilitySettingsRepository facilities;
    private final UserPreferencesRepository preferences;
    private final AuditLogRepository audits;
    public SettingsService(StaffProfileRepository profiles, FacilitySettingsRepository facilities,
                           UserPreferencesRepository preferences, AuditLogRepository audits) {
        this.profiles = profiles; this.facilities = facilities; this.preferences = preferences; this.audits = audits;
    }
    @Transactional
    public StaffProfile profile(String username, SettingsRequests.Profile request) {
        StaffProfile profile = profiles.findByUsername(username).orElseGet(StaffProfile::new);
        profile.setUsername(username); profile.setFullName(request.fullName()); profile.setEmail(request.email());
        profile.setPhone(request.phone()); profile.setDesignation(request.designation());
        StaffProfile saved = profiles.save(profile); audit(username, "PROFILE_UPDATED", "staff:" + username); return saved;
    }
    @Transactional(readOnly = true)
    public StaffProfile getProfile(String username) {
        return profiles.findByUsername(username).orElseGet(() -> { StaffProfile p = new StaffProfile(); p.setUsername(username); return p; });
    }
    @Transactional
    public FacilitySettings facility(SettingsRequests.Facility request, String username) {
        FacilitySettings facility = facilities.findById((short) 1).orElseGet(FacilitySettings::new);
        facility.setId((short) 1); facility.setFacilityName(request.facilityName()); facility.setMflCode(request.mflCode());
        facility.setCounty(request.county()); facility.setSubCounty(request.subCounty()); facility.setLocation(request.location());
        facility.setContactPhone(request.contactPhone()); facility.setContactEmail(request.contactEmail());
        facility.setAdministratorName(request.administratorName());
        FacilitySettings saved = facilities.save(facility); audit(username, "FACILITY_UPDATED", "facility:1"); return saved;
    }
    @Transactional(readOnly = true)
    public FacilitySettings getFacility() { return facilities.findById((short) 1).orElseGet(FacilitySettings::new); }
    @Transactional
    public UserPreferences preferences(String username, SettingsRequests.Preferences request) {
        UserPreferences p = preferences.findById(username).orElseGet(UserPreferences::new);
        p.setUsername(username); if (request.theme() != null) p.setTheme(request.theme());
        if (request.textSize() != null) p.setTextSize(request.textSize()); if (request.language() != null) p.setLanguage(request.language());
        if (request.ancReminders() != null) p.setAncReminders(request.ancReminders());
        if (request.immunizationReminders() != null) p.setImmunizationReminders(request.immunizationReminders());
        if (request.pncReminders() != null) p.setPncReminders(request.pncReminders());
        if (request.missedAppointmentNotifications() != null) p.setMissedAppointmentNotifications(request.missedAppointmentNotifications());
        UserPreferences saved = preferences.save(p); audit(username, "PREFERENCES_UPDATED", "user:" + username); return saved;
    }
    @Transactional(readOnly = true)
    public UserPreferences getPreferences(String username) {
        return preferences.findById(username).orElseGet(() -> { UserPreferences p = new UserPreferences(); p.setUsername(username); return p; });
    }
    @Transactional(readOnly = true) public List<AuditLog> audits() { return audits.findTop100ByOrderByOccurredAtDesc(); }
    private void audit(String username, String action, String reference) {
        AuditLog log = new AuditLog(); log.setUsername(username); log.setAction(action); log.setRecordReference(reference); audits.save(log);
    }
}
