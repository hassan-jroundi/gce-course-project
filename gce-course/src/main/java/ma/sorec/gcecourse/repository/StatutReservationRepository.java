package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.data.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatutReservationRepository extends JpaRepository<StatutReservation, String> {

}
