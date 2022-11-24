package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.PrixLit;

import java.util.Date;
import java.util.List;

public interface PrixLitService {

    List<PrixLit> listAll();

    PrixLit save(PrixLit prixLit);

    PrixLit get(Long id);

    void delete(Long id);

    List<PrixLit> getAllByIdLit(String idLit);

    PrixLit getLasPrixLit(String idLit, String typePrix);

    PrixLit getPrixReservation(String idLit, Date dateCreation, String typePrix);

    List<PrixLit> getAllByIdChambre(String idChambre, String typePrix);

    void changerPrixDeTousLesLits(Long montant, String typePrix);

}
