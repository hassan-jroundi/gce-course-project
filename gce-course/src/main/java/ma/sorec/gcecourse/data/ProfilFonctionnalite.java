package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "GCE_PROFIL_FONCTIONNALITE")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfilFonctionnalite extends BaseEntite {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator (name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    //bi-directional many-to-one association to Fonctionnalite
    @ManyToOne
    @JoinColumn(name="ID_FONCTIONNALITE")
    Fonctionnalite fonctionnalite;

    //bi-directional many-to-one association to Profil
    @ManyToOne
    @JoinColumn(name="ID_PROFIL")
    Profil profil;

    //bi-directional many-to-one association to ProfilFonctionnaliteAction
    @OneToMany(mappedBy="profilFonctionnalite")
    List<ProfilFonctionnaliteAction> profilFonctionnaliteActions;

    @Transient
    String idSession;

}
