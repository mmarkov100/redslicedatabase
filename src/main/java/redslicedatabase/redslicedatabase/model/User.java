package redslicedatabase.redslicedatabase.model;

/*
Сущность пользователя, которая хранится в базе данных
Не хранит данные о пароле, для этого есть firebase_auth
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
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
}
