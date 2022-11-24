package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "GCE_NOTIFICATION")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification implements Serializable {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator (name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "DATE_ENVOI")
    @Temporal(TemporalType.DATE)
    Date dateEnvoi;

    @Column(name = "TYPE")
    String type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_CHEVAL", nullable = false)
    Cheval cheval;

    @Transient
    List<Profil> profils;

    @Transient
    Boolean dejaLu;

    @Transient
    String idSession;

}
