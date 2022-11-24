package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.UtilisateurSite;
import ma.sorec.gcecourse.repository.UtilisateurSiteRepository;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import ma.sorec.gcecourse.service.UtilisateurSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UtilisateurSiteServiceImpl implements UtilisateurSiteService {

    @Autowired
    private UtilisateurSiteRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<UtilisateurSite> listAll() {
        return repository.findAll();
    }

    @Override
    public UtilisateurSite save(UtilisateurSite utilisateurSite) {

        UtilisateurSite entity = repository.save(utilisateurSite);

        return entity;
    }

    @Override
    public UtilisateurSite get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        UtilisateurSite entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public UtilisateurSite getByUtilisateurId(Long id) {
        return repository.findFirstByUtilisateurId(id);
    }

    @Override
    public List<UtilisateurSite> getAllByUtilisateurId(Long id) {
        return repository.findAllByUtilisateurId(id);
    }
}
