package redslicedatabase.redslicedatabase.model;

/*
Сущность пользователя, которая хранится в базе данных
Не хранит данные о пароле, для этого есть firebase_auth
 */

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "app_user") // Изменение имени таблицы, тк user зарезервировано Posgtgres'ом
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email; // Почта пользователя, уникальная для каждого пользователя

    @Column(unique = true, nullable = false)
    private String uidFirebase; // Уникальный id с firebas'а, уникальный для каждого пользователя

    private Integer totalTokens; // Максимально доступное количество токенов

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "starred_chat_id")
    private Chat starredChat; // Закрепленный чат пользователя. Не создается при создании аккаунта

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //
    private List<Chat> chats; // Список чатов пользователя

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //
    private List<Branch> branches; // Список веток пользователя

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true) //
    private List<Message> messages; // Список сообщений пользователя

    private LocalDateTime dateCreate = LocalDateTime.now(); // Дата создания пользователя






    // Lombok не работает, не знаю почему, поэтому так
    public Long getId() {
        return id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getUidFirebase() {
        return uidFirebase;
    }
    public void setUidFirebase(String uidFirebase) {
        this.uidFirebase = uidFirebase;
    }
    public Integer getTotalTokens() {
        return totalTokens;
    }
    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }
    public Chat getStarredChat() {
        return starredChat;
    }
    public void setStarredChat(Chat starredChat) {
        this.starredChat = starredChat;
    }
    public LocalDateTime getDateCreate() {
        return dateCreate;
    }
    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }
}
