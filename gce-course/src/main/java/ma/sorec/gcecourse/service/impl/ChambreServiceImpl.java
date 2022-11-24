package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.*;
import ma.sorec.gcecourse.exceptions.CustomException;
import ma.sorec.gcecourse.repository.ChambreRepository;
import ma.sorec.gcecourse.repository.LitRepository;
import ma.sorec.gcecourse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ChambreServiceImpl implements ChambreService {

    @Autowired
    private ChambreRepository repository;

    @Autowired
    private LitRepository litRepository;

    @Autowired
    private LitService litService;

    @Autowired
    private PrixLitService prixLitService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    NombreLitsHistoriqueService nombreLitsHistoriqueService;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<Chambre> listAll() {
        return repository.findAll();
    }

    @Override
    public Chambre save(Chambre chambre) {

        Long nombreLits = chambre.getNombreLits();
        String chambreNom = chambre.getImmeuble().getNom() + "-" + chambre.getNom();

        chambre.setNom(chambreNom);
        chambre.setDateCreation(new Date());
        chambre.setUserCreation(chambre.getIdSession() != null ? utilisateurSessionService.getByIdSession(chambre.getIdSession()).getUtilisateur().getId() : null);
        Chambre entity = repository.save(chambre);

        NombreLitsHistorique nombreLitsHistorique = NombreLitsHistorique.builder()
                .idChambre(entity.getId().toString())
                .nombre(chambre.getNombreLits())
                .dateDebut(new Date())
                .dateCreation(new Date())
                .userCreation(chambre.getIdSession() != null ? utilisateurSessionService.getByIdSession(chambre.getIdSession()).getUtilisateur().getId() : null)
                .build();

        nombreLitsHistoriqueService.save(nombreLitsHistorique);

        PrixLit prixUnitaireLit = PrixLit.builder()
                .idChambre(entity.getId().toString())
                .dateDebut(new Date())
                .montant(new Long(chambre.getPrixUnitaire()))
                .typePrix("U")
                .dateCreation(new Date())
                .userCreation(chambre.getIdSession() != null ? utilisateurSessionService.getByIdSession(chambre.getIdSession()).getUtilisateur().getId() : null)
                .build();

        prixLitService.save(prixUnitaireLit);

        PrixLit prixForfaitaireLit = PrixLit.builder()
                .idChambre(entity.getId().toString())
                .dateDebut(new Date())
                .montant(new Long(chambre.getPrixForfaitaire()))
                .typePrix("F")
                .dateCreation(new Date())
                .userModification(chambre.getIdSession() != null ? utilisateurSessionService.getByIdSession(chambre.getIdSession()).getUtilisateur().getId() : null)
                .build();

        prixLitService.save(prixForfaitaireLit);

        for (int i = 0 ; i < nombreLits ; i++) {
            Lit lit = Lit.builder()
                    .chambre(entity)
                    .isActif(true)
                    .nom(chambreNom + "-" + "LIT" + (i+1))
                    .ordre(new Long (i+1))
                    .dateCreation(new Date())
                    .userCreation(chambre.getIdSession() != null ? utilisateurSessionService.getByIdSession(chambre.getIdSession()).getUtilisateur().getId() : null)
                    .idSession(chambre.getIdSession())
                    .build();
            litService.save(lit);
        }

        return entity;
    }

    @Override
    public Chambre update(Chambre chambre) {
        Chambre entity = repository.getById(chambre.getId());
        if (entity != null) {
            entity.setNom(chambre.getNom());
            entity.setIsActif(chambre.getIsActif());
            entity.setDateModification(new Date());
            entity.setUserModification(chambre.getIdSession() != null ? utilisateurSessionService.getByIdSession(chambre.getIdSession()).getUtilisateur().getId() : null);
            entity = repository.save(entity);

            PrixLit prixUnitaire = prixLitService.getLasPrixLit(entity.getId().toString(), "U");
            PrixLit prixForfaitaire = prixLitService.getLasPrixLit(entity.getId().toString(), "F");

            if (prixUnitaire.getMontant() != new Long(chambre.getPrixUnitaire())) {
                prixUnitaire.setDateFin(new Date());
                prixUnitaire.setDateModification(new Date());
                prixUnitaire.setUserModification(chambre.getIdSession() != null ? utilisateurSessionService.getByIdSession(chambre.getIdSession()).getUtilisateur().getId() : null);
                prixLitService.save(prixUnitaire);

                PrixLit prixUnitaireNouveau = PrixLit.builder()
                        .dateDebut(new Date())
                        .idChambre(chambre.getId().toString())
                        .montant(new Long(chambre.getPrixUnitaire()))
                        .typePrix("U")
                        .dateCreation(new Date())
                        .userCreation(chambre.getIdSession() != null ? utilisateurSessionService.getByIdSession(chambre.getIdSession()).getUtilisateur().getId() : null)
                        .build();
                prixLitService.save(prixUnitaireNouveau);
            }

            if (prixForfaitaire.getMontant() != new Long(chambre.getPrixForfaitaire())) {
                prixForfaitaire.setDateFin(new Date());
                prixForfaitaire.setDateModification(new Date());
                prixForfaitaire.setUserModification(chambre.getIdSession() != null ? utilisateurSessionService.getByIdSession(chambre.getIdSession()).getUtilisateur().getId() : null);
                prixLitService.save(prixForfaitaire);

                PrixLit prixForfaitaireNouveau = PrixLit.builder()
                        .dateDebut(new Date())
                        .idChambre(chambre.getId().toString())
                        .montant(new Long(chambre.getPrixForfaitaire()))
                        .typePrix("F")
                        .dateModification(new Date())
                        .userModification(chambre.getIdSession() != null ? utilisateurSessionService.getByIdSession(chambre.getIdSession()).getUtilisateur().getId() : null)
                        .build();
                prixLitService.save(prixForfaitaireNouveau);
            }

            return entity;
        } else {
            throw new CustomException("A", "Chambre non trouvée.");
        }
    }

    @Override
    public Chambre get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        Chambre entity = this.get(id);
        List<Reservation> reservations = reservationService.getAllByPersonneLitLitChambreId(entity.getId());
        List<Lit> lits = litRepository.findAllByChambreId(id);
        if (!(reservations.isEmpty())) {
            throw new CustomException("C", "Cette chambre est liée à des réservations et ne peut être supprimée.");
        } else if (!(lits.isEmpty())) {
            throw new CustomException("C", "Cette chambre a des lits disponibles et ne peut être supprimée.");
        } else {
            repository.delete(entity);
        }
    }

    @Override
    public List<Chambre> getAllByImmeubleId(Long idImmeuble) {
        return repository.findAllByImmeubleIdOrderByNom(idImmeuble);
    }

    @Override
    public List<Chambre> getAllActifChambre(Long idImmeuble) {
        return repository.findAllByImmeubleIdAndIsActifIsTrueOrderByNom(idImmeuble);
    }
}
