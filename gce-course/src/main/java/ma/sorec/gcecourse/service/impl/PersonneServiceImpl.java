package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.Utils.PersonneMapperUtil;
import ma.sorec.gcecourse.data.Personne;
import ma.sorec.gcecourse.data.RelationLAD;
import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.data.mohr.PersonneM;
import ma.sorec.gcecourse.exceptions.CustomException;
import ma.sorec.gcecourse.repository.PersonneRepository;
import ma.sorec.gcecourse.repository.mohr.PersonneMRepository;
import ma.sorec.gcecourse.service.PersonneService;
import ma.sorec.gcecourse.service.RelationLADService;
import ma.sorec.gcecourse.service.ReservationService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PersonneServiceImpl implements PersonneService {

    @Autowired
    private PersonneRepository repository;

    @Autowired
    private PersonneMRepository personneMRepository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RelationLADService relationLADService;

    @Override
    public List<Personne> listAll() {
        return repository.findAll();
    }

    @Override
    public Personne save(Personne personne) {
        Personne entity = this.findFirstByNumeroPieceIdentite(personne.getNumeroPieceIdentite());
        if (entity == null) {
            personne.setId(repository.getNextValSeqGceId().toString());
            personne.setNom(personne.getNom().toUpperCase());
            personne.setPrenom(personne.getPrenom().toUpperCase());
            personne.setAdresse1(personne.getAdresse1().toUpperCase());
            personne.setNumeroPieceIdentite(personne.getNumeroPieceIdentite().toUpperCase());
            personne.setDateCreation(new Date());
            personne.setUserCreation(utilisateurSessionService.getByIdSession(personne.getIdSession()).getUtilisateur().getId());
            entity = repository.save(personne);
            if (personne.getCodeTypePersonne().equals("L")) {
                RelationLAD relationLAD = RelationLAD.builder()
                        .idLAD(entity.getId())
                        .idEmployeur(personne.getIdEmployeur())
                        .dateDebutContrat(new Date())
                        .dateCreation(new Date())
                        .userCreation(utilisateurSessionService.getByIdSession(personne.getIdSession()).getUtilisateur().getId())
                        .build();
                relationLADService.save(relationLAD);
            }
            return entity;
        } else {
            throw new CustomException("E", "Personne déjà existante dans la base.");
        }
    }

    @Override
    public Personne update(Personne personne) {

        Personne entity = this.get(personne.getId());
        if (entity != null) {
            personne.setNom(personne.getNom().toUpperCase());
            personne.setPrenom(personne.getPrenom().toUpperCase());
            personne.setAdresse1(personne.getAdresse1() != null ? personne.getAdresse1().toUpperCase() : "");
            personne.setNumeroPieceIdentite(personne.getNumeroPieceIdentite() != null ? personne.getNumeroPieceIdentite().toUpperCase() : "");
            personne.setDateModification(new Date());
            personne.setUserModification(utilisateurSessionService.getByIdSession(personne.getIdSession()).getUtilisateur().getId());
            if (personne.getCodeTypePersonne().equals("L")) {
                Personne employeur = this.findLADEmployeur(entity.getId());
                if (!(employeur.getId().equals(personne.getIdEmployeur()))) {
                    RelationLAD ancienneRelationLAD = relationLADService.getByPersonneIdAndDateFinContratIsNull(entity.getId());
                    ancienneRelationLAD.setDateFinContrat(new Date());
                    relationLADService.save(ancienneRelationLAD);
                    RelationLAD nouvelleRelationLAD = RelationLAD.builder()
                            .idLAD(entity.getId())
                            .idEmployeur(personne.getIdEmployeur())
                            .dateDebutContrat(new Date())
                            .build();
                    relationLADService.save(nouvelleRelationLAD);
                }
            }
            entity = repository.save(personne);
            return entity;
        } else {
            throw new CustomException("E", "Personne non trouvée dans la base de données");
        }
    }

    @Override
    public Personne get(String id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(String id) {

        Personne entity = this.get(id);
        List<Reservation> reservations = reservationService.getAllByPersonneId(entity.getId());
        List<Reservation> reservationsFacture = reservationService.getAllByPersonneAFacturerId(entity.getId());
        if (!(reservations.isEmpty())) {
            throw new CustomException("C", "Cette personne a des réservations et ne peut être supprimée");
        } else if (!(reservationsFacture.isEmpty())) {
            throw new CustomException("F", "Cette personne a déjà été facturé et ne peut être supprimée");
        } else {
            repository.delete(entity);
        }
    }

    @Override
    public List<Personne> findAllByNomAndPrenomAndNumeroPieceIdentiteAndCodeNaturePersonne(String nom, String prenom, String cin, String codeNaturePersonne) {
        return repository.findAllByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndNumeroPieceIdentiteContainingIgnoreCaseAndCodeNaturePersonneContainingIgnoreCase(nom, prenom, cin, codeNaturePersonne);
    }

    @Override
    public List<Personne> findAllByNomAndPrenomAndNumeroPieceIdentiteAndDesignationAndRaisonSociale(String nom, String prenom, String cin, String designation, String raisonSociale) {
        return repository.findAllByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndNumeroPieceIdentiteContainingIgnoreCaseAndDesignationContainingIgnoreCaseAndRaisonSocialeContainingIgnoreCase(nom, prenom, cin, designation, raisonSociale);
    }

    @Override
    public List<Personne> findAllByCriteriaBis(String nom, String prenom, String cin, String designation, String raisonSociale) {

        List<Personne> entities = new ArrayList<>();

        if (nom.length() > 0 || prenom.length() > 0 || cin.length() > 0) {
            if (nom.length() > 0 && prenom.length() == 0 && cin.length() == 0) {
                entities = repository.findAllByNomContainingIgnoreCase(nom);
            } else if (nom.length() == 0 && prenom.length() > 0 && cin.length() == 0) {
                entities = repository.findAllByPrenomContainingIgnoreCase(prenom);
            } else if (nom.length() == 0 && prenom.length() == 0 && cin.length() > 0) {
                entities = repository.findAllByNumeroPieceIdentiteContainingIgnoreCase(cin);
            } else {
                entities = repository.findAllByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNumeroPieceIdentiteContainingIgnoreCase(nom, prenom, cin);
            }
        } else if (designation.length() > 0) {
            entities = repository.findAllByDesignationContainingIgnoreCase(designation);
        } else if (raisonSociale.length() > 0) {
            entities = repository.findAllByRaisonSocialeContainingIgnoreCase(raisonSociale);
        }
        return entities;
    }

    @Override
    public Personne findFirstByNumeroPieceIdentite(String numeroPieceIdentite) {
        return repository.findFirstByNumeroPieceIdentiteIgnoreCase(numeroPieceIdentite);
    }

    @Override
    public Personne findLADEmployeur(String idLAD) {

        RelationLAD relationLAD = relationLADService.getByPersonneIdAndDateFinContratIsNull(idLAD);
        return PersonneMapperUtil.fromPersonneMEntityToPersonneEntity(personneMRepository.getById(relationLAD.getIdEmployeur()));
    }

    @Override
    public List<Personne> findAllByCriteria(String nom, String prenom, String cin, String designation, String raisonSociale) {

        List<Personne> entities = new ArrayList<>();
        List<PersonneM> personnesFromMOHR = new ArrayList<>();
        List<Personne> personnesFromGCE = new ArrayList<>();

        if (nom.length() > 0 || prenom.length() > 0 || cin.length() > 0) {
            if (nom.length() > 0 && prenom.length() == 0 && cin.length() == 0) {
                personnesFromMOHR = personneMRepository.findAllByNomContainingIgnoreCase(nom);
                personnesFromGCE = repository.findAllByNomContainingIgnoreCase(nom);
                if (!(personnesFromGCE.isEmpty())) {
                    for (Personne item : personnesFromGCE) {
                        item.setDatabaseSource("G");
                        entities.add(item);
                    }
                }
                if (!(personnesFromMOHR.isEmpty())) {
                    for (PersonneM item : personnesFromMOHR) {
                        Personne personne = Personne.builder()
                                .codeNaturePersonne(item.getCodeNaturePersonne())
                                .codeTitrePersonne(item.getCodeTitrePersonne())
                                .adresse1(item.getAdresse1())
                                .adresse2(item.getAdresse2())
                                .categoriePersMora(item.getCategoriePersMora())
                                .codeCategorie(item.getCodeCategorie())
                                .codePaysOrigine(item.getCodePaysOrigine())
                                .codeSexe(item.getCodeSexe())
                                .codeTypePieceIdentite(item.getCodeTypePieceIdentite())
                                .codeVille1(item.getCodeVille1())
                                .dateCreation(item.getDateCreation())
                                .dateNaissance(item.getDateNaissance())
                                .designation(item.getDesignation())
                                .email(item.getEmail())
                                .etrangere(item.getEtrangere())
                                .id(item.getId())
                                .idBanque(item.getIdBanque())
                                .nom(item.getNom())
                                .nomGerant(item.getNomGerant())
                                .numeroPieceIdentite(item.getNumeroPieceIdentite())
                                .numeroTelephone1(item.getNumeroTelephone1())
                                .numeroTelephone2(item.getNumeroTelephone2())
                                .prenom(item.getPrenom())
                                .raisonSociale(item.getRaisonSociale())
                                .rib(item.getRib())
                                .databaseSource("M")
                                .reservations(reservationService.getAllByPersonneAFacturerId(item.getId()))
                                .build();
                        entities.add(personne);
                    }
                }
            } else if (nom.length() == 0 && prenom.length() > 0 && cin.length() == 0) {
                personnesFromMOHR = personneMRepository.findAllByPrenomContainingIgnoreCase(prenom);
                personnesFromGCE = repository.findAllByPrenomContainingIgnoreCase(prenom);
                if (!(personnesFromGCE.isEmpty())) {
                    for (Personne item : personnesFromGCE) {
                        item.setDatabaseSource("G");
                        entities.add(item);
                    }
                }
                if (!(personnesFromMOHR.isEmpty())) {
                    for (PersonneM item : personnesFromMOHR) {
                        Personne personne = Personne.builder()
                                .codeNaturePersonne(item.getCodeNaturePersonne())
                                .codeTitrePersonne(item.getCodeTitrePersonne())
                                .adresse1(item.getAdresse1())
                                .adresse2(item.getAdresse2())
                                .categoriePersMora(item.getCategoriePersMora())
                                .codeCategorie(item.getCodeCategorie())
                                .codePaysOrigine(item.getCodePaysOrigine())
                                .codeSexe(item.getCodeSexe())
                                .codeTypePieceIdentite(item.getCodeTypePieceIdentite())
                                .codeVille1(item.getCodeVille1())
                                .dateCreation(item.getDateCreation())
                                .dateNaissance(item.getDateNaissance())
                                .designation(item.getDesignation())
                                .email(item.getEmail())
                                .etrangere(item.getEtrangere())
                                .id(item.getId())
                                .idBanque(item.getIdBanque())
                                .nom(item.getNom())
                                .nomGerant(item.getNomGerant())
                                .numeroPieceIdentite(item.getNumeroPieceIdentite())
                                .numeroTelephone1(item.getNumeroTelephone1())
                                .numeroTelephone2(item.getNumeroTelephone2())
                                .prenom(item.getPrenom())
                                .raisonSociale(item.getRaisonSociale())
                                .rib(item.getRib())
                                .databaseSource("M")
                                .reservations(reservationService.getAllByPersonneAFacturerId(item.getId()))
                                .build();
                        entities.add(personne);
                    }
                }
            } else if (nom.length() == 0 && prenom.length() == 0 && cin.length() > 0) {
                personnesFromMOHR = personneMRepository.findAllByNumeroPieceIdentiteContainingIgnoreCase(cin);
                personnesFromGCE = repository.findAllByNumeroPieceIdentiteContainingIgnoreCase(cin);
                if (!(personnesFromGCE.isEmpty())) {
                    for (Personne item : personnesFromGCE) {
                        item.setDatabaseSource("G");
                        entities.add(item);
                    }
                }
                if (!(personnesFromMOHR.isEmpty())) {
                    for (PersonneM item : personnesFromMOHR) {
                        Personne personne = Personne.builder()
                                .codeNaturePersonne(item.getCodeNaturePersonne())
                                .codeTitrePersonne(item.getCodeTitrePersonne())
                                .adresse1(item.getAdresse1())
                                .adresse2(item.getAdresse2())
                                .categoriePersMora(item.getCategoriePersMora())
                                .codeCategorie(item.getCodeCategorie())
                                .codePaysOrigine(item.getCodePaysOrigine())
                                .codeSexe(item.getCodeSexe())
                                .codeTypePieceIdentite(item.getCodeTypePieceIdentite())
                                .codeVille1(item.getCodeVille1())
                                .dateCreation(item.getDateCreation())
                                .dateNaissance(item.getDateNaissance())
                                .designation(item.getDesignation())
                                .email(item.getEmail())
                                .etrangere(item.getEtrangere())
                                .id(item.getId())
                                .idBanque(item.getIdBanque())
                                .nom(item.getNom())
                                .nomGerant(item.getNomGerant())
                                .numeroPieceIdentite(item.getNumeroPieceIdentite())
                                .numeroTelephone1(item.getNumeroTelephone1())
                                .numeroTelephone2(item.getNumeroTelephone2())
                                .prenom(item.getPrenom())
                                .raisonSociale(item.getRaisonSociale())
                                .rib(item.getRib())
                                .databaseSource("M")
                                .reservations(reservationService.getAllByPersonneAFacturerId(item.getId()))
                                .build();
                        entities.add(personne);
                    }
                }
            } else {
                personnesFromMOHR = personneMRepository.findAllByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNumeroPieceIdentiteContainingIgnoreCase(nom, prenom, cin);
                personnesFromGCE = repository.findAllByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNumeroPieceIdentiteContainingIgnoreCase(nom, prenom, cin);
                if (!(personnesFromGCE.isEmpty())) {
                    for (Personne item : personnesFromGCE) {
                        item.setDatabaseSource("G");
                        entities.add(item);
                    }
                }
                if (!(personnesFromMOHR.isEmpty())) {
                    for (PersonneM item : personnesFromMOHR) {
                        Personne personne = Personne.builder()
                                .codeNaturePersonne(item.getCodeNaturePersonne())
                                .codeTitrePersonne(item.getCodeTitrePersonne())
                                .adresse1(item.getAdresse1())
                                .adresse2(item.getAdresse2())
                                .categoriePersMora(item.getCategoriePersMora())
                                .codeCategorie(item.getCodeCategorie())
                                .codePaysOrigine(item.getCodePaysOrigine())
                                .codeSexe(item.getCodeSexe())
                                .codeTypePieceIdentite(item.getCodeTypePieceIdentite())
                                .codeVille1(item.getCodeVille1())
                                .dateCreation(item.getDateCreation())
                                .dateNaissance(item.getDateNaissance())
                                .designation(item.getDesignation())
                                .email(item.getEmail())
                                .etrangere(item.getEtrangere())
                                .id(item.getId())
                                .idBanque(item.getIdBanque())
                                .nom(item.getNom())
                                .nomGerant(item.getNomGerant())
                                .numeroPieceIdentite(item.getNumeroPieceIdentite())
                                .numeroTelephone1(item.getNumeroTelephone1())
                                .numeroTelephone2(item.getNumeroTelephone2())
                                .prenom(item.getPrenom())
                                .raisonSociale(item.getRaisonSociale())
                                .rib(item.getRib())
                                .databaseSource("M")
                                .reservations(reservationService.getAllByPersonneAFacturerId(item.getId()))
                                .build();
                        entities.add(personne);
                    }
                }
            }
        } else if (designation.length() > 0) {
            entities = PersonneMapperUtil.fromPersonneMEntitiesToPersonneEntities(personneMRepository.findAllByDesignationContainingIgnoreCase(designation));
        } else if (raisonSociale.length() > 0) {
            entities = PersonneMapperUtil.fromPersonneMEntitiesToPersonneEntities(personneMRepository.findAllByRaisonSocialeContainingIgnoreCase(raisonSociale));
        }
        return entities;
    }
}
