package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.StatutDetailReservation;
import ma.sorec.gcecourse.repository.StatutDetailReservationRepository;
import ma.sorec.gcecourse.service.StatutDetailReservationService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StatutDetailReservationServiceImpl implements StatutDetailReservationService {

    @Autowired
    private StatutDetailReservationRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<StatutDetailReservation> listAll() {
        return repository.findAll();
    }

    @Override
    public StatutDetailReservation save(StatutDetailReservation statutDetailReservation) {

        StatutDetailReservation entity = repository.save(statutDetailReservation);

        return entity;
    }

    @Override
    public StatutDetailReservation get(String id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(String id) {

        StatutDetailReservation entity = this.get(id);

        repository.delete(entity);
    }
}
