package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.UtilisateurSite;

import java.util.List;

public interface UtilisateurSiteService {

    List<UtilisateurSite> listAll();

    UtilisateurSite save(UtilisateurSite utilisateurSite);

    UtilisateurSite get(Long id);

    void delete(Long id);

    UtilisateurSite getByUtilisateurId(Long id);

    List<UtilisateurSite> getAllByUtilisateurId(Long id);

}
