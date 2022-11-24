package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.Personne;
import ma.sorec.gcecourse.data.PersonneLit;
import ma.sorec.gcecourse.data.Reservation;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface PersonneLitService {

    List<PersonneLit> listAll();

    PersonneLit save(PersonneLit personneLit);

    PersonneLit get(Long id);

    void delete(Long id);

    Long getNombreJoursReservation(Long id);

    PersonneLit getPersonneLitByReservationId(Long id);

    List<PersonneLit> getPersonneLitByLitId(Long idLit);

    List<PersonneLit> getPersonneLitByPersonneId(String idPersonne);

//    PersonneLit creerPersonneLit(Reservation reservation, Long idLit, Long idPersonne, Date dateDebut, Date dateFin, String typePrix);

    List<PersonneLit> getAllPersonneLitsByReservationId(Long idReservation);

//    List<PersonneLit> getAllPersonneLitByDate(LocalDate date);

}
