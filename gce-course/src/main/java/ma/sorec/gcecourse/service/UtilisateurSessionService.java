package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.UtilisateurSession;

import java.util.List;

public interface UtilisateurSessionService {

    List<UtilisateurSession> listAll();

    UtilisateurSession save(UtilisateurSession utilisateurSession);

    UtilisateurSession get(Long id);

    void delete(Long id);

    UtilisateurSession getByUtilisateurId(Long id);

    List<UtilisateurSession> getAllByUtilisateurId(Long id);

    UtilisateurSession getDernierByUtilisateurId(Long id);

    UtilisateurSession getByIdSession(String idSession);

}
