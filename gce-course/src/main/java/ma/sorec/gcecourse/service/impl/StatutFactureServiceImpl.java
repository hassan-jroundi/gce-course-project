package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.StatutFacture;
import ma.sorec.gcecourse.repository.StatutFactureRepository;
import ma.sorec.gcecourse.service.StatutFactureService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StatutFactureServiceImpl implements StatutFactureService {

    @Autowired
    private StatutFactureRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<StatutFacture> listAll() {
        return repository.findAll();
    }

    @Override
    public StatutFacture save(StatutFacture statutFacture) {

        StatutFacture entity = repository.save(statutFacture);

        return entity;
    }

    @Override
    public StatutFacture get(String id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(String id) {

        StatutFacture entity = this.get(id);

        repository.delete(entity);
    }
}
