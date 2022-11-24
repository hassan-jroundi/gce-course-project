package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Lit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LitRepository extends JpaRepository<Lit, Long> {

    List<Lit> findAllByChambreId(Long idChambre);

    List<Lit> findAllByChambreImmeubleSiteNom(String nomSite);

    List<Lit> findAllByChambreImmeubleId(Long idImmeuble);

    List<Lit> findAllByChambreIdAndIsActifIsTrueOrderByNom(Long idChambre);

    Lit findFirstByChambreIdOrderByOrdreDesc(Long idChambre);

}
