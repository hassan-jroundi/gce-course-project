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
public class FactureDTO {

    Long id;
    Date dateFacture;
    Long montantTotal;
    String codeStatutFacture;
    String codeModePaiement;
    String datePaiement;
    String idOperateur;
    String libelleStatut;
    String libelleModePaiement;
    String idPersonneAFacturer;
    String idSession;
    String nomOperateur;

}
