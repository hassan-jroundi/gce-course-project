package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.UtilisateurSite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtilisateurSiteRepository extends JpaRepository<UtilisateurSite, Long> {

    UtilisateurSite findFirstByUtilisateurId(Long id);

    List<UtilisateurSite> findAllByUtilisateurId(Long id);
}
