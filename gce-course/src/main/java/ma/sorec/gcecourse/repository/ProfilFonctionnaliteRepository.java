package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Profil;
import ma.sorec.gcecourse.data.ProfilFonctionnalite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfilFonctionnaliteRepository extends JpaRepository<ProfilFonctionnalite, Long> {

    List<ProfilFonctionnalite> findAllByProfilId(Long id);
}
