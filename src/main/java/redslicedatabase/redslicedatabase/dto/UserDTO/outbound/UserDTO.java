package redslicedatabase.redslicedatabase.dto.UserDTO.outbound;

/*
DTO класс для отправки пользователю сохраненных данных
 */

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class UserDTO {
    private Long id;
    private String email;
    private String uidFirebase;
    private Integer totalTokens;
    private Long starredChatId;
    private LocalDateTime dateCreate;

    public UserDTO(
            Long id,
            String email,
            String uidFirebase,
            Integer totalTokens,
            Long starredChatId,
            LocalDateTime dateCreate) {
        this.id = id;
        this.email = email;
        this.uidFirebase = uidFirebase;
        this.totalTokens = totalTokens;
        this.starredChatId = starredChatId;
        this.dateCreate = dateCreate;
    }

}
