package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ma.sorec.gcecourse.data.Box;
import ma.sorec.gcecourse.data.Piste;

import java.util.Date;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrixPisteDTO {

    Long id;
    Long montant;
    Date dateDebut;
    Date dateFin;
    String idPiste;
    String typePrix;
    String idSession;

}
