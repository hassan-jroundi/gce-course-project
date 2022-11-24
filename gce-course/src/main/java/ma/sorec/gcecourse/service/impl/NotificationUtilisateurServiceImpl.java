package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.NotificationUtilisateur;
import ma.sorec.gcecourse.repository.NotificationUtilisateurRepository;
import ma.sorec.gcecourse.service.NotificationUtilisateurService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class NotificationUtilisateurServiceImpl implements NotificationUtilisateurService {

    @Autowired
    private NotificationUtilisateurRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<NotificationUtilisateur> listAll() {
        return repository.findAll();
    }

    @Override
    public NotificationUtilisateur save(NotificationUtilisateur notificationUtilisateur) {

        return repository.save(notificationUtilisateur);
    }

    @Override
    public NotificationUtilisateur get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        NotificationUtilisateur entity = this.get(id);
        repository.delete(entity);
    }

    @Override
    public NotificationUtilisateur getByNotificationIdAndUtilisateurId(Long idNotification, Long idUtilisateur) {
        return repository.findFirstByNotificationIdAndUtilisateurId(idNotification, idUtilisateur);
    }
}
