package ma.sorec.gcecourse.service.impl;

import ma.sorec.gcecourse.data.Box;
import ma.sorec.gcecourse.data.Ecurie;
import ma.sorec.gcecourse.exceptions.CustomException;
import ma.sorec.gcecourse.repository.BoxRepository;
import ma.sorec.gcecourse.repository.EcurieRepository;
import ma.sorec.gcecourse.service.EcurieService;
import ma.sorec.gcecourse.service.UtilisateurSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class EcurieServiceImpl implements EcurieService {

    @Autowired
    private EcurieRepository repository;

    @Autowired
    UtilisateurSessionService utilisateurSessionService;

    @Autowired
    private BoxRepository boxRepository;

    @Override
    public List<Ecurie> listAll() {
        return repository.findAll();
    }

    @Override
    public Ecurie save(Ecurie ecurie) {
        List<Ecurie> ecuries = repository.findAllByNomIgnoreCase(ecurie.getNom());
        boolean identique = false;
        if (!(ecuries.isEmpty())) {
            for (Ecurie item : ecuries) {
                if ((item.getId().equals(ecurie.getId()))) {
                    identique = true;
                    break;
                }
            }
            if (identique) {
                throw new CustomException("C", "Aucun changement apporté au nom.");
            } else {
                throw new CustomException("C", "Le nom existe déjà");
            }
        } else {
            ecurie.setDateCreation(new Date());
            ecurie.setUserCreation(ecurie.getIdSession() != null ? utilisateurSessionService.getByIdSession(ecurie.getIdSession()).getUtilisateur().getId() : null);

            return repository.save(ecurie);
        }
    }

    @Override
    public Ecurie get(Long id) {

        return repository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {

        Ecurie entity = this.get(id);
        List<Box> boxes = boxRepository.findAllByEcurieId(entity.getId());
        if (!(boxes.isEmpty())) {
            throw new CustomException("C", "Cette écurie a des boxs disponibles et ne peut être supprimée");
        } else {
            repository.delete(entity);
        }
    }

    @Override
    public List<Ecurie> getAllByNomSite(String nomSite) {
        return repository.findAllBySiteNom(nomSite);
    }
}
