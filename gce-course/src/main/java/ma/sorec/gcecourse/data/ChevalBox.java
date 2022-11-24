package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "GCE_CHEVAL_BOX")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChevalBox extends BaseEntite {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator (name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CHEVAL")
    Cheval cheval;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_BOX")
    Box box;

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
        return "ChevalBox{" +
                "id=" + id +
                ", cheval=" + cheval +
                ", box=" + box +
                ", idReservation=" + idReservation +
                ", nomPersonneAFacturer=" + nomPersonneAFacturer +
                ", statut=" + statut +
                '}';
    }
}
