package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationDTO {

    Long id;
    String description;
    Date dateEnvoi;
    String type;
    ChevalDTO cheval;
    List<ProfilDTO> profils;
    Boolean dejaLu;
    String idSession;
}
