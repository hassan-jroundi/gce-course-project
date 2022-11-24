package ma.sorec.gcecourse.data.mohr;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "DET_DECLA_EFF_MOHR")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetailDeclarationEffectif {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDENTIFIANT")
    String identifiant;

    @Column(name = "ID_DECLARATION_EFFECTIF")
    String idDeclarationEffectif;

    @Column(name = "CODE_STATUT_CHEVAL")
    String codeStatutCheval;

    @Column(name = "ID_CHEVAL")
    Long idCheval;

    @Column(name = "ID_PROPRIETAIRE")
    String idProprietaire;

    @Column(name = "DATE_CREATION")
    @Temporal(TemporalType.DATE)
    Date dateCreation;

}
