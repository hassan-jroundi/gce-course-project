package ma.sorec.gcecourse.data.mohr;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PERSONNE_CHEVAL_MOHR")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonneChevalM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDENTIFIANT")
    String id;

    @Column(name = "ID_CHEVAL")
    String idCheval;

    @Column(name = "ID_PERSONNE")
    String idPersonne;

    @Column(name = "CODE_NATURE_RELATION")
    String codeNatureRelation;

    @Column(name = "DATE_DEBUT")
    Date dateDebut;

    @Column(name = "DATE_FIN")
    Date dateFin;

}
