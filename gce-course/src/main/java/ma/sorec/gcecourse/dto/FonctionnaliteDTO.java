package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FonctionnaliteDTO {

    Long id;
    String abreviation;
    String code;
    String description;
    String designation;
    BigDecimal ordre;
    String url;
    String urlIcon;
    String designationMenu;
    String idSession;

}
