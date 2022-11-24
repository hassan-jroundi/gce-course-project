package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.StatutReservation;

import java.util.List;

public interface StatutReservationService {

    List<StatutReservation> listAll();

    StatutReservation save(StatutReservation statutReservation);

    StatutReservation get(String id);

    void delete(String id);

}
