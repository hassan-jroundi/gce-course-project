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
public class RelationLADDTO {

    Long id;
    String idLAD;
    String idEmployeur;
    Date dateDebutContrat;
    Date dateFinContrat;
    String idSession;

}
