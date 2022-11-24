package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.ProfilFonctionnalite;
import ma.sorec.gcecourse.repository.ProfilFonctionnaliteRepository;
import ma.sorec.gcecourse.service.ProfilFonctionnaliteService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProfilFonctionnaliteServiceImpl implements ProfilFonctionnaliteService {

    @Autowired
    private ProfilFonctionnaliteRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<ProfilFonctionnalite> listAll() {
        return repository.findAll();
    }

    @Override
    public ProfilFonctionnalite save(ProfilFonctionnalite profilFonctionnalite) {

        ProfilFonctionnalite entity = repository.save(profilFonctionnalite);

        return entity;
    }

    @Override
    public ProfilFonctionnalite get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        ProfilFonctionnalite entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public List<ProfilFonctionnalite> getAllByProfilId(Long id) {
        return repository.findAllByProfilId(id);
    }
}
