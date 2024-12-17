package redslicedatabase.redslicedatabase.dto.UserDTO.outbound;

/*
DTO класс для отправки пользователю сохраненных данных
 */

import java.time.LocalDateTime;

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

    // Геттеры
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getUidFirebase() { return uidFirebase; }
    public Integer getTotalTokens() { return totalTokens; }
    public Long getStarredChatId() { return starredChatId; }
    public LocalDateTime getDateCreate() { return dateCreate; }
}
