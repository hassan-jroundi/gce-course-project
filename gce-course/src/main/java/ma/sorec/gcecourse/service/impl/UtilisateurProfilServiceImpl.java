package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.UtilisateurProfil;
import ma.sorec.gcecourse.repository.UtilisateurProfilRepository;
import ma.sorec.gcecourse.service.UtilisateurProfilService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UtilisateurProfilServiceImpl implements UtilisateurProfilService {

    @Autowired
    private UtilisateurProfilRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<UtilisateurProfil> listAll() {
        return repository.findAll();
    }

    @Override
    public UtilisateurProfil save(UtilisateurProfil utilisateurProfil) {

        UtilisateurProfil entity = repository.save(utilisateurProfil);

        return entity;
    }

    @Override
    public UtilisateurProfil get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        UtilisateurProfil entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public UtilisateurProfil getByUtilisateurId(Long id) {
        return repository.findFirstByUtilisateurId(id);
    }

    @Override
    public List<UtilisateurProfil> getAllByUtilisateurId(Long id) {
        return repository.findAllByUtilisateurId(id);
    }
}
