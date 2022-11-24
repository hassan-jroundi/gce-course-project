package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.ModePaiement;

import java.util.List;

public interface ModePaiementService {

    List<ModePaiement> listAll();

    ModePaiement save(ModePaiement modePaiement);

    ModePaiement get(String id);

    void delete(String id);

}
