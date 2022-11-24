package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Profil;

import java.util.List;

public interface ProfilService {

    List<Profil> listAll();

    Profil save(Profil profil);

    Profil get(Long id);

    void delete(Long id);

    Profil getByCode(String code);

}
