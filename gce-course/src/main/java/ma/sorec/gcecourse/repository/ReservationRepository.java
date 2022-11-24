package ma.sorec.gcecourse.repository;

import ma.sorec.gcecourse.data.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    @Query(value = "SELECT SEQ_GCE_ID.nextval FROM dual", nativeQuery = true)
    public BigDecimal getNextValSeqGceId();

    List<Reservation> findAllByIdPersonneFactureOrderByDateDebutDesc(String idPersonneAFacturer);

    List<Reservation> findAllByChevalBoxsIsNotNull();

    List<Reservation> findAllByChevalBoxsIsNotNullAndChevalBoxsBoxEcurieSiteNomAndChevalBoxsBoxIsActif(String nomSite, Boolean isActif);

    List<Reservation> findAllByPersonneLitsIsNotNullAndPersonneLitsLitChambreImmeubleSiteNomAndPersonneLitsLitIsActif(String nomSite, Boolean isActif);

    List<Reservation> findAllByChevalPistesIsNotNullAndChevalPistesPisteSiteNom(String nomSite);

    List<Reservation> findAllByPersonneLitsIsNotNull();

    List<Reservation> findAllByPersonneLitsIsNotNullAndPersonneLitsPersonneId(String id);

    List<Reservation> findAllByChevalBoxsIsNotNullAndChevalBoxsChevalId(Long id);

    List<Reservation> findAllByChevalPistesIsNotNullAndChevalPistesChevalId(Long id);

    List<Reservation> findAllByChevalPistesIsNotNull();

    List<Reservation> findAllByChevalPistesIsNotNullAndChevalPistesPisteIdAndChevalPistesPisteSiteNom(Long idPiste, String nomSite);

    List<Reservation> findAllByChevalPistesIsNotNullAndChevalPistesPisteId(Long id);

    List<Reservation> findAllByChevalBoxsChevalIdOrChevalPistesChevalId(Long id1, Long id2);

//    List<Reservation> findAllByFactureId(Long id);

    List<Reservation> findAllByPersonneLitsPersonneId(String id);

    List<Reservation> findAllByChevalPistesChevalId(Long id);

    List<Reservation> findAllByChevalBoxsChevalId(Long id);

    List<Reservation> findAllByChevalPistesPisteId(Long id);

    List<Reservation> findAllByChevalBoxsBoxId(Long id);

    List<Reservation> findAllByPersonneLitsLitId(Long id);

    List<Reservation> findAllByPersonneLitsLitChambreId(Long id);

    List<Reservation> findAllByPersonneLitsLitChambreImmeubleId(Long id);

    Reservation findFirstByChevalBoxsId(Long id);

    Reservation findFirstByPersonneLitsId(Long id);

//    List<Reservation> findAllByPersonneLitsPersonneNom(String nom);
//
//    List<Reservation> findAllByChevalBoxsChevalNumeroEsrimaContainingIgnoreCase(String numeroEsrima);
//
//    List<Reservation> findAllByChevalPistesChevalNumeroEsrimaContainingIgnoreCase(String numeroEsrima);
}
