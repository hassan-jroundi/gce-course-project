package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "GCE_FONCTIONNALITE_ACTION")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FonctionnaliteAction extends BaseEntite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator(name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
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

    //bi-directional many-to-one association to Fonctionnalite
    @ManyToOne
    @JoinColumn(name = "ID_FONCTIONNALITE")
    Fonctionnalite fonctionnalite;

    @Transient
    String idSession;
}
