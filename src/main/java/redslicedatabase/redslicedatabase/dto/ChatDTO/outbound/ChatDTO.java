package redslicedatabase.redslicedatabase.dto.ChatDTO.outbound;

/*
DTO для отправки ответа пользователю, исключает подробный список показа пользователя и выбранной ветки
 */

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class ChatDTO {
    private Long id;
    private Long userId;
    private String chatName;
    private Double temperature;
    private String context;
    private String modelUri;
    private Long selectedBranchId;
    private LocalDateTime dateEdit;
    private LocalDateTime dateCreate;

    // Конструктор
    public ChatDTO(Long id,
                   Long userId,
                   String chatName,
                   Double temperature,
                   String context,
                   String modelUri,
                   Long selectedBranchId,
                   LocalDateTime dateEdit,
                   LocalDateTime dateCreate) {
        this.id = id;
        this.userId = userId;
        this.chatName = chatName;
        this.temperature = temperature;
        this.context = context;
        this.modelUri = modelUri;
        this.selectedBranchId = selectedBranchId;
        this.dateEdit = dateEdit;
        this.dateCreate = dateCreate;
    }

}
