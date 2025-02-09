package redslicedatabase.redslicedatabase.model;

/*
Сущность ветки чата, которая хранится в базе данных
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Пользователь, к которому и относится ветка

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat; // Чат, к которому принадлежит ветка

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_branch_id")
    private Branch parentBranch; // Родительская ветка

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_start_id")
    private Message messageStart; // Сообщение, с которого началась ветка по id

    @Column(nullable = false)
    private Boolean isRoot = false; // Признак корневой ветки

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages; // Список сообщений в ветке

    @OneToMany(mappedBy = "parentBranch", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Branch> childBranches; // Список дочерних веток

    private LocalDateTime dateEdit = LocalDateTime.now(); // Дата изменения ветки

    private LocalDateTime dateCreate = LocalDateTime.now(); // Дата создания ветки
}


