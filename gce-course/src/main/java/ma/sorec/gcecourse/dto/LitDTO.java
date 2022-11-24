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
public class LitDTO {

    Long id;
    String nom;
    Long ordre;
    ChambreDTO chambre;
    Boolean isActif;
    Long prixLit;
    String idSession;

}
