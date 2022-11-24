package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.PrixLit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PrixLitRepository extends JpaRepository<PrixLit, Long> {

    List<PrixLit> findAllByIdChambre(String idChambre);

    PrixLit findFirstByIdChambreAndDateFinIsNull(String idChambre);

    PrixLit findFirstByIdChambreAndDateDebutBeforeOrDateFinAfter(String idChambre, Date date1, Date date2);

    PrixLit findFirstByIdChambreAndTypePrixAndDateFinIsNull(String idChambre, String typePrix);

    List<PrixLit> findAllByIdChambreAndTypePrix(String idChambre, String typePrix);
}
