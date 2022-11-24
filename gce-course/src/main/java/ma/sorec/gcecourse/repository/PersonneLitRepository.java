package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.PersonneLit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PersonneLitRepository extends JpaRepository<PersonneLit, Long> {

    PersonneLit findFirstByReservationId(Long id);

    List<PersonneLit> findAllByReservationId(Long id);

    List<PersonneLit> findAllByLitId(Long id);

    List<PersonneLit> findAllByPersonneId(String id);

//    List<PersonneLit> findAllByDateDebutGreaterThanEqualAndDateFinLessThanEqual(Date dateDebut, Date dateFin);
}
