package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "GCE_PISTE")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Piste extends BaseEntite {

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
