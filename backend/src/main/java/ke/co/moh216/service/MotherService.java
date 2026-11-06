package ke.co.moh216.service;

import ke.co.moh216.entity.Mother;
import ke.co.moh216.repository.MotherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MotherService {

    private final MotherRepository motherRepository;

    public MotherService(MotherRepository motherRepository) {
        this.motherRepository = motherRepository;
    }

    @Transactional
    public Mother registerMother(Mother mother) {
        mother.setId(null);

        if (mother.getFullName() == null ||
            mother.getFullName().isBlank()) {
            throw new IllegalArgumentException("Full name is required");
        }

        if (mother.getDateOfBirth() == null) {
            throw new IllegalArgumentException("Date of birth is required");
        }

        if (mother.getGravida() != null && mother.getGravida() < 0) {
            throw new IllegalArgumentException("Gravida cannot be negative");
        }

        if (mother.getParity() != null && mother.getParity() < 0) {
            throw new IllegalArgumentException("Parity cannot be negative");
        }

        return motherRepository.save(mother);
    }

    @Transactional(readOnly = true)
    public List<Mother> getAllMothers() {
        return motherRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Mother> searchMothers(String search) {
        if (search == null || search.isBlank()) {
            return getAllMothers();
        }
        String query = search.trim();
        return motherRepository
            .findByFullNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCaseOrCountyContainingIgnoreCaseOrderByRegisteredAtDesc(
                query, query, query);
    }

    @Transactional(readOnly = true)
    public Mother getMotherById(Long id) {
        return motherRepository.findById(id)
            .orElseThrow(() ->
                new NoSuchElementException("Mother not found"));
    }
}
