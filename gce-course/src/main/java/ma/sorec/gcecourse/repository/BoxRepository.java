package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Box;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoxRepository extends JpaRepository<Box, Long> {

    List<Box> findAllByEcurieId(Long id);

    List<Box> findAllByEcurieSiteNom(String nomSite);

    Box findFirstByNomIgnoreCase(String nom);

    Box findFirstByEcurieIdOrderByOrdreDesc(Long idEcurie);
}
