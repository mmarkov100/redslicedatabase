package redslicedatabase.redslicedatabase.model;

/*
Сущность ветки чата, которая хранится в базе данных
 */

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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


    // Lombok не работает, поэтому геттеры и сеттеры вручную
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public Chat getChat(){
        return chat;
    }
    public void setChat(Chat chat){
        this.chat = chat;
    }
    public Branch getParentBranch(){
        return parentBranch;
    }
    public void setParentBranch(Branch parentBranch){
        this.parentBranch = parentBranch;
    }
    public Message getMessageStart(){
        return messageStart;
    }
    public void setMessageStart(Message messageStart){
        this.messageStart = messageStart;
    }
    public Boolean getIsRoot(){
        return isRoot;
    }
    public void setIsRoot(Boolean isRoot){
        this.isRoot = isRoot;
    }
    public LocalDateTime getDateEdit() {
        return dateEdit;
    }
    public void setDateEdit(LocalDateTime dateEdit) {
        this.dateEdit = dateEdit;
    }
    public LocalDateTime getDateCreate(){
        return dateCreate;
    }
    public void setDateCreate(LocalDateTime dateCreate){
        this.dateCreate = dateCreate;
    }
}


