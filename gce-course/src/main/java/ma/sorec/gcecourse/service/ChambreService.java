package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Chambre;

import java.util.List;

public interface ChambreService {

    List<Chambre> listAll();

    Chambre save(Chambre chambre);

    Chambre update(Chambre chambre);

    Chambre get(Long id);

    void delete(Long id);

    List<Chambre> getAllByImmeubleId(Long idImmeuble);

    List<Chambre> getAllActifChambre(Long idImmeuble);

}
