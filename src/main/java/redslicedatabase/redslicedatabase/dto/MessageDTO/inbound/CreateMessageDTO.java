package redslicedatabase.redslicedatabase.dto.MessageDTO.inbound;

/*
DTO для создания нового сообщения
 */

import jakarta.validation.constraints.NotNull;

public class CreateMessageDTO {
    @NotNull(message = "UID Firebase cannot be null")
    private String uidFirebase;
    @NotNull(message = "Branch cannot be null")
    private Long branchId;
    private String role;
    private String text;
    private Integer totalTokens;
    private Integer inputTokens;
    private Integer completionTokens;





    // Геттеры и сеттеры
    public String getUidFirebase() {
        return uidFirebase;
    }
    public void setUidFirebase() {
        this.uidFirebase = uidFirebase;
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
}
