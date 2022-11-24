package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.ProfilFonctionnaliteAction;

import java.util.List;

public interface ProfilFonctionnaliteActionService {

    List<ProfilFonctionnaliteAction> listAll();

    ProfilFonctionnaliteAction save(ProfilFonctionnaliteAction profilFonctionnaliteAction);

    ProfilFonctionnaliteAction get(Long id);

    void delete(Long id);

}
