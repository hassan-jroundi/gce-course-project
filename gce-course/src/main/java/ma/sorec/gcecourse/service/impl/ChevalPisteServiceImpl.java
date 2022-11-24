package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.Cheval;
import ma.sorec.gcecourse.data.ChevalPiste;
import ma.sorec.gcecourse.data.Piste;
import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.exceptions.CustomException;
import ma.sorec.gcecourse.repository.ChevalPisteRepository;
import ma.sorec.gcecourse.service.ChevalPisteService;
import ma.sorec.gcecourse.service.ChevalService;
import ma.sorec.gcecourse.service.PisteService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ChevalPisteServiceImpl implements ChevalPisteService {

    @Autowired
    private ChevalPisteRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private ChevalPisteService service;

    @Autowired
    private PisteService pisteService;

    @Autowired
    private ChevalService chevalService;

    @Override
    public List<ChevalPiste> listAll() {
        return repository.findAll();
    }

    @Override
    public ChevalPiste save(ChevalPiste chevalPiste) {

        return repository.save(chevalPiste);
    }

    @Override
    public ChevalPiste get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        ChevalPiste entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public Long getNombreHeuresReservation(Long id) {

        ChevalPiste entity = this.get(id);

        return SharedUtil.getDifferenceHours(entity.getDateDebut(), entity.getDateFin());
    }

    @Override
    public ChevalPiste getChevalPisteByReservationId(Long id) {
        return repository.findFirstByReservationId(id);
    }

    @Override
    public List<ChevalPiste> getChevalPisteByHour(String heure) {
        List<ChevalPiste> chevalPistes = this.listAll();
        List<ChevalPiste> entities = new ArrayList<>();
        for (ChevalPiste item : chevalPistes) {
            if (new SimpleDateFormat("H").format(item.getDateDebut()).equals(heure)) {
                entities.add(item);
            }
        }
        return entities;
    }

    @Override
    public List<ChevalPiste> getChevalPisteByChevalId(Long id) {
        return repository.findAllByChevalId(id);
    }

    @Override
    public ChevalPiste creerChevalPiste(Reservation reservation, Long idPiste, Long idCheval, Date dateDebut, Date dateFin, String typePrix) {
        Cheval cheval = chevalService.get(idCheval);
        Piste piste = pisteService.get(idPiste);
        List<ChevalPiste> entities = service.getChevalPisteByChevalId(cheval.getId());
        for (ChevalPiste item : entities) {
            if (!(dateDebut.before(item.getDateDebut()) && dateFin.before(item.getDateDebut()) || dateDebut.after(item.getDateFin()))) {
                throw new CustomException("O", "Le cheval a déjà une réservation en cours selon l'horaire choisi");
            }
        }
        ChevalPiste chevalPiste = ChevalPiste.builder()
                .cheval(cheval)
                .dateDebut(dateDebut)
                .dateFin(dateFin)
                .piste(piste)
                .reservation(reservation)
                .build();
        chevalPiste = service.save(chevalPiste);

        return chevalPiste;
    }

    @Override
    public List<ChevalPiste> getAllChevalPistesByReservationId(Long idReservation) {
        return repository.findAllByReservationId(idReservation);
    }
}
