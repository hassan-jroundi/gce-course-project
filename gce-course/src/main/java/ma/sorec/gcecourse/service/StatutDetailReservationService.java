package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.StatutDetailReservation;

import java.util.List;

public interface StatutDetailReservationService {

    List<StatutDetailReservation> listAll();

    StatutDetailReservation save(StatutDetailReservation statutDetailReservation);

    StatutDetailReservation get(String id);

    void delete(String id);

}
