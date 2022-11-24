package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "GCE_RESERVATION")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reservation extends BaseEntite {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_res_id")
    @SequenceGenerator (name = "seq_gce_res_id", sequenceName = "SEQ_GCE_RES_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    Set<ChevalBox> chevalBoxs;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    Set<PersonneLit> personneLits;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    Set<ChevalPiste> chevalPistes;

    @Column(name = "ID_PERSONNE_FACTURE")
    String idPersonneFacture;

    @Column(name = "CODE_STATUT_RESERVATION")
    String codeStatutReservation;

    @Column(name = "CODE_TYPE_PRIX")
    String codeTypePrix;

    @Column(name = "DATE_DEBUT")
    @Temporal(TemporalType.DATE)
    Date dateDebut;

    @Column(name = "DATE_FIN")
    @Temporal(TemporalType.DATE)
    Date dateFin;

    @Transient
    List<DetailReservation> detailReservations;

    @Transient
    String libelleStatut;

    @Transient
    String idSession;

    @Transient
    boolean enCours;

}
