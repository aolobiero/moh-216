package ke.co.moh216.repository;
import ke.co.moh216.entity.StaffProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface StaffProfileRepository extends JpaRepository<StaffProfile, Long> {
    Optional<StaffProfile> findByUsername(String username);
}
