package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.PrixBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PrixBoxRepository extends JpaRepository<PrixBox, Long> {

    List<PrixBox> findAllByIdBox(String idBox);

    List<PrixBox> findAllByIdBoxAndDateFinIsNull(String idBox);

    PrixBox findFirstByIdBoxAndDateDebutBeforeAndDateFinAfter(String idBox, Date date1, Date date2);

    List<PrixBox> findAllByIdBoxAndTypePrix(String idBox, String typePrix);

    PrixBox findFirstByIdBoxAndTypePrixAndDateFinIsNull(String idBox, String typePrix);
}
