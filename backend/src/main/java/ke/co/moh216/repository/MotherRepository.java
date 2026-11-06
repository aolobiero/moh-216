package ke.co.moh216.repository;

import ke.co.moh216.entity.Mother;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotherRepository extends JpaRepository<Mother, Long> {
    List<Mother> findTop5ByOrderByRegisteredAtDesc();
    List<Mother> findByFullNameContainingIgnoreCaseOrPhoneNumberContainingIgnoreCaseOrCountyContainingIgnoreCaseOrderByRegisteredAtDesc(
        String fullName, String phoneNumber, String county);
}
