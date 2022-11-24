package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.NotificationProfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationProfilRepository extends JpaRepository<NotificationProfil, Long> {

    List<NotificationProfil> findAllByNotificationId(Long id);
}
