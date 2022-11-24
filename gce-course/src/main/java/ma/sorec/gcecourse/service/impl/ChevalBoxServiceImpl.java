package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.ChevalBox;
import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.repository.ChevalBoxRepository;
import ma.sorec.gcecourse.repository.mohr.TranspondeurChevalMRepository;
import ma.sorec.gcecourse.service.ChevalBoxService;
import ma.sorec.gcecourse.service.ReservationService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ChevalBoxServiceImpl implements ChevalBoxService {

    @Autowired
    private ChevalBoxRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    TranspondeurChevalMRepository transpondeurChevalMRepository;

    @Override
    public List<ChevalBox> listAll() {
        return repository.findAll();
    }

    @Override
    public ChevalBox save(ChevalBox chevalBox) {

        return repository.save(chevalBox);
    }

    @Override
    public ChevalBox get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        ChevalBox entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public Long getNombreJoursReservation(Long id) {

        Reservation reservation = reservationService.getByChevalBoxId(id);

        return SharedUtil.getDifferenceDays(reservation.getDateDebut(), reservation.getDateFin());
    }

    @Override
    public ChevalBox getChevalBoxByReservationId(Long idReservation) {
        ChevalBox chevalBox = repository.findFirstByReservationId(idReservation);
        if (chevalBox.getCheval() != null) {
            chevalBox.getCheval().setTranspondeur(transpondeurChevalMRepository.findFirstByIdChevalContainingIgnoreCaseAndDateFinIsNull(chevalBox.getCheval().getId().toString()).getNumeroTranspondeur());
        }
        return chevalBox;
    }

    @Override
    public List<ChevalBox> getAllChevalBoxsByReservationId(Long idReservation) {
        return repository.findAllByReservationId(idReservation);
    }

    @Override
    public List<ChevalBox> getChevalBoxByBoxId(Long idBox) {
        return repository.findAllByBoxId(idBox);
    }

    @Override
    public List<ChevalBox> getChevalBoxByChevalId(Long idCheval) {
        return repository.findAllByChevalId(idCheval);
    }
}
