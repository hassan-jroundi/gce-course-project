package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Lit;
import ma.sorec.gcecourse.data.PrixLit;
import ma.sorec.gcecourse.repository.PrixLitRepository;
import ma.sorec.gcecourse.service.ChambreService;
import ma.sorec.gcecourse.service.LitService;
import ma.sorec.gcecourse.service.PrixLitService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PrixLitServiceImpl implements PrixLitService {

    @Autowired
    private PrixLitRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    LitService litService;

    @Override
    public List<PrixLit> listAll() {
        return repository.findAll();
    }

    @Override
    public PrixLit save(PrixLit prixLit) {

        PrixLit entity = repository.save(prixLit);

        return entity;
    }

    @Override
    public PrixLit get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        PrixLit entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public List<PrixLit> getAllByIdLit(String idChambre) {
        return repository.findAllByIdChambre(idChambre);
    }

    @Override
    public PrixLit getLasPrixLit(String idChambre, String typePrix) {
        return repository.findFirstByIdChambreAndTypePrixAndDateFinIsNull(idChambre, typePrix);
    }

    @Override
    public PrixLit getPrixReservation(String idChambre, Date dateCreation, String typePrix) {
        PrixLit prixLit = new PrixLit();
        List<PrixLit> prixLits = repository.findAllByIdChambreAndTypePrix(idChambre, typePrix);
        for (PrixLit item : prixLits) {
            if (item.getDateFin() == null) {
                if (dateCreation.after(item.getDateDebut())) {
                    prixLit = item;
                    break;
                }
            } else {
                if (dateCreation.after(item.getDateDebut()) && dateCreation.before(item.getDateFin())) {
                    prixLit = item;
                    break;
                }
            }
        }
        return prixLit;
    }

    @Override
    public List<PrixLit> getAllByIdChambre(String idChambre, String typePrix) {
        return repository.findAllByIdChambreAndTypePrix(idChambre, typePrix);
    }

    @Override
    public void changerPrixDeTousLesLits(Long montant, String typePrix) {
        List<Lit> lits = litService.listAll();
        for (Lit lit : lits) {

            PrixLit ancien = this.getLasPrixLit(lit.getChambre().getId().toString(), typePrix);
            ancien.setDateFin(new Date());
            repository.save(ancien);

            PrixLit prixLit = PrixLit.builder()
                    .idChambre(lit.getChambre().getId().toString())
                    .dateCreation(new Date())
                    .dateDebut(new Date())
                    .typePrix(typePrix)
                    .montant(montant)
                    .build();

            repository.save(prixLit);
        }
    }
}
