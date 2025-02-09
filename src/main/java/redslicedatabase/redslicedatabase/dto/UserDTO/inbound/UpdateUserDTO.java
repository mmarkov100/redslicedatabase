package redslicedatabase.redslicedatabase.dto.UserDTO.inbound;

/*
DTO класс для получения данных от пользователя об обновлении чата
 */

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserDTO {

    private Integer totalTokens;
    private Long starredChatId;
}
