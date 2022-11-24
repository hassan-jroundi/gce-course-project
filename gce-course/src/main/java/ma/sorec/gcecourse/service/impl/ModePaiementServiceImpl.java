package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.ModePaiement;
import ma.sorec.gcecourse.repository.ModePaiementRepository;
import ma.sorec.gcecourse.service.ModePaiementService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ModePaiementServiceImpl implements ModePaiementService {

    @Autowired
    private ModePaiementRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<ModePaiement> listAll() {
        return repository.findAll();
    }

    @Override
    public ModePaiement save(ModePaiement modePaiement) {

        return repository.save(modePaiement);
    }

    @Override
    public ModePaiement get(String id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(String id) {

        ModePaiement entity = this.get(id);
        repository.delete(entity);
    }
}
