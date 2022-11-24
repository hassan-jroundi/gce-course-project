package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.NombreLitsHistorique;
import ma.sorec.gcecourse.repository.NombreLitsHistoriqueRepository;
import ma.sorec.gcecourse.service.NombreLitsHistoriqueService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class NombreLitsHistoriqueServiceImpl implements NombreLitsHistoriqueService {

    @Autowired
    private NombreLitsHistoriqueRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<NombreLitsHistorique> listAll() {
        return repository.findAll();
    }

    @Override
    public NombreLitsHistorique save(NombreLitsHistorique nombreLitsHistorique) {

        return repository.save(nombreLitsHistorique);
    }

    @Override
    public NombreLitsHistorique get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        NombreLitsHistorique entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public List<NombreLitsHistorique> getAllByIdChambre(String idChambre) {
        return repository.findAllByIdChambre(idChambre);
    }

    @Override
    public NombreLitsHistorique getLastNombreLitsHistorique(String idChambre) {
        return repository.findFirstByIdChambreAndDateFinIsNull(idChambre);
    }
}
