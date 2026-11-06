package ke.co.moh216.controller;

import jakarta.validation.Valid;
import ke.co.moh216.dto.ModuleRequests.*;
import ke.co.moh216.entity.*;
import ke.co.moh216.service.ClinicalModulesService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ClinicalModulesController {
    private final ClinicalModulesService service;
    public ClinicalModulesController(ClinicalModulesService service) { this.service = service; }
    @PostMapping("/health-records")
    public HealthRecord createRecord(@Valid @RequestBody HealthRecordRequest request, Authentication auth) {
        return service.createRecord(request, auth.getName());
    }
    @GetMapping("/health-records")
    public List<HealthRecord> records(@RequestParam(required = false) Long motherId,
        @RequestParam(required = false) Long childId) { return service.recordsFor(motherId, childId); }
    @PostMapping("/appointments")
    public Appointment createAppointment(@Valid @RequestBody AppointmentRequest request, Authentication auth) {
        return service.createAppointment(request, auth.getName());
    }
    @GetMapping("/appointments")
    public List<Appointment> appointments(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return service.appointments(from, to);
    }
    @PostMapping("/immunizations")
    public ImmunizationRecord createImmunization(@Valid @RequestBody ImmunizationRequest request, Authentication auth) {
        return service.createImmunization(request, auth.getName());
    }
    @GetMapping("/immunizations")
    public List<ImmunizationRecord> immunizations(@RequestParam Long childId) {
        return service.immunizationsFor(childId);
    }
    @GetMapping("/reports/summary")
    public ReportSummary reportSummary() {
        return new ReportSummary(service.appointmentCount(), service.immunizationCount());
    }
    public record ReportSummary(long scheduledAppointments, long immunizationsRecorded) {}
}
