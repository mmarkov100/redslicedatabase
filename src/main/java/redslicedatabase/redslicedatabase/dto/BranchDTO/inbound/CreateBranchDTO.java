package redslicedatabase.redslicedatabase.dto.BranchDTO.inbound;

/*
DTO класс для получения данных о создании новой ветки от клиента
 */

import jakarta.validation.constraints.NotNull;

public class CreateBranchDTO {

    @NotNull(message = "UID Firebase cannot be null")
    private String uidFirebase;
    @NotNull(message = "Chat ID cannot be null")
    private Long chatId;
    private Long parentBranchId;
    private Long messageStartId;





    // Геттеры и сеттеры
    public void setUidFirebase(String uidFirebase) {
        this.uidFirebase = uidFirebase;
    }
    public String getUidFirebase() {
        return uidFirebase;
    }
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
    public Long getChatId() {
        return chatId;
    }
    public void setParentBranchId(Long parentBranchId) {
        this.parentBranchId = parentBranchId;
    }
    public Long getParentBranchId() {
        return parentBranchId;
    }
    public void setMessageStartId(Long messageStartId) {
        this.messageStartId = messageStartId;
    }
    public Long getMessageStartId() {
        return messageStartId;
    }
}
