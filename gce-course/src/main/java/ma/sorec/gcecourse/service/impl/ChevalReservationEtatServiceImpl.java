package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.ChevalReservationEtat;
import ma.sorec.gcecourse.data.Utilisateur;
import ma.sorec.gcecourse.repository.ChevalReservationEtatRepository;
import ma.sorec.gcecourse.service.ChevalReservationEtatService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ChevalReservationEtatServiceImpl implements ChevalReservationEtatService {

    @Autowired
    private ChevalReservationEtatRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<ChevalReservationEtat> listAll() {
        return repository.findAll();
    }

    @Override
    public ChevalReservationEtat save(ChevalReservationEtat chevalReservationEtat) {

        chevalReservationEtat.setDateCreation(new Date());
        if (chevalReservationEtat.getIdSession() != null) {
            Utilisateur utilisateur = utilisateurSessionService.getByIdSession(chevalReservationEtat.getIdSession()).getUtilisateur();
            if (utilisateur != null) {
                chevalReservationEtat.setUserCreation(utilisateurSessionService.getByIdSession(chevalReservationEtat.getIdSession()).getUtilisateur().getId());
            }
        }

        return repository.save(chevalReservationEtat);
    }

    @Override
    public ChevalReservationEtat get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        ChevalReservationEtat entity = this.get(id);
        repository.delete(entity);
    }

    @Override
    public ChevalReservationEtat getByReservationId(Long idReservation) {
        return repository.findFirstByReservationId(idReservation);
    }
}
