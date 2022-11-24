package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonneDTO{

    String id;
    String codeNaturePersonne;
    String codeTypePersonne;
    String codeTitrePersonne;
    String idBanque;
    String codeCategorie;
    String nom;
    String prenom;
    Date dateNaissance;
    String adresse1;
    String adresse2;
    String numeroTelephone1;
    String numeroTelephone2;
    String email;
    String raisonSociale;
    String rib;
    String codeSexe;
    String designation;
    String codePaysOrigine;
    String etrangere;
    String codeVille1;
    String codeTypePieceIdentite;
    String numeroPieceIdentite;
    String nomGerant;
    String categoriePersMora;
    String databaseSource;
    String idEmployeur;
    List<ReservationDTO> reservations;
    String idSession;

}
