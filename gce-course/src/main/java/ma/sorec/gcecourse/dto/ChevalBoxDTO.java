package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChevalBoxDTO {

    Long id;
    ChevalDTO cheval;
    BoxDTO box;
    String idReservation;
    String nomPersonneAFacturer;
    String statut;
    String idSession;

}
