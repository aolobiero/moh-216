package ke.co.moh216.controller;

import ke.co.moh216.entity.Mother;
import ke.co.moh216.repository.MotherRepository;
import ke.co.moh216.repository.ChildRepository;
import ke.co.moh216.repository.AppointmentRepository;
import ke.co.moh216.repository.ImmunizationRecordRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final MotherRepository motherRepository;
    private final ChildRepository childRepository;
    private final AppointmentRepository appointmentRepository;
    private final ImmunizationRecordRepository immunizationRecordRepository;

    public DashboardController(MotherRepository motherRepository, ChildRepository childRepository,
            AppointmentRepository appointmentRepository, ImmunizationRecordRepository immunizationRecordRepository) {
        this.motherRepository = motherRepository;
        this.childRepository = childRepository;
        this.appointmentRepository = appointmentRepository;
        this.immunizationRecordRepository = immunizationRecordRepository;
    }

    @GetMapping("/summary")
    public DashboardSummary getSummary() {
        return new DashboardSummary(
            motherRepository.count(),
            childRepository.count(),
            appointmentRepository.countByStatus("SCHEDULED"),
            immunizationRecordRepository.count(),
            motherRepository.findTop5ByOrderByRegisteredAtDesc()
        );
    }

    public record DashboardSummary(
        long registeredMothers,
        long registeredChildren,
        long appointments,
        long immunizations,
        List<Mother> recentRegistrations
    ) {
    }
}
