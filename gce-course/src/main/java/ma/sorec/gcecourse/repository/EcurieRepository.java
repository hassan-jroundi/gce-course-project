package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Ecurie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EcurieRepository extends JpaRepository<Ecurie, Long> {

    List<Ecurie> findAllBySiteNom(String nomSite);

    List<Ecurie> findAllByNomIgnoreCase(String nom);
}
