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
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    private LocalDateTime dateCreate = LocalDateTime.now(); // Дата создания сообщения






    // Lombok не работает, не знаю почему, поэтому так
    public Long getId(){
        return id;
    }
    public void setId(Long id){

        this.id = id;
    }
    public Branch getBranch(){
        return branch;
    }
    public void setBranch(Branch branch){
        this.branch = branch;
    }
    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role = role;
    }
    public String getText(){
        return text;
    }
    public void setText(String text){
        this.text = text;
    }
    public Integer getTotalTokens(){
        return totalTokens;
    }
    public void setTotalTokens(Integer totalTokens){
        this.totalTokens = this.totalTokens;
    }
    public Integer getInputTokens(){
        return inputTokens;
    }
    public void setInputTokens(Integer inputTokens){
        this.inputTokens = this.inputTokens;
    }
    public Integer getCompletionTokens(){
        return completionTokens;
    }
    public void setCompletionTokens(Integer completionTokens){
        this.completionTokens = this.completionTokens;
    }
    public LocalDateTime getDateCreate(){
        return dateCreate;
    }
    public void setDateCreate(LocalDateTime dateCreate){
        this.dateCreate = dateCreate;
    }
}

