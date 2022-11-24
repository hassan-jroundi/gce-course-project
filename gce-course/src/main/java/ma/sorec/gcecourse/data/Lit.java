package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "GCE_LIT")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Lit extends BaseEntite {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator (name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "NOM")
    String nom;

    @Column(name = "ORDRE")
    Long ordre;

    @ManyToOne
    @JoinColumn(name = "ID_CHAMBRE", nullable = false)
    Chambre chambre;

    @Column(name = "IS_ACTIF")
    Boolean isActif;

    @Transient
    Long prixLit;

    @Transient
    String idSession;
}
