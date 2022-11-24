package ma.sorec.gcecourse.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "GCE_UTILISATEUR")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Utilisateur extends BaseEntite {

    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "seq_gce_id")
    @SequenceGenerator (name = "seq_gce_id", sequenceName = "SEQ_GCE_ID", allocationSize = 1)
    @Column(name = "ID")
    Long id;

    @Column(name="CODE_ETAT")
    String codeEtat;

    @Column(name = "LOGIN")
    String login;

    @Column(name = "MOT_DE_PASSE")
    String motDePasse;

    @OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ID_PERSONNE")
    Personne personne;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "ID_GROUPE_UTILISATEUR", nullable = false)
//    GroupeUtilisateur groupeUtilisateur;

    @OneToMany(mappedBy="utilisateur")
    @LazyCollection(LazyCollectionOption.FALSE)
    List<UtilisateurProfil> utilisateurProfils;

    //bi-directional many-to-one association to UtilisateurSession
    @OneToMany(mappedBy="utilisateur")
    @JsonIgnore
    List<UtilisateurSession> utilisateurSessions;

    @Column
    String codeTypeUtilisateur;

    @Transient
    @JsonIgnore
    Boolean alreadyConnected = false;

    @Transient
    String idProfil;

    @Transient
    String nomProfil;

    @Transient
    String idSite;

    @Transient
    Site site;

    @Transient
    Profil profil;

    @Transient
    String idSession;

}
