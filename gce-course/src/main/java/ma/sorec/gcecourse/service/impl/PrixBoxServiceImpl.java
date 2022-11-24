package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Box;
import ma.sorec.gcecourse.data.PrixBox;
import ma.sorec.gcecourse.repository.PrixBoxRepository;
import ma.sorec.gcecourse.service.BoxService;
import ma.sorec.gcecourse.service.PrixBoxService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PrixBoxServiceImpl implements PrixBoxService {

    @Autowired
    private PrixBoxRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    BoxService boxService;

    @Override
    public List<PrixBox> listAll() {
        return repository.findAll();
    }

    @Override
    public PrixBox save(PrixBox prixBox) {

        PrixBox entity = repository.save(prixBox);

        return entity;
    }

    @Override
    public PrixBox get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        PrixBox entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public List<PrixBox> getAllByIdBox(String idBox) {
        return repository.findAllByIdBox(idBox);
    }

    @Override
    public List<PrixBox> getLastPrixBox(String idBox) {
        List<PrixBox> entities = repository.findAllByIdBoxAndDateFinIsNull(idBox);
        return repository.findAllByIdBoxAndDateFinIsNull(idBox);
    }

    @Override
    public PrixBox getPrixReservation(String idBox, Date dateCreation, String typePrix) {
        PrixBox prixBox = new PrixBox();
        List<PrixBox> prixBoxes = repository.findAllByIdBoxAndTypePrix(idBox, typePrix);
        for (PrixBox item : prixBoxes) {
            if (item.getDateFin() == null) {
                if (dateCreation.after(item.getDateDebut())) {
                    prixBox = item;
                    break;
                }
            } else {
                if (dateCreation.after(item.getDateDebut()) && dateCreation.before(item.getDateFin())) {
                    prixBox = item;
                    break;
                }
            }
        }
        return prixBox;
    }

    @Override
    public List<PrixBox> getAllPrixUnitaireByIdBox(String idBox) {
        return repository.findAllByIdBoxAndTypePrix(idBox, "U");
    }

    @Override
    public List<PrixBox> getAllPrixForfaitaireByIdBox(String idBox) {
        return repository.findAllByIdBoxAndTypePrix(idBox, "F");
    }

    @Override
    public PrixBox getLastPrixUnitaire(String idBox) {
        return repository.findFirstByIdBoxAndTypePrixAndDateFinIsNull(idBox, "U");
    }

    @Override
    public PrixBox getLastPrixForfaitaire(String idBox) {
        return repository.findFirstByIdBoxAndTypePrixAndDateFinIsNull(idBox, "F");
    }

    @Override
    public void changerPrixDeTousLesBoxs(Long montant, String typePrix) {
        List<Box> boxes = boxService.listAll();
        for (Box box : boxes) {

            if (typePrix.equals("F")) {
                PrixBox ancien = this.getLastPrixForfaitaire(box.getId().toString());
                ancien.setDateFin(new Date());
                repository.save(ancien);
            }

            if (typePrix.equals("U")) {
                PrixBox ancien = this.getLastPrixUnitaire(box.getId().toString());
                ancien.setDateFin(new Date());
                repository.save(ancien);
            }

            PrixBox prixBox = PrixBox.builder()
                    .idBox(box.getId().toString())
                    .dateCreation(new Date())
                    .dateDebut(new Date())
                    .typePrix(typePrix)
                    .montant(montant)
                    .build();

            repository.save(prixBox);
        }
    }
}
