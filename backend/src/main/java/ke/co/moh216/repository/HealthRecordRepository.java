package ke.co.moh216.repository;
import ke.co.moh216.entity.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
    List<HealthRecord> findByMotherIdOrderByRecordDateDesc(Long motherId);
    List<HealthRecord> findByChildIdOrderByRecordDateDesc(Long childId);
}
