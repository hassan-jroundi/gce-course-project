package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.data.StatutReservation;
import ma.sorec.gcecourse.repository.ReservationRepository;
import ma.sorec.gcecourse.repository.StatutReservationRepository;
import ma.sorec.gcecourse.service.ReservationService;
import ma.sorec.gcecourse.service.StatutReservationService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StatutReservationServiceImpl implements StatutReservationService {

    @Autowired
    private StatutReservationRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<StatutReservation> listAll() {
        return repository.findAll();
    }

    @Override
    public StatutReservation save(StatutReservation statutReservation) {

        StatutReservation entity = repository.save(statutReservation);

        return entity;
    }

    @Override
    public StatutReservation get(String id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(String id) {

        StatutReservation entity = this.get(id);

        repository.delete(entity);
    }
}
