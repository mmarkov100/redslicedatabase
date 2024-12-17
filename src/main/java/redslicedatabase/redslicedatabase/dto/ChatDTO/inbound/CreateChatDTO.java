package redslicedatabase.redslicedatabase.dto.ChatDTO.inbound;

/*
DTO класс для получения данных об создании чата
 */

import jakarta.validation.constraints.Size;

public class CreateChatDTO {

    private Long userId;
    @Size(max = 255, message = "Chat name cannot exceed 255 characters")
    private String chatName;
    @Size(max = 1, message = "Temperature can between 0 and 1")
    private Double temperature;
    @Size(max = 10000, message = "Context cannot exceed 10000 characters")
    private String context;
    @Size(max = 255, message = "Model URI cannot exceed 255 characters")
    private String modelUri;




    // Геттеры и сеттеры
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
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
}
