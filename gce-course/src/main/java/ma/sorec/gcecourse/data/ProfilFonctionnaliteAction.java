package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "GCE_PROFIL_FONCT_ACTION")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfilFonctionnaliteAction extends BaseEntite {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator (name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name="ID_FONCTIONNALITE_ACTION")
    BigDecimal idFonctionnaliteAction;

    //bi-directional many-to-one association to ProfilFonctionnalite
    @ManyToOne
    @JoinColumn(name="ID_PROFIL_FONCTIONNALITE")
    ProfilFonctionnalite profilFonctionnalite;

    @Transient
    String idSession;

}
