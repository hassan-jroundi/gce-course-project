package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByOrderByDateEnvoiDesc();

}
