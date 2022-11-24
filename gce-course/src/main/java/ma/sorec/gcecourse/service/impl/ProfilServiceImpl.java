package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Profil;
import ma.sorec.gcecourse.repository.ProfilRepository;
import ma.sorec.gcecourse.service.ProfilService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProfilServiceImpl implements ProfilService {

    @Autowired
    private ProfilRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<Profil> listAll() {
        return repository.findAll();
    }

    @Override
    public Profil save(Profil profil) {

        Profil entity = repository.save(profil);

        return entity;
    }

    @Override
    public Profil get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        Profil entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public Profil getByCode(String code) {
        return repository.findFirstByCode(code);
    }
}
