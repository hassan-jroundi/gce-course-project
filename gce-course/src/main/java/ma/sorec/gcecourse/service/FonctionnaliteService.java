package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Fonctionnalite;

import java.util.List;

public interface FonctionnaliteService {

    List<Fonctionnalite> listAll();

    Fonctionnalite save(Fonctionnalite fonctionnalite);

    Fonctionnalite get(Long id);

    void delete(Long id);

}
