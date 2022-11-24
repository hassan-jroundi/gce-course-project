package ma.sorec.gcecourse.service;

import com.itextpdf.text.DocumentException;
import ma.sorec.gcecourse.data.Facture;
import ma.sorec.gcecourse.data.Reservation;

import javax.mail.MessagingException;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface ReservationService {

    Reservation creerReservation(Date dateDebut, Date dateFin, Long idPersonneAFacturer, Long idPersonne, Long idBox, Long idPiste, Long idLit, Long idCheval, String typePrix, String typeReservation, String idSession);

    Reservation modifierReservation(Long idReservation, Date dateDebut, Date dateFin, Long idPersonneAFacturer, Long idPersonne, Long idBox, Long idPiste, Long idLit, Long idCheval, String typePrix, String typeReservation, String idSession);

    void supprimerReservation(Long idReservation);

    void arreterReservation(Long idReservation);

    void decouperReservationParMois(LocalDate dateDebutRes, LocalDate dateFinRes, Reservation reservation, Long idLit, Long idBox, Long idPersonne, Long idCheval, Long idPiste, String typePrix, String idSession);

    List<Reservation> listAll();

    Reservation save(Reservation reservation);

    Reservation get(Long id);

    void delete(Long id);

    List<Reservation> getAllByPersonneAFacturerId(String idPersonneAFacturer);

    List<Reservation> getAllChevalBoxReservations();

    List<Reservation> getAllChevalBoxReservationsByCriteria(String nomSite, Long mois, Long annee);

    List<Reservation> getAllPersonneLitReservationsByCriteria(String nomSite, Long mois, Long annee);

    List<Reservation> getAllPersonneLitReservations();

    List<Reservation> getAllPersonneLitReservationsByPersonneId(String id);

    List<Reservation> getAllChevalBoxReservationsByChevalId(Long id);

    List<Reservation> getAllChevalPisteReservations();

    List<Reservation> getAllChevalPisteReservationsByPisteId(Long id);

    List<Reservation> getAllChevalPisteReservationsByCriteria(String nomSite, Long idPiste, Long mois, Long annee);

    List<Reservation> getAllChevalPisteReservationsByDate(Date date, Long heure, Long idPiste) throws ParseException;

    List<Reservation> getAllChevalPisteReservationsByChevalId(Long id);

    Long getReservationPrixEnCours(Long idReservation);

    List<Reservation> getAllByChevalIdEnCours(Long id);

    List<Reservation> getReservationsEnCours();

    List<Reservation> getReservationsChevalBoxEtChevalPisteEnCours();

    List<Reservation> getAllByPersonneIdEnCours(String id);

    List<Reservation> GetAllByPersonneIdNonFacture(String id);

    List<Reservation> getAllByChevalId(Long id);

    List<Reservation> getAllByPersonneId(String id);

    Facture facturer(List<String> detailReservationIds) throws DocumentException, MessagingException, IOException;

//    List<Reservation> getAllByFactureId(Long idFacture);

    List<Reservation> getAllByChevalPistePisteId(Long id);

    List<Reservation> getAllByChevalBoxBoxId(Long id);

    List<Reservation> getAllByPersonneLitLitId(Long id);

    List<Reservation> getAllByPersonneLitLitChambreId(Long id);

    List<Reservation> getAllByPersonneLitLitChambreImmeubleId(Long id);

    Reservation getByChevalBoxId(Long id);

    Reservation getByPersonneLitId(Long id);

    Boolean reservationDejaFacturee(Long id);

}
