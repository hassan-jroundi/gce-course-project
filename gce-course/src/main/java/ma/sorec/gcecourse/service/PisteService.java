package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Piste;

import java.util.List;

public interface PisteService {

    List<Piste> listAll();

    Piste save(Piste piste);

    Piste update(Piste piste);

    Piste get(Long id);

    void delete(Long id);

    List<Piste> getAllByNomSite(String nomSite);

}
