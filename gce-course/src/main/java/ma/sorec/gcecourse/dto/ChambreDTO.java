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
public class ChambreDTO {

    Long id;
    String nom;
    String numero;
    String etage;
    ImmeubleDTO immeuble;
    Boolean isActif;
    Long prixLit;
    String prixUnitaire;
    String prixForfaitaire;
    Long nombreLits;
    String idSession;

}
