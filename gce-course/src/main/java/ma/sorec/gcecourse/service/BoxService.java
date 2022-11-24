package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Box;

import java.util.List;

public interface BoxService {

    List<Box> listAll();

    Box save(Box box);

    Box get(Long id);

    void delete(Long id);

    List<Box> getAllByEcurieId(Long id);

    List<Box> getAllByNomSite(String nomSite);

    Box update(Box box);

    Box ajouterBox(String idEcurie);

}
