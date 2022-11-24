package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Immeuble;

import java.util.List;

public interface ImmeubleService {

    List<Immeuble> listAll();

    Immeuble save(Immeuble immeuble);

    Immeuble get(Long id);

    void delete(Long id);

    List<Immeuble> getAllByNomSite(String nomSite);

    List<Immeuble> getAllActifImmeuble(String nomSite);

}
