package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.FonctionnaliteAction;

import java.util.List;

public interface FonctionnaliteActionService {

    List<FonctionnaliteAction> listAll();

    FonctionnaliteAction save(FonctionnaliteAction fonctionnaliteAction);

    FonctionnaliteAction get(Long id);

    void delete(Long id);

    List<FonctionnaliteAction> getAllByFonctionnaliteId(Long id);

}
