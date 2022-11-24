package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.PrixBox;

import java.util.Date;
import java.util.List;

public interface PrixBoxService {

    List<PrixBox> listAll();

    PrixBox save(PrixBox prixBox);

    PrixBox get(Long id);

    void delete(Long id);

    List<PrixBox> getAllByIdBox(String idBox);

    List<PrixBox> getLastPrixBox(String idBox);

    PrixBox getPrixReservation(String idBox, Date dateCreation, String typePrix);

    List<PrixBox> getAllPrixUnitaireByIdBox(String idBox);

    List<PrixBox> getAllPrixForfaitaireByIdBox(String idBox);

    PrixBox getLastPrixUnitaire(String idBox);

    PrixBox getLastPrixForfaitaire(String idBox);

    void changerPrixDeTousLesBoxs(Long montant, String typePrix);

}
