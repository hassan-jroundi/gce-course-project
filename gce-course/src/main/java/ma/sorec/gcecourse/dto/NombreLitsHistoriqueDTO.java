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
public class NombreLitsHistoriqueDTO {

    Long id;
    Long nombre;
    Date dateDebut;
    Date dateFin;
    String idChambre;
    String idSession;
}
