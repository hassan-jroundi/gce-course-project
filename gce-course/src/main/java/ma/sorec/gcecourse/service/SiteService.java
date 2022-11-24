package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Site;

import java.util.List;

public interface SiteService {

    List<Site> listAll();

    Site save(Site site);

    Site get(Long id);

    void delete(Long id);

    Site getByNom(String nom);

}
