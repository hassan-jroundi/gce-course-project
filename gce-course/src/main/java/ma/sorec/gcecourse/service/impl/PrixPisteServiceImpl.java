package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.PrixPiste;
import ma.sorec.gcecourse.repository.PrixPisteRepository;
import ma.sorec.gcecourse.service.PrixPisteService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PrixPisteServiceImpl implements PrixPisteService {

    @Autowired
    private PrixPisteRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<PrixPiste> listAll() {
        return repository.findAll();
    }

    @Override
    public PrixPiste save(PrixPiste prixPiste) {

        PrixPiste entity = repository.save(prixPiste);

        return entity;
    }

    @Override
    public PrixPiste get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        PrixPiste entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public List<PrixPiste> getAllByIdPiste(String idPiste) {
        return repository.findAllByIdPiste(idPiste);
    }

    @Override
    public PrixPiste getLastPrixPiste(String idPiste) {
        return repository.findFirstByIdPisteAndDateFinIsNull(idPiste);
    }

    @Override
    public PrixPiste getPrixReservation(String idPiste, Date dateCreation) {
        PrixPiste prixPiste = new PrixPiste();
        List<PrixPiste> prixPistes = repository.findAllByIdPiste(idPiste);
        for (PrixPiste item : prixPistes) {
            if (item.getDateFin() == null) {
                if (dateCreation.after(item.getDateDebut())) {
                    prixPiste = item;
                    break;
                }
            } else {
                if (dateCreation.after(item.getDateDebut()) && dateCreation.before(item.getDateFin())) {
                    prixPiste = item;
                    break;
                }
            }
        }
        return prixPiste;
    }

//    @Override
//    public PrixPiste getByNomPiste(String nomPiste) {
//
//        return repository.findByPisteNomIgnoreCaseAndDateFinIsNull(nomPiste);
//    }
//
//    @Override
//    public List<PrixPiste> getAllByNomPiste(String nomPiste) {
//
//        return repository.findAllByPisteNomIgnoreCaseOrderByDateDebutDesc(nomPiste);
//    }
//
//    @Override
//    public List<PrixPiste> getAllByIdPiste(Long idPiste) {
//        return repository.findAllByPisteId(idPiste);
//    }
}
