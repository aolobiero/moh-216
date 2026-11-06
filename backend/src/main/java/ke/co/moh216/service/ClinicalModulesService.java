package ke.co.moh216.service;

import ke.co.moh216.dto.ModuleRequests.*;
import ke.co.moh216.entity.*;
import ke.co.moh216.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ClinicalModulesService {
    private final MotherRepository mothers;
    private final ChildRepository children;
    private final HealthRecordRepository records;
    private final AppointmentRepository appointments;
    private final ImmunizationRecordRepository immunizations;

    public ClinicalModulesService(MotherRepository mothers, ChildRepository children,
        HealthRecordRepository records, AppointmentRepository appointments,
        ImmunizationRecordRepository immunizations) {
        this.mothers = mothers; this.children = children; this.records = records;
        this.appointments = appointments; this.immunizations = immunizations;
    }

    @Transactional
    public HealthRecord createRecord(HealthRecordRequest request, String username) {
        validateSubject(request.motherId(), request.childId());
        HealthRecord record = new HealthRecord();
        record.setMother(request.motherId() == null ? null : mother(request.motherId()));
        record.setChild(request.childId() == null ? null : child(request.childId()));
        record.setRecordType(request.recordType()); record.setTitle(request.title());
        record.setRecordDate(request.recordDate()); record.setClinicalNotes(request.clinicalNotes());
        record.setSensitive(request.sensitive()); record.setAuthorUsername(username);
        return records.save(record);
    }
    @Transactional(readOnly = true)
    public List<HealthRecord> recordsFor(Long motherId, Long childId) {
        validateSubject(motherId, childId);
        return motherId != null ? records.findByMotherIdOrderByRecordDateDesc(motherId)
            : records.findByChildIdOrderByRecordDateDesc(childId);
    }
    @Transactional
    public Appointment createAppointment(AppointmentRequest request, String username) {
        validateSubject(request.motherId(), request.childId());
        Appointment appointment = new Appointment();
        appointment.setMother(request.motherId() == null ? null : mother(request.motherId()));
        appointment.setChild(request.childId() == null ? null : child(request.childId()));
        appointment.setAppointmentType(request.appointmentType()); appointment.setFacilityName(request.facilityName());
        appointment.setProviderUsername(request.providerUsername()); appointment.setStartsAt(request.startsAt());
        appointment.setEndsAt(request.endsAt()); appointment.setStatus(request.status() == null ? "SCHEDULED" : request.status());
        appointment.setNotes(request.notes()); appointment.setCreatedBy(username);
        return appointments.save(appointment);
    }
    @Transactional(readOnly = true)
    public List<Appointment> appointments(LocalDateTime from, LocalDateTime to) {
        return appointments.findByStartsAtBetweenOrderByStartsAt(from, to);
    }
    @Transactional
    public ImmunizationRecord createImmunization(ImmunizationRequest request, String username) {
        ImmunizationRecord record = new ImmunizationRecord();
        record.setChild(child(request.childId())); record.setVaccineName(request.vaccineName());
        record.setDoseLabel(request.doseLabel()); record.setAdministeredDate(request.administeredDate());
        record.setProviderUsername(username); record.setNotes(request.notes());
        return immunizations.save(record);
    }
    @Transactional(readOnly = true)
    public List<ImmunizationRecord> immunizationsFor(Long childId) {
        child(childId);
        return immunizations.findByChildIdOrderByAdministeredDateDesc(childId);
    }
    public long appointmentCount() { return appointments.countByStatus("SCHEDULED"); }
    public long immunizationCount() { return immunizations.count(); }
    private Mother mother(Long id) { return mothers.findById(id).orElseThrow(() -> new NoSuchElementException("Mother not found")); }
    private Child child(Long id) { return children.findById(id).orElseThrow(() -> new NoSuchElementException("Child not found")); }
    private void validateSubject(Long motherId, Long childId) {
        if ((motherId == null) == (childId == null)) throw new IllegalArgumentException("Exactly one motherId or childId is required");
    }
}
