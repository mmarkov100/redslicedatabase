package redslicedatabase.redslicedatabase.dto.BranchDTO.outbound;

/*
DTO для отправки ответа клиенту
 */

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
public class BranchDTO {
    private Long id;
    private Long userId;
    private Long chatId;
    private Long parentBranchId;
    private Long messageStartId;
    private Boolean isRoot;
    private LocalDateTime dateEdit;
    private LocalDateTime dateCreate;

    // Конструктор
    public BranchDTO(Long id,
                     Long userId,
                     Long chatId,
                     Long parentBranchId,
                     Long messageStartId,
                     Boolean isRoot,
                     LocalDateTime dateEdit,
                     LocalDateTime dateCreate
    ) {
        this.id = id;
        this.userId = userId;
        this.chatId = chatId;
        this.parentBranchId = parentBranchId;
        this.messageStartId = messageStartId;
        this.isRoot = isRoot;
        this.dateEdit = dateEdit;
        this.dateCreate = dateCreate;
    }

}

