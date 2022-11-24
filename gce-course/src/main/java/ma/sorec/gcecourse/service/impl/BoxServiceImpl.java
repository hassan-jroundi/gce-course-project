package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Box;
import ma.sorec.gcecourse.data.Ecurie;
import ma.sorec.gcecourse.data.PrixBox;
import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.exceptions.CustomException;
import ma.sorec.gcecourse.repository.BoxRepository;
import ma.sorec.gcecourse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BoxServiceImpl implements BoxService {

    @Autowired
    private BoxRepository repository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PrixBoxService prixBoxService;

    @Autowired
    private UtilisateurSessionService utilisateurSessionService;

    @Autowired
    EcurieService ecurieService;

    @Override
    public List<Box> listAll() {
        return repository.findAll();
    }

    @Override
    public Box save(Box box) {
        Box memeNom = repository.findFirstByNomIgnoreCase(box.getNom());
        if (memeNom != null) {
            throw new CustomException("A", "Le nom du box renseigné est déjà pris.");
        } else {
            Box entity = Box.builder()
                    .nom(box.getNom())
                    .ecurie(box.getEcurie())
                    .isActif(box.getIsActif())
                    .dateCreation(new Date())
                    .userCreation(box.getIdSession() != null ? utilisateurSessionService.getByIdSession(box.getIdSession()).getUtilisateur().getId() : null)
                    .build();
            entity = repository.save(entity);

            PrixBox prixUnitaire = PrixBox.builder()
                    .idBox(entity.getId().toString())
                    .dateDebut(new Date())
                    .montant(new Long(box.getPrixUnitaire()))
                    .typePrix("U")
                    .dateCreation(new Date())
                    .userCreation(box.getIdSession() != null ? utilisateurSessionService.getByIdSession(box.getIdSession()).getUtilisateur().getId() : null)
                    .build();
            prixBoxService.save(prixUnitaire);

            PrixBox prixForfaitaire = PrixBox.builder()
                    .idBox(entity.getId().toString())
                    .dateDebut(new Date())
                    .montant(new Long(box.getPrixForfaitaire()))
                    .typePrix("F")
                    .dateCreation(new Date())
                    .userCreation(box.getIdSession() != null ? utilisateurSessionService.getByIdSession(box.getIdSession()).getUtilisateur().getId() : null)
                    .build();
            prixBoxService.save(prixForfaitaire);
            return entity;
        }
    }

    @Override
    public Box get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        Box entity = this.get(id);
        List<Reservation> reservations = reservationService.getAllByChevalBoxBoxId(id);
        if (!(reservations.isEmpty())) {
            throw new CustomException("C", "Ce box est lié à des réservations et ne peut être supprimé.");
        } else {
            repository.delete(entity);
        }
    }

    @Override
    public List<Box> getAllByEcurieId(Long id) {
        return repository.findAllByEcurieId(id);
    }

    @Override
    public List<Box> getAllByNomSite(String nomSite) {
        return repository.findAllByEcurieSiteNom(nomSite);
    }

    @Override
    public Box update(Box box) {
        Box entity = this.get(box.getId());
        if (entity != null) {
            entity.setNom(box.getNom());
            entity.setIsActif(box.getIsActif());
            entity.setDateModification(new Date());
            entity.setUserModification(box.getIdSession() != null ? utilisateurSessionService.getByIdSession(box.getIdSession()).getUtilisateur().getId() : null);
            entity = repository.save(entity);

            PrixBox prixUnitaire = prixBoxService.getLastPrixUnitaire(entity.getId().toString());
            PrixBox prixForfaitaire = prixBoxService.getLastPrixForfaitaire(entity.getId().toString());

            if (prixUnitaire.getMontant() != new Long(box.getPrixUnitaire())) {
                prixUnitaire.setDateFin(new Date());
                prixUnitaire.setUserModification(box.getIdSession() != null ? utilisateurSessionService.getByIdSession(box.getIdSession()).getUtilisateur().getId() : null);
                prixUnitaire.setDateModification(new Date());
                prixBoxService.save(prixUnitaire);
                PrixBox prixUnitaireNouveau = PrixBox.builder()
                        .dateDebut(new Date())
                        .idBox(box.getId().toString())
                        .montant(new Long(box.getPrixUnitaire()))
                        .typePrix("U")
                        .dateCreation(new Date())
                        .userCreation(box.getIdSession() != null ? utilisateurSessionService.getByIdSession(box.getIdSession()).getUtilisateur().getId() : null)
                        .build();
                prixBoxService.save(prixUnitaireNouveau);
            }

            if (prixForfaitaire.getMontant() != new Long(box.getPrixForfaitaire())) {
                prixForfaitaire.setDateFin(new Date());
                prixForfaitaire.setDateModification(new Date());
                prixForfaitaire.setUserModification(box.getIdSession() != null ? utilisateurSessionService.getByIdSession(box.getIdSession()).getUtilisateur().getId() : null);
                prixBoxService.save(prixForfaitaire);
                PrixBox prixForfaitaireNouveau = PrixBox.builder()
                        .dateDebut(new Date())
                        .idBox(box.getId().toString())
                        .montant(new Long(box.getPrixForfaitaire()))
                        .typePrix("F")
                        .dateCreation(new Date())
                        .userCreation(box.getIdSession() != null ? utilisateurSessionService.getByIdSession(box.getIdSession()).getUtilisateur().getId() : null)
                        .build();
                prixBoxService.save(prixForfaitaireNouveau);
            }

        } else {
            throw new CustomException("A", "Box introuvable");
        }
        return entity;
    }

    @Override
    public Box ajouterBox(String idEcurie) {
        Ecurie ecurie = ecurieService.get(new Long(idEcurie));
        if (ecurie != null) {
            Box last = repository.findFirstByEcurieIdOrderByOrdreDesc(ecurie.getId());
            PrixBox lastPrixUnitaire = prixBoxService.getLastPrixUnitaire(last.getId().toString());
            PrixBox lastPrixForfaitaire = prixBoxService.getLastPrixForfaitaire(last.getId().toString());
            Box box = Box.builder()
                    .ordre(last.getOrdre() + 1)
                    .ecurie(ecurie)
                    .isActif(true)
                    .nom(ecurie.getNom().substring(ecurie.getNom().length()-1) + (new Long(last.getOrdre()) + 1))
                    .dateCreation(new Date())
                    .build();
            box = repository.save(box);

            PrixBox prixUnitaire = PrixBox.builder()
                    .idBox(box.getId().toString())
                    .dateDebut(new Date())
                    .montant(lastPrixUnitaire.getMontant())
                    .typePrix("U")
                    .dateCreation(new Date())
                    .userCreation(box.getIdSession() != null ? utilisateurSessionService.getByIdSession(box.getIdSession()).getUtilisateur().getId() : null)
                    .build();
            prixBoxService.save(prixUnitaire);

            PrixBox prixForfaitaire = PrixBox.builder()
                    .idBox(box.getId().toString())
                    .dateDebut(new Date())
                    .montant(lastPrixForfaitaire.getMontant())
                    .typePrix("F")
                    .dateCreation(new Date())
                    .userCreation(box.getIdSession() != null ? utilisateurSessionService.getByIdSession(box.getIdSession()).getUtilisateur().getId() : null)
                    .build();
            prixBoxService.save(prixForfaitaire);
            return box;
        } else {
            throw new CustomException("A", "Chambre non trouvée");
        }
    }
}
