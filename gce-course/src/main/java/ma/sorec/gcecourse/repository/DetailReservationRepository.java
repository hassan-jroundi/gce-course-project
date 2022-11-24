package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.DetailReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface DetailReservationRepository extends JpaRepository<DetailReservation, Long> {

    List<DetailReservation> findAllByIdReservationOrderByDateDebutAsc(String idReservation);

    List<DetailReservation> findAllByDateDebutGreaterThanEqualAndDateFinLessThanEqualAndIdReservationIsNotNull(Date dateDebut, Date dateFin);

    List<DetailReservation> findAllByFactureId(Long idFacture);

}
