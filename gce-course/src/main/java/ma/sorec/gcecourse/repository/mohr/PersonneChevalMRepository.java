package ma.sorec.gcecourse.repository.mohr;

import ma.sorec.gcecourse.data.mohr.PersonneChevalM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonneChevalMRepository extends JpaRepository<PersonneChevalM, String> {

    PersonneChevalM findByIdChevalAndCodeNatureRelationAndDateFinIsNull(String idCheval, String codeNatureRelation);

    List<PersonneChevalM> findAllByIdPersonneAndCodeNatureRelationAndDateFinIsNull(String idPersonne, String codeNatureRelation);

    List<PersonneChevalM> findAllByIdChevalAndCodeNatureRelation(String idCheval, String codeNatureRelation);

    PersonneChevalM findFirstByIdChevalAndCodeNatureRelationAndDateFinIsNull(String idCheval, String codeNatureRelation);

    List<PersonneChevalM> findAllByIdCheval(String idCheval);

    List<PersonneChevalM> findAllByIdPersonne(String idPersonne);

}
