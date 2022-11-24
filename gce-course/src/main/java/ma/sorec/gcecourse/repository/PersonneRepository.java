package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Personne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PersonneRepository extends JpaRepository<Personne, String> {

    List<Personne> findAllByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndNumeroPieceIdentiteContainingIgnoreCaseAndCodeNaturePersonneContainingIgnoreCase(String nom, String prenom, String cin, String codeNaturePersonne);

    List<Personne> findAllByNomContainingIgnoreCaseAndPrenomContainingIgnoreCaseAndNumeroPieceIdentiteContainingIgnoreCaseAndDesignationContainingIgnoreCaseAndRaisonSocialeContainingIgnoreCase(String nom, String prenom, String cin, String designation, String raisonSociale);

    List<Personne> findAllByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNumeroPieceIdentiteContainingIgnoreCase(String nom, String prenom, String cin);

    List<Personne> findAllByNomContainingIgnoreCase(String nom);

    List<Personne> findAllByPrenomContainingIgnoreCase(String prenom);

    List<Personne> findAllByNumeroPieceIdentiteContainingIgnoreCase(String cin);

    List<Personne> findAllByDesignationContainingIgnoreCase(String designation);

    List<Personne> findAllByRaisonSocialeContainingIgnoreCase(String raisonSociale);

    Personne findFirstByNumeroPieceIdentiteIgnoreCase(String numeroPieceIdentite);

    @Query(value = "SELECT SEQ_GCE_ID.nextval FROM dual", nativeQuery = true)
    public BigDecimal getNextValSeqGceId();

}
