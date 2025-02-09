package redslicedatabase.redslicedatabase.model;

/*
Сущность чата, который хранится в базе данных
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Пользователь, к которому и относится чат

    @Column(nullable = false)
    private String chatName; // Название чата

    private Double temperature; // Креативность ответа, от 0 до 1

    @Column(length = 10000)
    private String context; // Окружение чата, задать правила как должна отвечать нейросеть. Максимум 10к символов

    @Column(nullable = false)
    private String modelUri; // Используемая нейросеть в чате

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_branch_id")
    private Branch selectedBranch; // Выбранная текущая ветка в чате.

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Branch> branches; // Список веток, связанных с чатом

    private LocalDateTime dateEdit = LocalDateTime.now(); // Дата изменения чата, изменяется при создании новой ветки или изменении ветки

    private LocalDateTime dateCreate = LocalDateTime.now(); // Дата создания чата

}

