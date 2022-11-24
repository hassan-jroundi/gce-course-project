package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ma.sorec.gcecourse.data.ProfilFonctionnalite;
import ma.sorec.gcecourse.data.UtilisateurProfil;

import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfilDTO {

    Long id;
    private String abreviation;
    private String code;
    private String description;
    private String designation;
    String idSession;
//    private List<ProfilFonctionnalite> profilFonctionnalites;
//    private List<UtilisateurProfil> utilisateurProfils;

}
