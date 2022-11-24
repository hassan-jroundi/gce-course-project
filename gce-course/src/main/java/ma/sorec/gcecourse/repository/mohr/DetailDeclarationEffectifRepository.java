package ma.sorec.gcecourse.repository.mohr;

import ma.sorec.gcecourse.data.mohr.DetailDeclarationEffectif;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailDeclarationEffectifRepository extends JpaRepository<DetailDeclarationEffectif, String> {

    DetailDeclarationEffectif findFirstByIdChevalOrderByDateCreationDesc(Long idCheval);

}
