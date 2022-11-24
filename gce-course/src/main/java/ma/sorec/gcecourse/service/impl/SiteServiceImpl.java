package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Site;
import ma.sorec.gcecourse.repository.SiteRepository;
import ma.sorec.gcecourse.service.SiteService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SiteServiceImpl implements SiteService {

    @Autowired
    private SiteRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<Site> listAll() {
        return repository.findAll();
    }

    @Override
    public Site save(Site site) {

        site.setDateCreation(new Date());
        site.setUserCreation(utilisateurSessionService.getByIdSession(site.getIdSession()).getUtilisateur().getId());
        Site entity = repository.save(site);

        return entity;
    }

    @Override
    public Site get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        Site entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public Site getByNom(String nom) {
        return repository.findFirstByNomIgnoreCase(nom);
    }
}
