package ke.co.moh216;

import ke.co.moh216.dto.ChildRequest;
import ke.co.moh216.entity.Child;
import ke.co.moh216.entity.Mother;
import ke.co.moh216.repository.ChildRepository;
import ke.co.moh216.repository.MotherRepository;
import ke.co.moh216.service.ChildService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChildServiceTest {

    @Mock
    private ChildRepository childRepository;

    @Mock
    private MotherRepository motherRepository;

    @InjectMocks
    private ChildService childService;

    @Test
    void rejectsRegistrationWhenMotherDoesNotExist() {
        when(motherRepository.findById(99L)).thenReturn(Optional.empty());

        ChildRequest request = new ChildRequest(
            "Synthetic Child",
            LocalDate.of(2025, 1, 1),
            "Female",
            99L,
            null,
            null,
            null
        );

        assertThrows(NoSuchElementException.class, () -> childService.registerChild(request));
    }

    @Test
    void registersChildAgainstExistingMother() {
        Mother mother = new Mother();
        mother.setId(7L);
        mother.setFullName("Synthetic Mother");
        when(motherRepository.findById(7L)).thenReturn(Optional.of(mother));
        when(childRepository.save(any(Child.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChildRequest request = new ChildRequest(
            "Synthetic Child",
            LocalDate.of(2025, 1, 1),
            "Male",
            7L,
            null,
            "Synthetic Facility",
            null
        );

        assertEquals("Synthetic Child", childService.registerChild(request).fullName());
    }
}
