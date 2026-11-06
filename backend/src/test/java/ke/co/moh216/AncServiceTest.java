package ke.co.moh216;

import ke.co.moh216.dto.AncVisitRequest;
import ke.co.moh216.dto.PregnancyRequest;
import ke.co.moh216.entity.AncVisit;
import ke.co.moh216.entity.Mother;
import ke.co.moh216.entity.Pregnancy;
import ke.co.moh216.repository.AncVisitRepository;
import ke.co.moh216.repository.MotherRepository;
import ke.co.moh216.repository.PregnancyRepository;
import ke.co.moh216.service.AncService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AncServiceTest {
    @Mock MotherRepository motherRepository;
    @Mock PregnancyRepository pregnancyRepository;
    @Mock AncVisitRepository ancVisitRepository;
    @InjectMocks AncService ancService;

    @Test
    void createsPregnancyForExistingMother() {
        Mother mother = new Mother();
        mother.setId(3L);
        when(motherRepository.findById(3L)).thenReturn(Optional.of(mother));
        when(pregnancyRepository.save(any(Pregnancy.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pregnancy pregnancy = ancService.createPregnancy(new PregnancyRequest(
            3L, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 10, 8), 1, 0
        ));

        assertEquals(mother, pregnancy.getMother());
    }

    @Test
    void rejectsVisitForUnknownPregnancy() {
        when(pregnancyRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ancService.addVisit(
            404L, new AncVisitRequest(LocalDate.now(), null, null, null, null, null)
        ));
    }

    @Test
    void recordsVisitForExistingPregnancy() {
        Pregnancy pregnancy = new Pregnancy();
        when(pregnancyRepository.findById(8L)).thenReturn(Optional.of(pregnancy));
        when(ancVisitRepository.save(any(AncVisit.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AncVisit visit = ancService.addVisit(
            8L, new AncVisitRequest(LocalDate.now(), null, 120, 80, "Synthetic note", null)
        );

        assertEquals(pregnancy, visit.getPregnancy());
        assertEquals(120, visit.getSystolicBp());
    }
}
