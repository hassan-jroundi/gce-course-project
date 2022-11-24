package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.Utils.TrippleDes;
import ma.sorec.gcecourse.data.*;
import ma.sorec.gcecourse.exceptions.CustomException;
import ma.sorec.gcecourse.repository.UtilisateurRepository;
import ma.sorec.gcecourse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    private UtilisateurRepository repository;

    @Autowired
    private ProfilService profilService;

    @Autowired
    private UtilisateurProfilService utilisateurProfilService;

    @Autowired
    private UtilisateurSiteService utilisateurSiteService;

    @Autowired
    private UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private ProfilFonctionnaliteService profilFonctionnaliteService;

    @Autowired
    private FactureService factureService;

    @Autowired
    private SiteService siteService;

    @Override
    public List<Utilisateur> listAll() {
        return repository.findAll();
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {

        return repository.save(utilisateur);
    }

    @Override
    public Utilisateur get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        Utilisateur entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public List<Utilisateur> chargerListeUtilisateurs() {

        List<Utilisateur> utilisateurs = repository.findAll();
        for (Utilisateur utilisateur : utilisateurs) {
            utilisateur = ajouterUtilisateurSiteEtProfilEtSession(utilisateur.getId());
        }
        return utilisateurs;
    }

    @Override
    public Utilisateur creerUtilisateur(Utilisateur utilisateur) throws Exception {
        Utilisateur operateur = utilisateurSessionService.getByIdSession(utilisateur.getIdSession()).getUtilisateur();
        Utilisateur existant = repository.findFirstByLoginIgnoreCase(utilisateur.getLogin());
        if (existant != null) {
            throw new CustomException("A", "Login déjà existant");
        } else {
            Utilisateur entity = Utilisateur.builder()
                    .login(utilisateur.getLogin())
                    .codeEtat("A")
                    .motDePasse(TrippleDes.encrypt(utilisateur.getMotDePasse()))
                    .personne(utilisateur.getPersonne())
                    .codeTypeUtilisateur(utilisateur.getCodeTypeUtilisateur())
                    .dateCreation(new Date())
                    .userCreation(operateur.getId())
                    .build();
            entity = repository.save(entity);

            UtilisateurProfil utilisateurProfil = UtilisateurProfil.builder()
                    .profil(profilService.get(new Long(utilisateur.getIdProfil())))
                    .dateDebut(new Date())
                    .utilisateur(entity)
                    .dateCreation(new Date())
                    .userCreation(operateur.getId())
                    .build();
            utilisateurProfilService.save(utilisateurProfil);

            UtilisateurSite utilisateurSite = UtilisateurSite.builder()
                    .site(siteService.get(new Long(utilisateur.getIdSite())))
                    .utilisateur(entity)
                    .dateDebut(new Date())
                    .dateCreation(new Date())
                    .userCreation(operateur.getId())
                    .build();
            utilisateurSiteService.save(utilisateurSite);

            return entity;

        }
    }

    @Override
    public Utilisateur getByLogin(String login) {
        return repository.findFirstByLoginIgnoreCase(login);
    }

    @Override
    public Utilisateur ajouterUtilisateurSiteEtProfilEtSession(Long id) {

        Utilisateur utilisateur = repository.getById(id);

        UtilisateurSite utilisateurSite = utilisateurSiteService.getByUtilisateurId(utilisateur.getId());
        utilisateur.setSite(utilisateurSite.getSite());

        UtilisateurProfil utilisateurProfil = utilisateurProfilService.getByUtilisateurId(utilisateur.getId());
        utilisateur.setProfil(utilisateurProfil.getProfil());

        UtilisateurSession utilisateurSession = utilisateurSessionService.getDernierByUtilisateurId(utilisateur.getId());
        if (utilisateurSession != null) {
            if (utilisateurSession.getIdSession() != null) {
                utilisateur.setIdSession(utilisateurSession.getIdSession());
            }
        }

        return utilisateur;
    }

    @Override
    public void supprimerUtilisateur(Long id) {

        Utilisateur utilisateur = repository.getById(id);

        List<Facture> factures = factureService.getAllByIdOperateur(utilisateur.getId().toString());
        if (!(factures.isEmpty())) {
            throw new CustomException("A", "Il n'est pas possible de supprimer un utilisateur ayant déjà effectué des actions.");
        } else {
            List<UtilisateurSession> utilisateurSessions = utilisateurSessionService.getAllByUtilisateurId(utilisateur.getId());
            for (UtilisateurSession utilisateurSession : utilisateurSessions) {
                utilisateurSessionService.delete(utilisateurSession.getId());
            }

            List<UtilisateurProfil> utilisateurProfils = utilisateurProfilService.getAllByUtilisateurId(utilisateur.getId());
            for (UtilisateurProfil utilisateurProfil : utilisateurProfils) {
                utilisateurProfilService.delete(utilisateurProfil.getId());
            }

            List<UtilisateurSite> utilisateurSites = utilisateurSiteService.getAllByUtilisateurId(utilisateur.getId());
            for (UtilisateurSite utilisateurSite : utilisateurSites) {
                utilisateurSiteService.delete(utilisateurSite.getId());
            }

            repository.delete(utilisateur);
        }
    }

    @Override
    public Utilisateur modifierUtilisateur(Utilisateur utilisateur) {

        Utilisateur operateur = utilisateurSessionService.getByIdSession(utilisateur.getIdSession()).getUtilisateur();
        Long idSite = new Long(utilisateur.getIdSite());
        Long idProfil = new Long(utilisateur.getIdProfil());

        Utilisateur entity = repository.getById(utilisateur.getId());

        entity.setLogin(utilisateur.getLogin());
        entity.setCodeEtat(utilisateur.getCodeEtat());
        entity.setUserModification(operateur.getId());
        entity.setDateModification(new Date());

        entity = repository.save(entity);

        UtilisateurProfil utilisateurProfilAncien = utilisateurProfilService.getByUtilisateurId(entity.getId());
        if (!(utilisateurProfilAncien.getProfil().getId().equals(idProfil))) {
            utilisateurProfilAncien.setDateFin(new Date());
            utilisateurProfilAncien.setDateModification(new Date());
            utilisateurProfilAncien.setUserModification(operateur.getId());
            utilisateurProfilService.save(utilisateurProfilAncien);
            UtilisateurProfil utilisateurProfilNouveau = UtilisateurProfil.builder()
                    .profil(profilService.get(idProfil))
                    .utilisateur(entity)
                    .dateDebut(new Date())
                    .dateCreation(new Date())
                    .userCreation(operateur.getId())
                    .build();
            utilisateurProfilService.save(utilisateurProfilNouveau);
        }

        UtilisateurSite utilisateurSiteAncien = utilisateurSiteService.getByUtilisateurId(entity.getId());
        if (!(utilisateurSiteAncien.getSite().getId().equals(idSite))) {
            utilisateurSiteAncien.setDateFin(new Date());
            utilisateurSiteAncien.setDateModification(new Date());
            utilisateurSiteAncien.setUserModification(operateur.getId());
            utilisateurSiteService.save(utilisateurSiteAncien);
            UtilisateurSite utilisateurSiteNouveau = UtilisateurSite.builder()
                    .site(siteService.get(idSite))
                    .utilisateur(entity)
                    .dateDebut(new Date())
                    .dateCreation(new Date())
                    .userCreation(operateur.getId())
                    .build();
            utilisateurSiteService.save(utilisateurSiteNouveau);
        }

        return entity;
    }

    @Override
    public void changerMotDePasse(Long id, String motDePasse) throws Exception {
        Utilisateur utilisateur = repository.getById(id);
        utilisateur.setMotDePasse(TrippleDes.encrypt(motDePasse));
        utilisateur.setDateModification(new Date());
        utilisateur.setUserModification(utilisateurSessionService.getByIdSession(utilisateur.getIdSession()).getUtilisateur().getId());

        repository.save(utilisateur);
    }
}
