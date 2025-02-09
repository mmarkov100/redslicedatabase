package redslicedatabase.redslicedatabase.dto.ChatDTO.inbound;

/*
DTO класс для обновления настроек чата
 */

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Data
public class UpdateChatDTO {

    @NotNull(message = "UID Firebase cannot be null")
    private String uidFirebase;
    @Size(max = 255, message = "Chat name cannot exceed 255 characters")
    private String chatName;
    @DecimalMin(value = "0.0", message = "Temperature must be greater than or equal to 0")
    @DecimalMax(value = "1.0", message = "Temperature must be less than or equal to 1")
    private Double temperature;
    @Size(max = 10000, message = "Context cannot exceed 10000 characters")
    private String context;
    @Size(max = 255, message = "Model URI cannot exceed 255 characters")
    private String modelUri;
    private Long selectedBranchId;
}

