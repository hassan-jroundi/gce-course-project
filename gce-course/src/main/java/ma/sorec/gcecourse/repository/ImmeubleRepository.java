package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Immeuble;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImmeubleRepository extends JpaRepository<Immeuble, Long> {

    List<Immeuble> findAllBySiteNomOrderByNom(String nomSite);

    List<Immeuble> findAllBySiteNomAndIsActifIsTrueOrderByNom(String nomSite);

    Immeuble findFirstByNomIgnoreCase(String nom);
}
