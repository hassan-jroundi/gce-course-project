package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Lit;

import java.util.List;

public interface LitService {

    List<Lit> listAll();

    Lit save(Lit lit);

    Lit update(Lit lit);

    Lit get(Long id);

    void delete(Long id, String idSession);

    List<Lit> getAllByChambreId(Long idChambre);

    List<Lit> getAllByImmeubleId(Long idImmeuble);

    List<Lit> getAllByNomSite(String nomSite);

    List<Lit> getAllActifLit(Long idChambre);

    Lit ajouterLit(String idChambre, String idSession);

}
