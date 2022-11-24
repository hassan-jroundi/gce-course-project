package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GCE_GROUPE_UTILISATEUR")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupeUtilisateur implements Serializable {

    @Id
    @Column(name = "CODE")
    String code;

    @Column(name = "LIBELLE")
    String libelle;

    @Transient
    String idSession;
}
