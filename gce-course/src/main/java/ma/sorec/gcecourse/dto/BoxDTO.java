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
public class BoxDTO {

    Long id;
    String nom;
    String numero;
    Integer ordre;
    EcurieDTO ecurie;
    Boolean isActif;
    String prixUnitaire;
    String prixForfaitaire;
    String idSession;
}
