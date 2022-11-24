package ma.sorec.gcecourse.data.mohr;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import ma.sorec.gcecourse.data.Reservation;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PERSONNE_MOHR")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonneM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDENTIFIANT")
    String id;

    @Column(name = "CODE_NATURE_PERSONNE")
    String codeNaturePersonne;

    @Column(name = "CODE_TITRE_PERSONNE")
    String codeTitrePersonne;

    @Column(name = "ID_BANQUE")
    String idBanque;

    @Column(name = "CODE_CATEGORIE")
    String codeCategorie;

    @Column(name = "NOM")
    String nom;

    @Column(name = "PRENOM")
    String prenom;

    @Column(name = "DATE_NAISSANCE")
    Date dateNaissance;

    @Column(name = "ADRESSE_1")
    String adresse1;

    @Column(name = "ADRESSE_2")
    String adresse2;

    @Column(name = "NUMERO_TELEPHONE_1")
    String numeroTelephone1;

    @Column(name = "NUMERO_TELEPHONE_2")
    String numeroTelephone2;

    @Column(name = "EMAIL")
    String email;

    @Column(name = "RAISON_SOCIALE")
    String raisonSociale;

    @Column(name = "RIB")
    String rib;

    @Column(name = "DATE_CREATION")
    Date dateCreation;

    @Column(name = "CODE_SEXE")
    String codeSexe;

    @Column(name = "DESIGNATION")
    String designation;

    @Column(name = "CODE_PAYS_ORIGINE")
    String codePaysOrigine;

    @Column(name = "ETRANGERE")
    String etrangere;

    @Column(name = "CODE_VILLE_1")
    String codeVille1;

    @Column(name = "CODE_TYPE_PIECE_IDENTITE")
    String codeTypePieceIdentite;

    @Column(name = "NUMERO_PIECE_IDENTITE")
    String numeroPieceIdentite;

    @Column(name= "NOM_GERANT")
    String nomGerant;

    @Column(name= "CATEGORIE_PERS_MORA")
    String categoriePersMora;

    @Transient
    String codeNatureRelation;

    @Transient
    List<Reservation> reservations;

}
