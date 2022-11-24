package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChevalPisteDTO {

    Long id;
    ChevalDTO cheval;
    PisteDTO piste;
    Date dateDebut;
    Date dateFin;
    String idSession;

}
