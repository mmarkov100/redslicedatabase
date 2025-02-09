package redslicedatabase.redslicedatabase.dto.MessageDTO.inbound;

/*
DTO для создания нового сообщения
 */

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
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
    private String usedModel;
}
