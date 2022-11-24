package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.TypePersonne;

import java.util.List;

public interface TypePersonneService {

    List<TypePersonne> listAll();

    TypePersonne save(TypePersonne typePersonne);

    TypePersonne get(Long id);

    void delete(Long id);

}
