package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.NotificationUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationUtilisateurRepository extends JpaRepository<NotificationUtilisateur, Long> {

    NotificationUtilisateur findFirstByNotificationIdAndUtilisateurId(Long idNotification, Long idUtilisateur);

}
