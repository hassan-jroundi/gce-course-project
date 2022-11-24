package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ma.sorec.gcecourse.data.Profil;
import ma.sorec.gcecourse.data.Utilisateur;

import java.util.Date;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UtilisateurProfilDTO {

    Long id;
    Date dateDebut;
    Date dateFin;
    ProfilDTO profil;
    UtilisateurDTO utilisateur;
    String idSession;

}
