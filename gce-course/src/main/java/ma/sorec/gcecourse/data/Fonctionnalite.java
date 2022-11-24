package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "GCE_FONCTIONNALITE")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Fonctionnalite extends BaseEntite {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator (name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "ABREVIATION")
    String abreviation;

    @Column(name = "CODE")
    String code;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "DESIGNATION")
    String designation;

    @Column(name = "ORDRE")
    BigDecimal ordre;

    @Column(name = "URL")
    String url;

    //bi-directional many-to-one association to ProfilFonctionnalite
    @OneToMany(mappedBy="fonctionnalite")
    List<ProfilFonctionnalite> profilFonctionnalites;

    @Column(name="URL_ICON")
    String urlIcon;

    @Column(name="DESIGNATION_MENU")
    String designationMenu;

    @Transient
    String idSession;
}
