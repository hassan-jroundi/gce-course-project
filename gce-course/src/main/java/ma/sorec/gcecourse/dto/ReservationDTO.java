package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;
import java.util.Set;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReservationDTO {

    Long id;
    Set<ChevalBoxDTO> chevalBoxs;
    Set<PersonneLitDTO> personneLits;
    Set<ChevalPisteDTO> chevalPistes;
    String idPersonneFacture;
    String codeStatutReservation;
    String codeTypePrix;
    Date dateDebut;
    Date dateFin;
    List<DetailReservationDTO> detailReservations;
    String libelleStatut;
    String idSession;
    boolean enCours;
}
