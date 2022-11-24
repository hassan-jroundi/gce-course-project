package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Utilisateur findFirstByLoginIgnoreCase(String login);
}
