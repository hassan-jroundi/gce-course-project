package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.UtilisateurSession;
import ma.sorec.gcecourse.repository.UtilisateurSessionRepository;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UtilisateurSessionServiceImpl implements UtilisateurSessionService {

    @Autowired
    private UtilisateurSessionRepository repository;

    @Override
    public List<UtilisateurSession> listAll() {
        return repository.findAll();
    }

    @Override
    public UtilisateurSession save(UtilisateurSession utilisateurSession) {

        UtilisateurSession entity = repository.save(utilisateurSession);

        return entity;
    }

    @Override
    public UtilisateurSession get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        UtilisateurSession entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public UtilisateurSession getByUtilisateurId(Long id) {
        return repository.findFirstByUtilisateurIdAndDateFinIsNull(id);
    }

    @Override
    public List<UtilisateurSession> getAllByUtilisateurId(Long id) {
        return repository.findAllByUtilisateurId(id);
    }

    @Override
    public UtilisateurSession getDernierByUtilisateurId(Long id) {
        return repository.findFirstByUtilisateurIdAndDateFinIsNullOrderByDateDebutDesc(id);
    }

    @Override
    public UtilisateurSession getByIdSession(String idSession) {
        return repository.findFirstByIdSession(idSession);
    }
}
