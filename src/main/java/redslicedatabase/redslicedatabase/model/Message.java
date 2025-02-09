package redslicedatabase.redslicedatabase.model;

/*
Сущность сообщения, которое хранится в базе данных

Пример HTTP POST запроса
POST /messages
{
  "branch": {
    "id": 2
  },
  "role": "user",
  "text": "Hello, this is the first message in the branch!",
  "totalTokens": 50,
  "inputTokens": 30,
  "completionTokens": 20
}
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Message {

    // Lombok не работает, не знаю почему, поэтому так
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Пользователь, к которому и относится сообщение

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch; // Ветка, к которой принадлежит сообщение

    @Column(nullable = false)
    private String role; // Роль: user, assistant

    @Column(length = 10000, nullable = false)
    private String text; // Текст сообщения

    private Integer totalTokens; // Количество токенов, использованных ботом (nullable) при создании сообщения

    private Integer inputTokens;

    private Integer completionTokens;

    private String usedModel;

    private LocalDateTime dateCreate = LocalDateTime.now(); // Дата создания сообщения

}

