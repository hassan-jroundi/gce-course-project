package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Fonctionnalite;
import ma.sorec.gcecourse.repository.FonctionnaliteRepository;
import ma.sorec.gcecourse.service.FonctionnaliteService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FonctionnaliteServiceImpl implements FonctionnaliteService {

    @Autowired
    private FonctionnaliteRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<Fonctionnalite> listAll() {
        return repository.findAll();
    }

    @Override
    public Fonctionnalite save(Fonctionnalite fonctionnalite) {

        fonctionnalite.setDateCreation(new Date());
        fonctionnalite.setUserCreation(utilisateurSessionService.getByIdSession(fonctionnalite.getIdSession()).getUtilisateur().getId());

        return repository.save(fonctionnalite);
    }

    @Override
    public Fonctionnalite get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        Fonctionnalite entity = this.get(id);

        repository.delete(entity);
    }
}
