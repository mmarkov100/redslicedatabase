package redslicedatabase.redslicedatabase.dto.BranchDTO.inbound;

/*
DTO класс для получения данных о создании новой ветки от клиента
 */

public class CreateBranchDTO {

    private Long chatId;
    private Long parentBranchId;
    private Long messageStartId;





    // Геттеры и сеттеры
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
