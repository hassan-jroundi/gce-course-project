package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.PrixPiste;

import java.util.Date;
import java.util.List;

public interface PrixPisteService {

    List<PrixPiste> listAll();

    PrixPiste save(PrixPiste prixPiste);

    PrixPiste get(Long id);

    void delete(Long id);

    List<PrixPiste> getAllByIdPiste(String idPiste);

    PrixPiste getLastPrixPiste(String idPiste);

    PrixPiste getPrixReservation(String idPiste, Date dateCreation);

//    PrixPiste getByNomPiste(String nomPiste);
//
//    List<PrixPiste> getAllByNomPiste(String nomPiste);
//
//    List<PrixPiste> getAllByIdPiste(Long idPiste);

}
