package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.TypeUtilisateur;
import ma.sorec.gcecourse.repository.TypeUtilisateurRepository;
import ma.sorec.gcecourse.service.TypeUtilisateurService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TypeUtilisateurServiceImpl implements TypeUtilisateurService {

    @Autowired
    private TypeUtilisateurRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<TypeUtilisateur> listAll() {
        return repository.findAll();
    }

    @Override
    public TypeUtilisateur save(TypeUtilisateur typeUtilisateur) {

        TypeUtilisateur entity = repository.save(typeUtilisateur);

        return entity;
    }

    @Override
    public TypeUtilisateur get(String id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(String id) {

        TypeUtilisateur entity = this.get(id);

        repository.delete(entity);
    }
}
