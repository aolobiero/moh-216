package ke.co.moh216.repository;
import ke.co.moh216.entity.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, String> {}
