package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FactureRepository extends JpaRepository<Facture, Long> {

    List<Facture> findAllByIdPersonneAFacturer(String id);

    List<Facture> findAllByCodeStatutFacture(String codeStatutFacture);

    List<Facture> findAllByIdOperateur(String idOperateur);

    List<Facture> findAllByDateFactureIsNotNullAndDatePaiementIsNull();
}
