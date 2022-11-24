package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ma.sorec.gcecourse.data.Box;

import javax.persistence.*;
import java.util.Date;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrixBoxDTO {

    Long id;
    Long montant;
    Date dateDebut;
    Date dateFin;
    String idBox;
    String typePrix;
    String idSession;
}
