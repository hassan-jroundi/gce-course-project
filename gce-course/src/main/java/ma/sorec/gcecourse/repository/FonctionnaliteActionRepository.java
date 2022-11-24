package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.FonctionnaliteAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FonctionnaliteActionRepository extends JpaRepository<FonctionnaliteAction, Long> {

    List<FonctionnaliteAction> findAllByFonctionnaliteId(Long id);

}
