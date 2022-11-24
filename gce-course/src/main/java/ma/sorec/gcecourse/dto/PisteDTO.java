package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PisteDTO {

    Long id;
    String nom;
    String numero;
    SiteDTO site;
    Boolean isActif;
    String idSession;

}
