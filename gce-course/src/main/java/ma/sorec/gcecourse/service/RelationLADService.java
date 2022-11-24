package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.RelationLAD;

import java.util.List;

public interface RelationLADService {

    List<RelationLAD> listAll();

    RelationLAD save(RelationLAD relationLAD);

    RelationLAD get(Long id);

    void delete(Long id);

    RelationLAD getByPersonneIdAndDateFinContratIsNull(String id);

}
