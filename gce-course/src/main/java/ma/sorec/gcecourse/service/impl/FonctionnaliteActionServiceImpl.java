package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.FonctionnaliteAction;
import ma.sorec.gcecourse.repository.FonctionnaliteActionRepository;
import ma.sorec.gcecourse.service.FonctionnaliteActionService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FonctionnaliteActionServiceImpl implements FonctionnaliteActionService {

    @Autowired
    private FonctionnaliteActionRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<FonctionnaliteAction> listAll() {
        return repository.findAll();
    }

    @Override
    public FonctionnaliteAction save(FonctionnaliteAction fonctionnaliteAction) {

        fonctionnaliteAction.setDateCreation(new Date());
        fonctionnaliteAction.setUserCreation(utilisateurSessionService.getByIdSession(fonctionnaliteAction.getIdSession()).getUtilisateur().getId());

        return repository.save(fonctionnaliteAction);
    }

    @Override
    public FonctionnaliteAction get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        FonctionnaliteAction entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public List<FonctionnaliteAction> getAllByFonctionnaliteId(Long id) {
        return repository.findAllByFonctionnaliteId(id);
    }
}
