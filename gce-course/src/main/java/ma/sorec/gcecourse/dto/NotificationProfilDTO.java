package ma.sorec.gcecourse.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationProfilDTO {

    Long id;
    ProfilDTO profil;
    NotificationDTO notification;
    String idSession;

}
