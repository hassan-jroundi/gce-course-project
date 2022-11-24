package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Profil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfilRepository extends JpaRepository<Profil, Long> {

    Profil findFirstByCode(String code);
}
