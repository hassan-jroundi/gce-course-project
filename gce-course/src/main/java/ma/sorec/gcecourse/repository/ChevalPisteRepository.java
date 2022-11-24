package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.ChevalPiste;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChevalPisteRepository extends JpaRepository<ChevalPiste, Long> {

    ChevalPiste findFirstByReservationId(Long id);

    List<ChevalPiste> findAllByReservationId(Long id);

    List<ChevalPiste> findAllByChevalId(Long id);

}
