package ma.sorec.gcecourse.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "GCE_DETAIL_RESERVATION")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetailReservation extends BaseEntite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator(name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "ID_RESERVATION")
    String idReservation;

    @Column(name = "DATE_DEBUT")
    @Temporal(TemporalType.DATE)
    Date dateDebut;

    @Column(name = "DATE_FIN")
    @Temporal(TemporalType.DATE)
    Date dateFin;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "ID_FACTURE")
    Facture facture;

    @Column(name = "CODE_STATUT_DETAIL_RESERVATION")
    String codeStatutDetailReservation;

    @Transient
    String nomPersonneAFacturer;

    @Transient
    String statut;

    @Transient
    Reservation reservation;

    @Transient
    String libelleStatut;

    @Transient
    String idFacture;

    @Transient
    String nomConcerne;

    @Transient
    String libelleTypeReservation;

    @Transient
    String nomLit;

    @Transient
    String nomBox;

    @Transient
    String nomPiste;

    @Transient
    String idPersonneAFacturer;

    @Transient
    String idSession;

}
