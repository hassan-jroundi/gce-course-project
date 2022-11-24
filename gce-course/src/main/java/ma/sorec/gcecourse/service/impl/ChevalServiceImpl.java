package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Cheval;
import ma.sorec.gcecourse.data.ChevalBox;
import ma.sorec.gcecourse.data.ChevalPiste;
import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.data.mohr.DetailDeclarationEffectif;
import ma.sorec.gcecourse.data.mohr.TranspondeurChevalM;
import ma.sorec.gcecourse.repository.ChevalRepository;
import ma.sorec.gcecourse.repository.mohr.DetailDeclarationEffectifRepository;
import ma.sorec.gcecourse.repository.mohr.TranspondeurChevalMRepository;
import ma.sorec.gcecourse.service.ChevalService;
import ma.sorec.gcecourse.service.ReservationService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ChevalServiceImpl implements ChevalService {

    @Autowired
    private ChevalRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private DetailDeclarationEffectifRepository detailDeclarationEffectifRepository;

    @Autowired
    private TranspondeurChevalMRepository transpondeurChevalMRepository;

    @Override
    public List<Cheval> listAll() {
        return repository.findAll();
    }

    @Override
    public Cheval get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Cheval> findAllByNumeroEsrimaAndNomCheval(String numeroEsrima, String nomCheval) {
        List<Cheval> entities = repository.findAllByNumeroEsrimaContainingAndNomContainingIgnoreCase(numeroEsrima, nomCheval);
        for (Cheval cheval : entities) {
            DetailDeclarationEffectif detailDeclarationEffectif = detailDeclarationEffectifRepository.findFirstByIdChevalOrderByDateCreationDesc(cheval.getId());
            if (detailDeclarationEffectif != null) {
                cheval.setStatut(detailDeclarationEffectif.getCodeStatutCheval());
            }
        }
        return entities;
    }

    @Override
    public List<Cheval> findAllByNumeroEsrimaAndNomChevalAndNumeroTranspondeur(String numeroEsrima, String nomCheval, String numeroTranspondeur) {
        List<Cheval> entities = new ArrayList<>();
        if (numeroTranspondeur.length() > 0 && numeroEsrima.length() == 0 && nomCheval.length() == 0) {
            List<TranspondeurChevalM> transpondeurChevalMList = transpondeurChevalMRepository.findAllByNumeroTranspondeurContainingIgnoreCaseAndDateFinIsNull(numeroTranspondeur);
            for (TranspondeurChevalM transpondeurChevalM : transpondeurChevalMList) {
                entities.add(this.get(new Long(transpondeurChevalM.getIdCheval())));
            }
        } else {
            entities = repository.findAllByNomContainingIgnoreCaseAndNumeroEsrimaContaining(nomCheval, numeroEsrima);
        }
        for (Cheval cheval : entities) {
            DetailDeclarationEffectif detailDeclarationEffectif = detailDeclarationEffectifRepository.findFirstByIdChevalOrderByDateCreationDesc(cheval.getId());
            if (detailDeclarationEffectif != null) {
                cheval.setStatut(detailDeclarationEffectif.getCodeStatutCheval());
            }
            TranspondeurChevalM transpondeurChevalM = transpondeurChevalMRepository.findFirstByIdChevalContainingIgnoreCaseAndDateFinIsNull(cheval.getId().toString());
            if (transpondeurChevalM != null) {
                cheval.setTranspondeur(transpondeurChevalM.getNumeroTranspondeur());
            }

        }
        return entities;
    }

    @Override
    public List<Cheval> findAllByNumeroEsrimaAndNumeroTranspondeur(String numeroEsrima, String numeroTranspondeur) {
        return repository.findAllByNumeroEsrimaContainingAndNumeroTranspondeurContaining(numeroEsrima, numeroTranspondeur);
    }

    @Override
    public List<Cheval> findAllByNumeroEsrima(String numeroEsrima) {
        return repository.findAllByNumeroEsrimaContainingIgnoreCase(numeroEsrima);
    }

    @Override
    public List<Cheval> findAllByNumeroTranspondeur(String numeroTranspondeur) {
        return repository.findAllByNumeroTranspondeurContainingIgnoreCase(numeroTranspondeur);
    }

    @Override
    public List<Cheval> findAllChevalAvecReservationEnCours() {
        List<Reservation> reservations = reservationService.getReservationsEnCours();
        List<Cheval> chevals = new ArrayList<>();
        for (Reservation item : reservations) {
            for (ChevalBox chevalBox : item.getChevalBoxs()) {
                if (!(chevals.contains(chevalBox.getCheval()))) {
                    chevals.add(chevalBox.getCheval());
                }
            }
            for (ChevalPiste chevalPiste : item.getChevalPistes()) {
                if (!(chevals.contains(chevalPiste.getCheval()))) {
                    chevals.add(chevalPiste.getCheval());
                }
            }
        }
        return chevals;
    }
}
