package redslicedatabase.redslicedatabase.dto.MessageDTO.inbound;

/*
DTO для получения пары сообщений, от юзера и нейросети и сохранения их в бд
 */

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Data
public class CreateMessagePairDTO {
    private Long branchId; // ID ветки, в которой сохраняются сообщения
    private List<CreateMessageDTO> messages; // Два сообщения: от пользователя и от нейросети


}
