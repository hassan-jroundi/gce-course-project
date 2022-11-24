package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.UtilisateurSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtilisateurSessionRepository extends JpaRepository<UtilisateurSession, Long> {

    UtilisateurSession findFirstByUtilisateurIdAndDateFinIsNull(Long id);

    List<UtilisateurSession> findAllByUtilisateurId(Long id);

    UtilisateurSession findFirstByUtilisateurIdAndDateFinIsNullOrderByDateDebutDesc(Long id);

    UtilisateurSession findFirstByIdSession(String idSession);
}
