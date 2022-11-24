package ma.sorec.gcecourse.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@MappedSuperclass
@SuperBuilder
public class BaseEntite implements Serializable {

    @Column(name = "USER_CREATION", updatable = false)
    @JsonIgnore
    Long userCreation;

    @Column(name = "DATE_CREATION", updatable = false)
    @Temporal(TemporalType.DATE)
    @JsonIgnore
    Date dateCreation;

    @Column(name = "USER_MODIFICATION")
    @JsonIgnore
    Long userModification;

    @Column(name = "DATE_MODIFICATION")
    @Temporal(TemporalType.DATE)
    @JsonIgnore
    Date dateModification;
}
