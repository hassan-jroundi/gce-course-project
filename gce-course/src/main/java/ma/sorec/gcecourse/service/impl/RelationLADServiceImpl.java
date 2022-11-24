package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.RelationLAD;
import ma.sorec.gcecourse.repository.RelationLADRepository;
import ma.sorec.gcecourse.service.RelationLADService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RelationLADServiceImpl implements RelationLADService {

    @Autowired
    private RelationLADRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<RelationLAD> listAll() {
        return repository.findAll();
    }

    @Override
    public RelationLAD save(RelationLAD relationLAD) {

        RelationLAD entity = repository.save(relationLAD);

        return entity;
    }

    @Override
    public RelationLAD get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        RelationLAD entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public RelationLAD getByPersonneIdAndDateFinContratIsNull(String id) {
        return repository.findFirstByIdLADAndDateFinContratIsNull(id);
    }
}
