package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.RelanceFacturation;
import ma.sorec.gcecourse.data.RelationLAD;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelanceFacturationRepository extends JpaRepository<RelanceFacturation, Long> {

    RelanceFacturation findFirstByIdFacture(String idFacture);
}
