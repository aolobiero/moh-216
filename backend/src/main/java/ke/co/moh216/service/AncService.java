package ke.co.moh216.service;

import ke.co.moh216.dto.AncVisitRequest;
import ke.co.moh216.dto.PregnancyRequest;
import ke.co.moh216.entity.AncVisit;
import ke.co.moh216.entity.Mother;
import ke.co.moh216.entity.Pregnancy;
import ke.co.moh216.repository.AncVisitRepository;
import ke.co.moh216.repository.MotherRepository;
import ke.co.moh216.repository.PregnancyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AncService {
    private final MotherRepository motherRepository;
    private final PregnancyRepository pregnancyRepository;
    private final AncVisitRepository ancVisitRepository;

    public AncService(MotherRepository motherRepository, PregnancyRepository pregnancyRepository,
                      AncVisitRepository ancVisitRepository) {
        this.motherRepository = motherRepository;
        this.pregnancyRepository = pregnancyRepository;
        this.ancVisitRepository = ancVisitRepository;
    }

    @Transactional
    public Pregnancy createPregnancy(PregnancyRequest request) {
        Mother mother = motherRepository.findById(request.motherId())
            .orElseThrow(() -> new NoSuchElementException("Mother not found"));
        Pregnancy pregnancy = new Pregnancy();
        pregnancy.setMother(mother);
        pregnancy.setLastMenstrualPeriod(request.lastMenstrualPeriod());
        pregnancy.setExpectedDeliveryDate(request.expectedDeliveryDate());
        pregnancy.setGravida(request.gravida());
        pregnancy.setParity(request.parity());
        return pregnancyRepository.save(pregnancy);
    }

    @Transactional(readOnly = true)
    public List<Pregnancy> getPregnancies(Long motherId) {
        return pregnancyRepository.findByMotherIdOrderByRecordedAtDesc(motherId);
    }

    @Transactional
    public AncVisit addVisit(Long pregnancyId, AncVisitRequest request) {
        Pregnancy pregnancy = pregnancyRepository.findById(pregnancyId)
            .orElseThrow(() -> new NoSuchElementException("Pregnancy not found"));
        AncVisit visit = new AncVisit();
        visit.setPregnancy(pregnancy);
        visit.setVisitDate(request.visitDate());
        visit.setWeightKg(request.weightKg());
        visit.setSystolicBp(request.systolicBp());
        visit.setDiastolicBp(request.diastolicBp());
        visit.setClinicalNotes(request.clinicalNotes());
        visit.setFollowUpDate(request.followUpDate());
        return ancVisitRepository.save(visit);
    }

    @Transactional(readOnly = true)
    public List<AncVisit> getVisits(Long pregnancyId) {
        return ancVisitRepository.findByPregnancyIdOrderByVisitDateDesc(pregnancyId);
    }
}
