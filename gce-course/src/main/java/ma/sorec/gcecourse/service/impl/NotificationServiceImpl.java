package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Notification;
import ma.sorec.gcecourse.data.NotificationProfil;
import ma.sorec.gcecourse.data.NotificationUtilisateur;
import ma.sorec.gcecourse.repository.NotificationRepository;
import ma.sorec.gcecourse.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository repository;

    @Autowired
    private NotificationProfilService notificationProfilService;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private NotificationUtilisateurService notificationUtilisateurService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Override
    public List<Notification> listAll() {
        return repository.findAllByOrderByDateEnvoiDesc();
    }

    @Override
    public Notification save(Notification notification) {

        return repository.save(notification);
    }

    @Override
    public Notification get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        Notification entity = this.get(id);

        repository.delete(entity);
    }

    @Override
    public List<Notification> listAllByCodeProfil(String code, Long idUtilisateur) {
        List<Notification> notifications = repository.findAllByOrderByDateEnvoiDesc();
        List<Notification> result = new ArrayList<>();
        for (Notification notification : notifications) {
            List<NotificationProfil> notificationProfils = notificationProfilService.getAllByNotificationId(notification.getId());
            for (NotificationProfil notificationProfil : notificationProfils) {
                if (notificationProfil.getProfil().getCode().equals(code)) {
                    result.add(notification);
                    break;
                }
            }
        }
        for (Notification notification : result) {
            NotificationUtilisateur notificationUtilisateur = notificationUtilisateurService.getByNotificationIdAndUtilisateurId(notification.getId(), idUtilisateur);
            if (notificationUtilisateur != null) {
                notification.setDejaLu(true);
            } else {
                notification.setDejaLu(false);
            }
        }
        return result;
    }

    @Override
    public List<Notification> listAllByCodeProfilPartiel(String code, Long idUtilisateur) {
        List<Notification> notifications = listAllByCodeProfil(code, idUtilisateur);
        List<Notification> result = new ArrayList<>();
        if (notifications.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                result.add(notifications.get(i));
            }
            return result;
        } else {
            return notifications;
        }
    }

    @Override
    public void setDejaLu(Long idNotification, Long idUtilisateur) {
        NotificationUtilisateur existant = notificationUtilisateurService.getByNotificationIdAndUtilisateurId(idNotification, idUtilisateur);
        if (existant == null) {
            NotificationUtilisateur notificationUtilisateur = NotificationUtilisateur.builder()
                    .notification(repository.getById(idNotification))
                    .utilisateur(utilisateurService.get(idUtilisateur))
                    .build();
            notificationUtilisateurService.save(notificationUtilisateur);
        }
    }
}
