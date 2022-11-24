package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "GCE_CHAMBRE")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Chambre extends BaseEntite {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator (name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "NOM")
    String nom;

    @Column(name = "NUMERO")
    String numero;

    @Column(name = "ETAGE")
    String etage;

    @ManyToOne
    @JoinColumn(name = "ID_IMMEUBLE", nullable = false)
    Immeuble immeuble;

    @Column(name = "IS_ACTIF")
    Boolean isActif;

    @Transient
    Long prixLit;

    @Transient
    String prixUnitaire;

    @Transient
    String prixForfaitaire;

    @Transient
    Long nombreLits;

    @Transient
    String idSession;
}
