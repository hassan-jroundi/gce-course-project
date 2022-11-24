package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Facture;

import java.util.List;

public interface FactureService {

    List<Facture> listAll();

    Facture save(Facture facture);

    Facture get(Long id);

    void delete(Long id);

    List<Facture> getAllByPersonneId(String id);

    Double getMontantHTFacture(Long id);

    Facture mettreEnPaye(Long id, String idSession, String codeModePaiement);

    void relanceFactures();

    List<Facture> getAllByIdOperateur(String idOperateur);

    List<Facture> getAllEnCoursDePaiement();

}
