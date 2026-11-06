package ke.co.moh216.repository;

import ke.co.moh216.entity.AncVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AncVisitRepository extends JpaRepository<AncVisit, Long> {
    List<AncVisit> findByPregnancyIdOrderByVisitDateDesc(Long pregnancyId);
}
