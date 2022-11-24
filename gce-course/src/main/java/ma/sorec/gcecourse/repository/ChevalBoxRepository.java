package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.ChevalBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChevalBoxRepository extends JpaRepository<ChevalBox, Long> {

    ChevalBox findFirstByReservationId(Long id);

    List<ChevalBox> findAllByReservationId(Long id);

    List<ChevalBox> findAllByBoxId(Long id);

    List<ChevalBox> findAllByChevalId(Long id);

}
