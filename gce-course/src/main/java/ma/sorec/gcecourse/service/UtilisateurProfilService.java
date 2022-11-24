package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.UtilisateurProfil;

import java.util.List;

public interface UtilisateurProfilService {

    List<UtilisateurProfil> listAll();

    UtilisateurProfil save(UtilisateurProfil utilisateurProfil);

    UtilisateurProfil get(Long id);

    void delete(Long id);

    UtilisateurProfil getByUtilisateurId(Long id);

    List<UtilisateurProfil> getAllByUtilisateurId(Long id);

}
