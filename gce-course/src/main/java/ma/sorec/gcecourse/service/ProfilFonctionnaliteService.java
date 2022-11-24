package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.ProfilFonctionnalite;

import java.util.List;

public interface ProfilFonctionnaliteService {

    List<ProfilFonctionnalite> listAll();

    ProfilFonctionnalite save(ProfilFonctionnalite profilFonctionnalite);

    ProfilFonctionnalite get(Long id);

    void delete(Long id);

    List<ProfilFonctionnalite> getAllByProfilId(Long id);

}
