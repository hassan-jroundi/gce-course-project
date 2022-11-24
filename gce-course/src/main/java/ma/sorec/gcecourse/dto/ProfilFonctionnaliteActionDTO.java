package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ma.sorec.gcecourse.data.Fonctionnalite;
import ma.sorec.gcecourse.data.Profil;

import java.math.BigDecimal;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfilFonctionnaliteActionDTO {

    Long id;
    BigDecimal idFonctionnaliteAction;
    ProfilFonctionnaliteDTO profilFonctionnalite;
    String idSession;

}
