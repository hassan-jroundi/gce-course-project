package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "GCE_RELATION_LAD")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RelationLAD extends BaseEntite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator(name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "ID_LAD")
    String idLAD;

    @Column(name = "ID_EMPLOYEUR")
    String idEmployeur;

    @Column(name = "DATE_DEBUT_CONTRAT")
    Date dateDebutContrat;

    @Column(name = "DATE_FIN_CONTRAT")
    Date dateFinContrat;

    @Transient
    String idSession;
}
