package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.RelanceFacturation;

import java.util.List;

public interface RelanceFacturationService {

    List<RelanceFacturation> listAll();

    RelanceFacturation save(RelanceFacturation relanceFacturation);

    RelanceFacturation get(Long id);

    void delete(Long id);

    RelanceFacturation getByIdFacture(String idFacture);

}
