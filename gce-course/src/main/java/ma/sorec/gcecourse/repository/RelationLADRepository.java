package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.RelationLAD;
import ma.sorec.gcecourse.data.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelationLADRepository extends JpaRepository<RelationLAD, Long> {

    RelationLAD findFirstByIdLADAndDateFinContratIsNull(String idLAD);

}
