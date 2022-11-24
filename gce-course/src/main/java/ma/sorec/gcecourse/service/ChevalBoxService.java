package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.ChevalBox;

import java.util.List;

public interface ChevalBoxService {

    List<ChevalBox> listAll();

    ChevalBox save(ChevalBox chevalBox);

    ChevalBox get(Long id);

    void delete(Long id);

    Long getNombreJoursReservation(Long id);

    ChevalBox getChevalBoxByReservationId(Long idReservation);

    List<ChevalBox> getAllChevalBoxsByReservationId(Long idReservation);

    List<ChevalBox> getChevalBoxByBoxId(Long idBox);

    List<ChevalBox> getChevalBoxByChevalId(Long idCheval);

}
