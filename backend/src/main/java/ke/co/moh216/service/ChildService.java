package ke.co.moh216.service;

import ke.co.moh216.dto.ChildRequest;
import ke.co.moh216.dto.ChildResponse;
import ke.co.moh216.entity.Child;
import ke.co.moh216.entity.Mother;
import ke.co.moh216.repository.ChildRepository;
import ke.co.moh216.repository.MotherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ChildService {

    private final ChildRepository childRepository;
    private final MotherRepository motherRepository;

    public ChildService(ChildRepository childRepository, MotherRepository motherRepository) {
        this.childRepository = childRepository;
        this.motherRepository = motherRepository;
    }

    @Transactional
    public ChildResponse registerChild(ChildRequest request) {
        Mother mother = motherRepository.findById(request.motherId())
            .orElseThrow(() -> new NoSuchElementException("Mother not found"));

        Child child = new Child();
        child.setFullName(request.fullName().trim());
        child.setDateOfBirth(request.dateOfBirth());
        child.setSex(request.sex().trim());
        child.setMother(mother);
        child.setBirthWeightKg(request.birthWeightKg());
        child.setBirthFacility(trimToNull(request.birthFacility()));
        child.setNeonatalHistory(trimToNull(request.neonatalHistory()));

        return ChildResponse.from(childRepository.save(child));
    }

    @Transactional(readOnly = true)
    public List<ChildResponse> searchChildren(String search) {
        List<Child> children = search == null || search.isBlank()
            ? childRepository.findAllByOrderByRegisteredAtDesc()
            : childRepository.findByFullNameContainingIgnoreCaseOrderByRegisteredAtDesc(search.trim());

        return children.stream().map(ChildResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ChildResponse getChild(Long id) {
        return childRepository.findById(id)
            .map(ChildResponse::from)
            .orElseThrow(() -> new NoSuchElementException("Child not found"));
    }

    private String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
