package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Chambre;
import ma.sorec.gcecourse.data.Immeuble;
import ma.sorec.gcecourse.data.Reservation;
import ma.sorec.gcecourse.exceptions.CustomException;
import ma.sorec.gcecourse.repository.ChambreRepository;
import ma.sorec.gcecourse.repository.ImmeubleRepository;
import ma.sorec.gcecourse.service.ImmeubleService;
import ma.sorec.gcecourse.service.ReservationService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ImmeubleServiceImpl implements ImmeubleService {

    @Autowired
    private ImmeubleRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private ReservationService reservationService;

    @Override
    public List<Immeuble> listAll() {
        return repository.findAll();
    }

    @Override
    public Immeuble save(Immeuble immeuble) {

        immeuble.setDateCreation(new Date());
        if (immeuble.getIdSession() != null) {
            immeuble.setUserCreation(utilisateurSessionService.getByIdSession(immeuble.getIdSession()).getUtilisateur().getId());
        }

        return repository.save(immeuble);
    }

    @Override
    public Immeuble get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        Immeuble entity = this.get(id);
        List<Reservation> reservations = reservationService.getAllByPersonneLitLitChambreImmeubleId(entity.getId());
        List<Chambre> chambres = chambreRepository.findAllByImmeubleIdOrderByNom(id);
        if (!(reservations.isEmpty())) {
            throw new CustomException("C", "Cet immeuble est lié à des réservations et ne peut être supprimé.");
        } else if (!(chambres.isEmpty())) {
            throw new CustomException("C", "Cet immeuble a des lits disponibles et ne peut être supprimé.");
        } else {
            repository.delete(entity);
        }
    }

    @Override
    public List<Immeuble> getAllByNomSite(String nomSite) {
        return repository.findAllBySiteNomOrderByNom(nomSite);
    }

    @Override
    public List<Immeuble> getAllActifImmeuble(String nomSite) {
        return repository.findAllBySiteNomAndIsActifIsTrueOrderByNom(nomSite);
    }
}
