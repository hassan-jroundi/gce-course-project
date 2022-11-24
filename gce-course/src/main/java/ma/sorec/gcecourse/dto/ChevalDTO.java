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
public class ChevalDTO {

    Long id;
    String sexe;
    String codeRace;
    String nom;
    String numeroEsrima;
    String numeroTranspondeur;
    Date dateNaissance;
    String codeCheval;
    String etat;
    Date dateEtat;
    Date dateDeces;
    Date dateAutorisationCourir;
    String observation;
    String motifDesactivation;
    String statut;
    String idSession;
    String transpondeur;
    String libelleEtat;

    @Override
    public String toString() {
        return "ChevalDTO{" +
                "id='" + id + '\'' +
                ", sexe='" + sexe + '\'' +
                ", codeRace='" + codeRace + '\'' +
                ", nom='" + nom + '\'' +
                ", numeroEsrima='" + numeroEsrima + '\'' +
                ", numeroTranspondeur='" + numeroTranspondeur + '\'' +
                ", dateNaissance=" + dateNaissance +
                ", codeCheval='" + codeCheval + '\'' +
                ", etat='" + etat + '\'' +
                ", dateEtat=" + dateEtat +
                ", dateDeces=" + dateDeces +
                ", dateAutorisationCourir=" + dateAutorisationCourir +
                ", observation='" + observation + '\'' +
                ", motifDesactivation='" + motifDesactivation + '\'' +
                ", statut='" + statut + '\'' +
                ", idSession='" + idSession + '\'' +
                ", transpondeur='" + transpondeur + '\'' +
                ", libelleEtat='" + libelleEtat + '\'' +
                '}';
    }
}
