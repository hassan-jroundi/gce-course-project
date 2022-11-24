package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Piste;
import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.exceptions.CustomException;
import ma.sorec.gcecourse.repository.PisteRepository;
import ma.sorec.gcecourse.service.PisteService;
import ma.sorec.gcecourse.service.ReservationService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PisteServiceImpl implements PisteService {

    @Autowired
    private PisteRepository repository;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<Piste> listAll() {
        return repository.findAll();
    }

    @Override
    public Piste save(Piste piste) {

        piste.setDateCreation(new Date());
        piste.setUserCreation(utilisateurSessionService.getByIdSession(piste.getIdSession()).getUtilisateur().getId());
        Piste entity = repository.save(piste);

        return entity;
    }

    @Override
    public Piste update(Piste piste) {

        piste.setDateModification(new Date());
        piste.setUserModification(utilisateurSessionService.getByIdSession(piste.getIdSession()).getUtilisateur().getId());
        Piste entity = repository.save(piste);

        return entity;
    }

    @Override
    public Piste get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        Piste entity = this.get(id);
        List<Reservation> reservations = reservationService.getAllByChevalPistePisteId(entity.getId());
        if (reservations.size() > 0) {
            throw new CustomException("C", "Cette piste est liée à des réservations et ne peut être supprimée.");
        } else {
            repository.delete(entity);
        }
    }

    @Override
    public List<Piste> getAllByNomSite(String nomSite) {
        return repository.findAllBySiteNom(nomSite);
    }
}
