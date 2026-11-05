package ke.co.moh216.repository;

import ke.co.moh216.entity.Mother;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotherRepository extends JpaRepository<Mother, Long> {
}
