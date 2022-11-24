package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.RelanceFacturation;
import ma.sorec.gcecourse.repository.RelanceFacturationRepository;
import ma.sorec.gcecourse.service.RelanceFacturationService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class RelanceFacturationServiceImpl implements RelanceFacturationService {

    @Autowired
    private RelanceFacturationRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<RelanceFacturation> listAll() {
        return repository.findAll();
    }

    @Override
    public RelanceFacturation save(RelanceFacturation relanceFacturation) {

        RelanceFacturation entity = repository.save(relanceFacturation);

        return entity;
    }

    @Override
    public RelanceFacturation get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        RelanceFacturation entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public RelanceFacturation getByIdFacture(String idFacture) {
        return repository.findFirstByIdFacture(idFacture);
    }
}
