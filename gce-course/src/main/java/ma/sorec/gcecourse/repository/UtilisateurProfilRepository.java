package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.UtilisateurProfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtilisateurProfilRepository extends JpaRepository<UtilisateurProfil, Long> {

    UtilisateurProfil findFirstByUtilisateurId(Long id);

    List<UtilisateurProfil> findAllByUtilisateurId(Long id);

}
