package ma.sorec.gcecourse.data.mohr;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STATUT_CHEVAL_MOHR")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StatutChevalM {

  @Id
  @Column(name = "CODE")
  String code;

  @Column(name = "DESIGNATION")
  String designation;

}
