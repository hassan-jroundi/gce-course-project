package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ChambreRepository extends JpaRepository<Chambre, Long> {

    List<Chambre> findAllByImmeubleIdOrderByNom(Long idImmeuble);

    List<Chambre> findAllByImmeubleIdAndIsActifIsTrueOrderByNom(Long idImmeuble);

    @Query(value = "SELECT SEQ_GCE_ID.nextval FROM dual", nativeQuery = true)
    public BigDecimal getNextValSeqGceId();
}
