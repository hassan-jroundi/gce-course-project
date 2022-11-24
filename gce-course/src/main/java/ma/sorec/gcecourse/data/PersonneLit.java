package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "GCE_PERSONNE_LIT")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonneLit extends BaseEntite {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator (name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_LIT")
    Lit lit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PERSONNE")
    Personne personne;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "ID_RESERVATION")
    Reservation reservation;

    @Transient
    String idReservation;

    @Transient
    String nomPersonneAFacturer;

    @Transient
    String statut;

    @Transient
    String idSession;

    @Override
    public String toString() {
        return "PersonneLit{" +
                "id=" + id +
                ", lit=" + lit +
                ", personne=" + personne +
                ", idReservation=" + idReservation +
                ", nomPersonneAFacturer=" + nomPersonneAFacturer +
                ", statut=" + statut +
                '}';
    }
}
