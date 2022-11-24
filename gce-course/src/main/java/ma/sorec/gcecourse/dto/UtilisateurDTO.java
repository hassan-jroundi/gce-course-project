package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ma.sorec.gcecourse.data.UtilisateurProfil;
import ma.sorec.gcecourse.data.UtilisateurSession;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UtilisateurDTO {

    Long id;
    String codeEtat;
    String login;
    String motDePasse;
    PersonneDTO personne;
    String codeTypeUtilisateur;
//    List<UtilisateurProfil> utilisateurProfils;
//    List<UtilisateurSession> utilisateurSessions;
    Boolean alreadyConnected;
    String idProfil;
    String nomProfil;
    String idSite;
    SiteDTO site;
    ProfilDTO profil;
    String idSession;

}
