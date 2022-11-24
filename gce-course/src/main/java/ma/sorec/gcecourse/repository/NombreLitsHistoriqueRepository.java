package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.NombreLitsHistorique;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NombreLitsHistoriqueRepository extends JpaRepository<NombreLitsHistorique, Long> {

    List<NombreLitsHistorique> findAllByIdChambre(String id);

    NombreLitsHistorique findFirstByIdChambreAndDateFinIsNull(String id);
}
