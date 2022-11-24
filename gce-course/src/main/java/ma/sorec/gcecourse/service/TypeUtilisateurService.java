package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.TypeUtilisateur;

import java.util.List;

public interface TypeUtilisateurService {

    List<TypeUtilisateur> listAll();

    TypeUtilisateur save(TypeUtilisateur typeUtilisateur);

    TypeUtilisateur get(String id);

    void delete(String id);

}
