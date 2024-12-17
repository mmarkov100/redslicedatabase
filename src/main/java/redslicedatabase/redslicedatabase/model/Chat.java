package redslicedatabase.redslicedatabase.model;

/*
Сущность чата, который хранится в базе данных
 */

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
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




    // Lombok не работает, не знаю почему, поэтому так
    public Long getId(){
        return id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getChatName() {
        return chatName;
    }
    public void setChatName(String chatName) {
        this.chatName = chatName;
    }
    public Double getTemperature() {
        return temperature;
    }
    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
    public String getContext() {
        return context;
    }
    public void setContext(String context) {
        this.context = context;
    }
    public String getModelUri() {
        return modelUri;
    }
    public void setModelUri(String modelUri) {
        this.modelUri = modelUri;
    }
    public Branch getSelectedBranch() {
        return selectedBranch;
    }
    public void setSelectedBranch(Branch selectedBranch) {
        this.selectedBranch = selectedBranch;
    }
    public LocalDateTime getDateEdit() {
        return dateEdit;
    }
    public void setDateEdit(LocalDateTime dateEdit) {
        this.dateEdit = dateEdit;
    }
    public LocalDateTime getDateCreate() {
        return dateCreate;
    }
    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }
}

