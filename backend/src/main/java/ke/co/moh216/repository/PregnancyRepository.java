package ke.co.moh216.repository;

import ke.co.moh216.entity.Pregnancy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PregnancyRepository extends JpaRepository<Pregnancy, Long> {
    List<Pregnancy> findByMotherIdOrderByRecordedAtDesc(Long motherId);
}
