package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.ChevalPiste;
import ma.sorec.gcecourse.data.Reservation;

import java.util.Date;
import java.util.List;

public interface ChevalPisteService {

    List<ChevalPiste> listAll();

    ChevalPiste save(ChevalPiste chevalPiste);

    ChevalPiste get(Long id);

    void delete(Long id);

    Long getNombreHeuresReservation(Long id);

    ChevalPiste getChevalPisteByReservationId(Long id);

    List<ChevalPiste> getChevalPisteByHour(String heure);

    List<ChevalPiste> getChevalPisteByChevalId(Long id);

    ChevalPiste creerChevalPiste(Reservation reservation, Long idPiste, Long idCheval, Date dateDebut, Date dateFin, String typePrix);

    List<ChevalPiste> getAllChevalPistesByReservationId(Long idReservation);

}
