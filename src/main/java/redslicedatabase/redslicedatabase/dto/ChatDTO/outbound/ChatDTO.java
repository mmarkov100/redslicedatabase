package redslicedatabase.redslicedatabase.dto.ChatDTO.outbound;

/*
DTO для отправки ответа пользователю, исключает подробный список показа пользователя и выбранной ветки
 */

import java.time.LocalDateTime;

public class ChatDTO {
    private Long id;
    private Long userId;
    private String chatName;
    private Double temperature;
    private String context;
    private String modelUri;
    private Long selectedBranchId;
    private LocalDateTime dateEdit;
    private LocalDateTime dateCreate;

    // Конструктор
    public ChatDTO(Long id,
                   Long userId,
                   String chatName,
                   Double temperature,
                   String context,
                   String modelUri,
                   Long selectedBranchId,
                   LocalDateTime dateEdit,
                   LocalDateTime dateCreate) {
        this.id = id;
        this.userId = userId;
        this.chatName = chatName;
        this.temperature = temperature;
        this.context = context;
        this.modelUri = modelUri;
        this.selectedBranchId = selectedBranchId;
        this.dateEdit = dateEdit;
        this.dateCreate = dateCreate;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getChatName() { return chatName; }
    public Double getTemperature() { return temperature; }
    public String getContext() { return context; }
    public String getModelUri() { return modelUri; }
    public Long getSelectedBranchId() { return selectedBranchId; }
    public LocalDateTime getDateEdit() { return dateEdit; }
    public LocalDateTime getDateCreate() { return dateCreate; }
}
