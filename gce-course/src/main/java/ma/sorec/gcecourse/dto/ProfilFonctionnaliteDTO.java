package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ma.sorec.gcecourse.data.Fonctionnalite;
import ma.sorec.gcecourse.data.Profil;
import ma.sorec.gcecourse.data.ProfilFonctionnaliteAction;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfilFonctionnaliteDTO {

    Long id;
    FonctionnaliteDTO fonctionnalite;
    ProfilDTO profil;
    String idSession;
//    List<ProfilFonctionnaliteAction> profilFonctionnaliteActions;

}
