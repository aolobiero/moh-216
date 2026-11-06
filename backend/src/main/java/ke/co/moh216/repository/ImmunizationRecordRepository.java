package ke.co.moh216.repository;
import ke.co.moh216.entity.ImmunizationRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ImmunizationRecordRepository extends JpaRepository<ImmunizationRecord, Long> {
    List<ImmunizationRecord> findByChildIdOrderByAdministeredDateDesc(Long childId);
}
