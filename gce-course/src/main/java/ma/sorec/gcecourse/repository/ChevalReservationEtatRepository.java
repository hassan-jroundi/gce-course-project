package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.ChevalReservationEtat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChevalReservationEtatRepository extends JpaRepository<ChevalReservationEtat, Long> {

    ChevalReservationEtat findFirstByReservationId(Long idReservation);
}
