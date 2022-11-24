package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Cheval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChevalRepository extends JpaRepository<Cheval, Long> {

    List<Cheval> findAllByNumeroEsrimaContainingAndNomContainingIgnoreCase(String numeroEsrima, String nom);

    List<Cheval> findAllByNumeroEsrimaContainingAndNumeroTranspondeurContaining(String numeroEsrima, String numeroTranspondeur);

    List<Cheval> findAllByNumeroEsrimaContainingIgnoreCase(String numeroEsrima);

    List<Cheval> findAllByNumeroTranspondeurContainingIgnoreCase(String numeroTranspondeur);

    Cheval findFirstByNumeroEsrima(String numeroEsrima);

    List<Cheval> findAllByNomContainingIgnoreCaseAndNumeroEsrimaContaining(String nom, String numeroEsrima);
}
