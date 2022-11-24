package ma.sorec.gcecourse.repository.mohr;

import ma.sorec.gcecourse.data.mohr.TranspondeurChevalM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranspondeurChevalMRepository extends JpaRepository<TranspondeurChevalM, String> {

    List<TranspondeurChevalM> findAllByNumeroTranspondeurContainingIgnoreCaseAndDateFinIsNull(String numeroTranspondeur);

    TranspondeurChevalM findFirstByIdChevalContainingIgnoreCaseAndDateFinIsNull(String idCheval);
}
