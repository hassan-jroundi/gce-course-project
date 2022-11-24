package ma.sorec.gcecourse.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "GCE_UTILISATEUR_SESSION")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UtilisateurSession extends BaseEntite {

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

    @Column(name="ID_SESSION")
    String idSession;

    @ManyToOne
    @JoinColumn(name="ID_UTILISATEUR")
    Utilisateur utilisateur;

}
