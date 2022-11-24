package ma.sorec.gcecourse.data;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "GCE_TYPE_PERSONNE")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypePersonne implements Serializable {

    @Id
    @Column(name = "CODE")
    String code;

    @Column(name = "DESIGNATION")
    String designation;

    @Transient
    String idSession;
}
