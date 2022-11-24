package ma.sorec.gcecourse.repository.mohr;

import ma.sorec.gcecourse.data.mohr.PersonneM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonneMRepository extends JpaRepository<PersonneM, String> {

    List<PersonneM> findAllByNumeroPieceIdentiteContainingIgnoreCase(String numeroPieceIdentite);

    List<PersonneM> findAllByNumeroPieceIdentiteContainingIgnoreCaseAndNomContainingIgnoreCaseAndPrenomContainingIgnoreCase(String numeroPieceIdentite, String nom, String prenom);

    List<PersonneM> findAllByRaisonSocialeContainingIgnoreCase(String raisonSociale);

    List<PersonneM> findAllByDesignationContainingIgnoreCase(String designation);

    List<PersonneM> findAllByNomContainingIgnoreCase(String nom);

    List<PersonneM> findAllByNomContainingIgnoreCaseOrDesignationContainingIgnoreCaseOrRaisonSocialeContainingIgnoreCase(String nom, String designation, String raisonSociale);

    List<PersonneM> findAllByPrenomContainingIgnoreCase(String prenom);

    List<PersonneM> findAllByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNumeroPieceIdentiteContainingIgnoreCase(String nom, String prenom, String cin);
}
