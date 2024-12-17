package redslicedatabase.redslicedatabase.dto.UserDTO.inbound;

/*
DTO класс для получения данных от пользователя об обновлении чата
 */

public class UpdateUserDTO {

    private Integer totalTokens;
    private Long starredChatId;


    //Геттеры и сеттеры
    public Integer getTotalTokens() {
        return totalTokens;
    }
    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }
    public Long getStarredChatId() {
        return starredChatId;
    }
    public void setStarredChatId(Long starredChatId) {
        this.starredChatId = starredChatId;
    }
}
