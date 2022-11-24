package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.Utils.SharedUtil;
import ma.sorec.gcecourse.data.PersonneLit;
import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.repository.PersonneLitRepository;
import ma.sorec.gcecourse.service.PersonneLitService;
import ma.sorec.gcecourse.service.ReservationService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PersonneLitServiceImpl implements PersonneLitService {

    @Autowired
    private PersonneLitRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private ReservationService reservationService;

    @Override
    public List<PersonneLit> listAll() {
        return repository.findAll();
    }

    @Override
    public PersonneLit save(PersonneLit personneLit) {

        return repository.save(personneLit);
    }

    @Override
    public PersonneLit get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        PersonneLit entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public Long getNombreJoursReservation(Long id) {

        Reservation reservation = reservationService.getByPersonneLitId(id);

        return SharedUtil.getDifferenceDays(reservation.getDateDebut(), reservation.getDateFin());
    }

    @Override
    public PersonneLit getPersonneLitByReservationId(Long id) {
        return repository.findFirstByReservationId(id);
    }

    @Override
    public List<PersonneLit> getPersonneLitByLitId(Long idLit) {
        return repository.findAllByLitId(idLit);
    }

    @Override
    public List<PersonneLit> getPersonneLitByPersonneId(String idPersonne) {
        return repository.findAllByPersonneId(idPersonne);
    }

    @Override
    public List<PersonneLit> getAllPersonneLitsByReservationId(Long idReservation) {
        return repository.findAllByReservationId(idReservation);
    }

}
