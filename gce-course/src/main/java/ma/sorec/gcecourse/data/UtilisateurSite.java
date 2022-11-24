package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "GCE_UTILISATEUR_SITE")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UtilisateurSite extends BaseEntite {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator (name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name="DATE_DEBUT")
    @Temporal(TemporalType.DATE)
    Date dateDebut;

    @Column(name="DATE_FIN")
    @Temporal(TemporalType.DATE)
    Date dateFin;

    @ManyToOne
    @JoinColumn(name="ID_UTILISATEUR")
    Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name="ID_SITE")
    Site site;

    @Transient
    String idSession;

}
