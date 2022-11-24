package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "GCE_IMMEUBLE")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Immeuble extends BaseEntite {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator (name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "NOM")
    String nom;

    @Column(name = "NUMERO")
    String numero;

    @ManyToOne
    @JoinColumn(name = "ID_SITE", nullable = false)
    Site site;

    @Column(name = "IS_ACTIF")
    Boolean isActif;

    @Transient
    String idSession;
}
