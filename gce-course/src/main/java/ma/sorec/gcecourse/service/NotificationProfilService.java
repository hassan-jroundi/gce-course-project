package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.NotificationProfil;

import java.util.List;

public interface NotificationProfilService {

    List<NotificationProfil> listAll();

    NotificationProfil save(NotificationProfil notificationProfil);

    NotificationProfil get(Long id);

    void delete(Long id);

    List<NotificationProfil> getAllByNotificationId(Long id);

}
