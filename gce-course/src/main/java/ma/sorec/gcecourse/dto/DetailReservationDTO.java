package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ma.sorec.gcecourse.data.Facture;

import java.util.Date;


@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetailReservationDTO {

    Long id;
    String idReservation;
    Date dateDebut;
    Date dateFin;
    Facture facture;
    String codeStatutDetailReservation;
    String nomPersonneAFacturer;
    String statut;
    ReservationDTO reservation;
    String libelleStatut;
    String idFacture;
    String nomConcerne;
    String libelleTypeReservation;
    String nomLit;
    String nomBox;
    String nomPiste;
    String idPersonneAFacturer;
    String idSession;

}
