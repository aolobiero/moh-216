package ke.co.moh216.repository;

import ke.co.moh216.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findAllByOrderByRegisteredAtDesc();

    List<Child> findByFullNameContainingIgnoreCaseOrderByRegisteredAtDesc(String search);

    List<Child> findTop5ByOrderByRegisteredAtDesc();
}
