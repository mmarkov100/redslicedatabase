package redslicedatabase.redslicedatabase.dto.MessageDTO.outbound;

/*
DTO для отправки сохраненного результата ответа пользователя
 */

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private Long userId;
    private Long branchId;
    private String role;
    private String text;
    private Integer totalTokens;
    private Integer inputTokens;
    private Integer completionTokens;
    private LocalDateTime dateCreate;

    public MessageDTO(Long id,
                      Long userId,
                      Long branchId,
                      String role,
                      String text,
                      Integer totalTokens,
                      Integer inputTokens,
                      Integer completionTokens,
                      LocalDateTime dateCreate
                      ) {
        this.id = id;
        this.userId = userId;
        this.branchId = branchId;
        this.role = role;
        this.text = text;
        this.totalTokens = totalTokens;
        this.inputTokens = inputTokens;
        this.completionTokens = completionTokens;
        this.dateCreate = dateCreate;
    }




    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getBranchId() {
        return branchId;
    }
    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public Integer getTotalTokens() {
        return totalTokens;
    }
    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }
    public Integer getInputTokens() {
        return inputTokens;
    }
    public void setInputTokens(Integer inputTokens) {
        this.inputTokens = inputTokens;
    }
    public Integer getCompletionTokens() {
        return completionTokens;
    }
    public void setCompletionTokens(Integer completionTokens) {
        this.completionTokens = completionTokens;
    }
    public LocalDateTime getDateCreate() {
        return dateCreate;
    }
    public void setDateCreate(LocalDateTime dateCreate) {
        this.dateCreate = dateCreate;
    }
}
