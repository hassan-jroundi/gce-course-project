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
public class UtilisateurSiteDTO {

    Long id;
    Date dateDebut;
    Date dateFin;
    UtilisateurDTO utilisateur;
    SiteDTO site;
    String idSession;

}
