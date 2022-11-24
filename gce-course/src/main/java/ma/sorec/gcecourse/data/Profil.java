package ma.sorec.gcecourse.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "GCE_PROFIL")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profil extends BaseEntite {

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

    //bi-directional many-to-one association to ProfilFonctionnalite
    @OneToMany(mappedBy="profil")
    @JsonIgnore
    List<ProfilFonctionnalite> profilFonctionnalites;

    //bi-directional many-to-one association to UtilisateurProfil
    @OneToMany(mappedBy="profil")
    @JsonIgnore
    List<UtilisateurProfil> utilisateurProfils;

    @Transient
    String idSession;

}
