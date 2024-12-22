package redslicedatabase.redslicedatabase.dto.ChatDTO.inbound;

/*
DTO класс для обновления настроек чата
 */

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class UpdateChatDTO {

    @NotNull(message = "UID Firebase cannot be null")
    private String uidFirebase;
    @Size(max = 255, message = "Chat name cannot exceed 255 characters")
    private String chatName;
    @DecimalMin(value = "0.0", inclusive = true, message = "Temperature must be greater than or equal to 0")
    @DecimalMax(value = "1.0", inclusive = true, message = "Temperature must be less than or equal to 1")
    private Double temperature;
    @Size(max = 10000, message = "Context cannot exceed 10000 characters")
    private String context;
    @Size(max = 255, message = "Model URI cannot exceed 255 characters")
    private String modelUri;
    private Long selectedBranchId;




    // Геттеры и сеттеры
    public String getUidFirebase() {return uidFirebase;}
    public void setUidFirebase(String uidFirebase) {this.uidFirebase = uidFirebase;}

    public String getChatName() { return chatName; }
    public void setChatName(String chatName) { this.chatName = chatName; }

    public Double getTemperature() { return temperature; }
    public void setTemperature(Double temperature) { this.temperature = temperature; }

    public String getContext() { return context; }
    public void setContext(String context) { this.context = context; }

    public String getModelUri() { return modelUri; }
    public void setModelUri(String modelUri) { this.modelUri = modelUri; }

    public Long getSelectedBranchId() { return selectedBranchId; }
    public void setSelectedBranchId(Long selectedBranchId) { this.selectedBranchId = selectedBranchId; }
}

