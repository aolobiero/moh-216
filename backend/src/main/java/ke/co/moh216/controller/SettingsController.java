package ke.co.moh216.controller;

import jakarta.validation.Valid;
import ke.co.moh216.dto.SettingsRequests;
import ke.co.moh216.service.SettingsService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SettingsController {
    private final SettingsService service;
    private final JdbcTemplate jdbcTemplate;
    public SettingsController(SettingsService service, JdbcTemplate jdbcTemplate) {
        this.service = service;
        this.jdbcTemplate = jdbcTemplate;
    }
    @GetMapping("/profile") public Object profile(Authentication auth) { return service.getProfile(auth.getName()); }
    @PutMapping("/profile") public Object profile(@Valid @RequestBody SettingsRequests.Profile request, Authentication auth) { return service.profile(auth.getName(), request); }
    @GetMapping("/preferences") public Object preferences(Authentication auth) { return service.getPreferences(auth.getName()); }
    @PutMapping("/preferences") public Object preferences(@RequestBody SettingsRequests.Preferences request, Authentication auth) { return service.preferences(auth.getName(), request); }
    @GetMapping("/facility") @PreAuthorize("hasRole('ADMIN')") public Object facility() { return service.getFacility(); }
    @PutMapping("/facility") @PreAuthorize("hasRole('ADMIN')") public Object facility(@Valid @RequestBody SettingsRequests.Facility request, Authentication auth) { return service.facility(request, auth.getName()); }
    @GetMapping("/audit") @PreAuthorize("hasRole('ADMIN')") public Object audit() { return service.audits(); }
    @GetMapping("/health") @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> health() {
        jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        return Map.of("database", "connected", "application", "healthy");
    }
}
