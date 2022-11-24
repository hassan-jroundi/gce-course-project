package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GCE_TYPE_UTILISATEUR")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypeUtilisateur implements Serializable {

    @Id
    @Column(name = "CODE")
    String code;

    @Column(name = "DESIGNATION")
    String designation;

    @Transient
    String idSession;
}
