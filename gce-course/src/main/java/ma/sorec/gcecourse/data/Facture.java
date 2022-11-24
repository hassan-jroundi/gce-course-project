package ma.sorec.gcecourse.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "GCE_FACTURE")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Facture extends BaseEntite {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_fac_id")
    @SequenceGenerator (name = "seq_gce_fac_id", sequenceName = "SEQ_GCE_FAC_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "DATE_FACTURE")
    @Temporal(TemporalType.DATE)
    Date dateFacture;

    @Column(name = "MONTANT_TOTAL")
    Long montantTotal;

    @Column(name = "CODE_STATUT_FACTURE")
    String codeStatutFacture;

    @Column(name = "CODE_MODE_PAIEMENT")
    String codeModePaiement;

    @Column(name = "DATE_PAIEMENT")
    @Temporal(TemporalType.DATE)
    Date datePaiement;

    @Column(name = "ID_OPERATEUR")
    String idOperateur;

    @JsonIgnore
    @OneToMany(mappedBy = "facture", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    Set<DetailReservation> detailReservations;

    @Transient
    String libelleStatut;

    @Transient
    String libelleModePaiement;

    @Column(name = "ID_PERSONNE_A_FACTURER")
    String idPersonneAFacturer;

    @Transient
    String idSession;

    @Transient
    String nomOperateur;

}
