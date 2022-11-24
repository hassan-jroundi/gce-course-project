package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.PrixPiste;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PrixPisteRepository extends JpaRepository<PrixPiste, Long> {

    List<PrixPiste> findAllByIdPiste(String idPiste);

    PrixPiste findFirstByIdPisteAndDateFinIsNull(String idPiste);

    PrixPiste findFirstByIdPisteAndDateDebutBeforeAndAndDateFinAfter(String idPiste, Date date1, Date date2);

}
