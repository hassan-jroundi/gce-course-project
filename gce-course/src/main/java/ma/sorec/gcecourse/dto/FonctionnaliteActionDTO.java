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
public class FonctionnaliteActionDTO {

    Long id;
    String abreviation;
    String code;
    String description;
    String designation;
    FonctionnaliteDTO fonctionnalite;
    String idSession;

}
