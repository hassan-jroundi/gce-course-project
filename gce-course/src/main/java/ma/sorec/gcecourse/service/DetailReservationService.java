package ma.sorec.gcecourse.service;

import ma.sorec.gcecourse.data.DetailReservation;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DetailReservationService {

    List<DetailReservation> listAll();

    DetailReservation save(DetailReservation detailReservation);

    DetailReservation get(Long id);

    void delete(Long id);

    List<DetailReservation> getAllByIdReservation(String idReservation);

    DetailReservation creerDetailReservation(String idReservation, Date dateDebut, Date dateFin, String typePrix, String idSession);

    List<DetailReservation> getAllDetailReservationByDate(LocalDate date);

    List<DetailReservation> getAllByIdFacture(Long idFacture);

}
