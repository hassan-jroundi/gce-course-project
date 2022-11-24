package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> listAll();

    Notification save(Notification notification);

    Notification get(Long id);

    void delete(Long id);

    List<Notification> listAllByCodeProfil(String code, Long idUtilisateur);

    List<Notification> listAllByCodeProfilPartiel(String code, Long idUtilisateur);

    void setDejaLu(Long idNotification, Long idUtilisateur);

}
