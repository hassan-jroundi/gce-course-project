package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "CHEVAL_MOHR")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cheval implements Serializable {

    @Id
    @Column(name = "IDENTIFIANT")
    Long id;

    @Column(name = "SEXE")
    @Null
    String sexe;

    @Column(name = "CODE_RACE")
    @Null
    String codeRace;

    @Column(name = "NOM")
    String nom;

    @Column(name = "NUMERO_ESRIMA")
    String numeroEsrima;

    @Column(name = "NUMERO_TRANSPONDEUR")
    String numeroTranspondeur;

    @Column(name = "DATE_NAISSANCE")
    Date dateNaissance;

    @Column(name = "CODE_CHEVAL")
    String codeCheval;

    @Column(name = "ETAT")
    String etat;

    @Column(name = "DATE_ETAT")
    Date dateEtat;

    @Column(name = "DATE_DECES")
    @Null
    Date dateDeces;

    @Column(name = "DATE_AUTORISATION_COURIR")
    @Null
    Date dateAutorisationCourir;

    @Column(name = "OBSERVATION")
    @Null
    String observation;

    @Column(name = "MOTIF_DESACTIVATION")
    @Null
    String motifDesactivation;

    @Transient
    String statut;

    @Transient
    String idSession;

    @Transient
    String transpondeur;

    @Transient
    String libelleEtat;
}
