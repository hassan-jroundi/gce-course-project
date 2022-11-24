package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.NotificationProfil;
import ma.sorec.gcecourse.repository.NotificationProfilRepository;
import ma.sorec.gcecourse.service.NotificationProfilService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class NotificationProfilServiceImpl implements NotificationProfilService {

    @Autowired
    private NotificationProfilRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Override
    public List<NotificationProfil> listAll() {
        return repository.findAll();
    }

    @Override
    public NotificationProfil save(NotificationProfil notificationProfil) {

        return repository.save(notificationProfil);
    }

    @Override
    public NotificationProfil get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        NotificationProfil entity = this.get(id);
        repository.delete(entity);
    }

    @Override
    public List<NotificationProfil> getAllByNotificationId(Long id) {
        return repository.findAllByNotificationId(id);
    }
}
