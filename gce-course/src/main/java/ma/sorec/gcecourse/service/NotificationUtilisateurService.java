package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.NotificationUtilisateur;

import java.util.List;

public interface NotificationUtilisateurService {

    List<NotificationUtilisateur> listAll();

    NotificationUtilisateur save(NotificationUtilisateur notificationUtilisateur);

    NotificationUtilisateur get(Long id);

    void delete(Long id);

    NotificationUtilisateur getByNotificationIdAndUtilisateurId(Long idNotification, Long idUtilisateur);

}
