package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.TypePersonne;
import ma.sorec.gcecourse.repository.TypePersonneRepository;
import ma.sorec.gcecourse.service.TypePersonneService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TypePersonneServiceImpl implements TypePersonneService {

    @Autowired
    private TypePersonneRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<TypePersonne> listAll() {
        return repository.findAll();
    }

    @Override
    public TypePersonne save(TypePersonne typePersonne) {

        TypePersonne entity = repository.save(typePersonne);

        return entity;
    }

    @Override
    public TypePersonne get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        TypePersonne entity = this.get(id);

        repository.delete(entity);
    }
}
