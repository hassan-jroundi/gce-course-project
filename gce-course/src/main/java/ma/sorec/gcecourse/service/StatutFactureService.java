package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.StatutFacture;

import java.util.List;

public interface StatutFactureService {

    List<StatutFacture> listAll();

    StatutFacture save(StatutFacture statutFacture);

    StatutFacture get(String id);

    void delete(String id);

}
