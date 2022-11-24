package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Ecurie;

import java.util.List;

public interface EcurieService {

    List<Ecurie> listAll();

    Ecurie save(Ecurie ecurie);

    Ecurie get(Long id);

    void delete(Long id);

    List<Ecurie> getAllByNomSite(String nomSite);

}
