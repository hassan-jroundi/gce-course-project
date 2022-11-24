package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.ProfilFonctionnaliteAction;
import ma.sorec.gcecourse.repository.ProfilFonctionnaliteActionRepository;
import ma.sorec.gcecourse.service.ProfilFonctionnaliteActionService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProfilFonctionnaliteActionServiceImpl implements ProfilFonctionnaliteActionService {

    @Autowired
    private ProfilFonctionnaliteActionRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<ProfilFonctionnaliteAction> listAll() {
        return repository.findAll();
    }

    @Override
    public ProfilFonctionnaliteAction save(ProfilFonctionnaliteAction profilFonctionnaliteAction) {

        ProfilFonctionnaliteAction entity = repository.save(profilFonctionnaliteAction);

        return entity;
    }

    @Override
    public ProfilFonctionnaliteAction get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        ProfilFonctionnaliteAction entity = this.get(id);

        repository.delete(entity);
    }
}
