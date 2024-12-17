package redslicedatabase.redslicedatabase.dto.BranchDTO.outbound;

/*
DTO для отправки ответа клиенту
 */

import java.time.LocalDateTime;

public class BranchDTO {
    private Long id;
    private Long chatId;
    private Long parentBranchId;
    private Long messageStartId;
    private Boolean isRoot;
    private LocalDateTime dateEdit;
    private LocalDateTime dateCreate;

    // Конструктор
    public BranchDTO(Long id,
                     Long chatId,
                     Long parentBranchId,
                     Long messageStartId,
                     Boolean isRoot,
                     LocalDateTime dateEdit,
                     LocalDateTime dateCreate
    ) {
        this.id = id;
        this.chatId = chatId;
        this.parentBranchId = parentBranchId;
        this.messageStartId = messageStartId;
        this.isRoot = isRoot;
        this.dateEdit = dateEdit;
        this.dateCreate = dateCreate;
    }

    // Геттеры
    public Long getId() { return id; }
    public Long getChatId() { return chatId; }
    public Long getParentBranchId() { return parentBranchId; }
    public Long getMessageStartId() { return messageStartId; }
    public Boolean getIsRoot() { return isRoot; }
    public LocalDateTime getDateEdit() { return dateEdit; }
    public LocalDateTime getDateCreate() { return dateCreate; }
}

