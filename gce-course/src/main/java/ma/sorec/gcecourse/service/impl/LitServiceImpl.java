package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Chambre;
import ma.sorec.gcecourse.data.Lit;
import ma.sorec.gcecourse.data.NombreLitsHistorique;
import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.exceptions.CustomException;
import ma.sorec.gcecourse.repository.ChambreRepository;
import ma.sorec.gcecourse.repository.LitRepository;
import ma.sorec.gcecourse.repository.NombreLitsHistoriqueRepository;
import ma.sorec.gcecourse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LitServiceImpl implements LitService {

    @Autowired
    private LitRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private ChambreService chambreService;

    @Autowired
    private NombreLitsHistoriqueRepository nombreLitsHistoriqueRepository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private PrixLitService prixLitService;

    @Override
    public List<Lit> listAll() {
        return repository.findAll();
    }

    @Override
    public Lit save(Lit lit) {
        lit.setDateCreation(new Date());
        lit.setUserCreation(lit.getIdSession() != null ? utilisateurSessionService.getByIdSession(lit.getIdSession()).getUtilisateur().getId() : null);
        return repository.save(lit);
    }

    @Override
    public Lit update(Lit lit) {
        Lit entity = repository.getById(lit.getId());
        if (entity != null) {
            lit.setDateModification(new Date());
            lit.setUserModification(lit.getIdSession() != null ? utilisateurSessionService.getByIdSession(lit.getIdSession()).getUtilisateur().getId() : null);
            entity = repository.save(lit);
            return entity;
        } else {
            throw new CustomException("A", "Lit non trouvé.");
        }
    }

    @Override
    public Lit get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id, String idSession) {

        Lit entity = this.get(id);
        List<Reservation> reservations = reservationService.getAllByPersonneLitLitId(entity.getId());

        if (!(reservations.isEmpty())) {
            throw new CustomException("C", "Ce lit est lié à des réservations et ne peut être supprimé");
        } else {
            Chambre chambre = chambreRepository.getById(entity.getChambre().getId());

            NombreLitsHistorique nombreLitsHistoriqueAncien = nombreLitsHistoriqueRepository.findFirstByIdChambreAndDateFinIsNull(chambre.getId().toString());
            nombreLitsHistoriqueAncien.setDateFin(new Date());
            nombreLitsHistoriqueAncien.setDateModification(new Date());
            nombreLitsHistoriqueAncien.setUserModification(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId());
            nombreLitsHistoriqueAncien = nombreLitsHistoriqueRepository.save(nombreLitsHistoriqueAncien);

            NombreLitsHistorique nombreLitsHistoriqueNouveau = new NombreLitsHistorique();
            nombreLitsHistoriqueNouveau.setIdChambre(chambre.getId().toString());
            nombreLitsHistoriqueNouveau.setDateDebut(new Date());
            nombreLitsHistoriqueNouveau.setNombre(nombreLitsHistoriqueAncien.getNombre() - 1);
            nombreLitsHistoriqueNouveau.setDateCreation(new Date());
            nombreLitsHistoriqueNouveau.setUserCreation(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId());
            nombreLitsHistoriqueRepository.save(nombreLitsHistoriqueNouveau);

            repository.delete(entity);
        }
    }

    @Override
    public List<Lit> getAllByChambreId(Long idChambre) {
        return repository.findAllByChambreId(idChambre);
    }

    @Override
    public List<Lit> getAllByImmeubleId(Long idImmeuble) {
        return repository.findAllByChambreImmeubleId(idImmeuble);
    }

    @Override
    public List<Lit> getAllByNomSite(String nomSite) {
        return repository.findAllByChambreImmeubleSiteNom(nomSite);
    }

    @Override
    public List<Lit> getAllActifLit(Long idChambre) {
        return repository.findAllByChambreIdAndIsActifIsTrueOrderByNom(idChambre);
    }

    @Override
    public Lit ajouterLit(String idChambre, String idSession) {
        Chambre chambre = chambreService.get(new Long(idChambre));
        if (chambre != null) {
            Lit last = repository.findFirstByChambreIdOrderByOrdreDesc(chambre.getId());
            Lit lit = Lit.builder()
                    .ordre(last.getOrdre() + 1)
                    .chambre(chambre)
                    .isActif(true)
                    .userCreation(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId())
                    .dateCreation(new Date())
                    .nom(chambre.getNom().substring(chambre.getNom().length()-1) + (new Long(last.getOrdre()) + 1)).build();
            lit = repository.save(lit);

            NombreLitsHistorique nombreLitsHistoriqueAncien = nombreLitsHistoriqueRepository.findFirstByIdChambreAndDateFinIsNull(chambre.getId().toString());
            nombreLitsHistoriqueAncien.setDateFin(new Date());
            nombreLitsHistoriqueAncien.setDateModification(new Date());
            nombreLitsHistoriqueAncien.setUserModification(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId());
            nombreLitsHistoriqueAncien = nombreLitsHistoriqueRepository.save(nombreLitsHistoriqueAncien);

            NombreLitsHistorique nombreLitsHistoriqueNouveau = new NombreLitsHistorique();
            nombreLitsHistoriqueNouveau.setIdChambre(chambre.getId().toString());
            nombreLitsHistoriqueNouveau.setDateDebut(new Date());
            nombreLitsHistoriqueNouveau.setNombre(nombreLitsHistoriqueAncien.getNombre() + 1);
            nombreLitsHistoriqueNouveau.setDateCreation(new Date());
            nombreLitsHistoriqueNouveau.setUserCreation(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId());
            nombreLitsHistoriqueRepository.save(nombreLitsHistoriqueNouveau);

            chambre.setUserModification(utilisateurSessionService.getByIdSession(idSession).getUtilisateur().getId());
            chambre.setDateModification(new Date());
            return lit;
        } else {
            throw new CustomException("A", "Chambre non trouvée");
        }
    }
}
