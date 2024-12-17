package redslicedatabase.redslicedatabase.dto.MessageDTO.inbound;

/*
DTO для получения пары сообщений, от юзера и нейросети и сохранения их в бд
 */

import java.util.List;

public class CreateMessagePairDTO {
    private Long branchId; // ID ветки, в которой сохраняются сообщения
    private List<CreateMessageDTO> messages; // Два сообщения: от пользователя и от нейросети




    // Геттеры и сеттеры
    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public List<CreateMessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<CreateMessageDTO> messages) {
        this.messages = messages;
    }
}
