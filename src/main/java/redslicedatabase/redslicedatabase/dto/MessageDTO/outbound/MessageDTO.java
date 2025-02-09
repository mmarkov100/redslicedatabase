package redslicedatabase.redslicedatabase.dto.MessageDTO.outbound;

/*
DTO для отправки сохраненного результата ответа пользователя
 */

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Data
public class MessageDTO {
    private Long id;
    private Long userId;
    private Long branchId;
    private String role;
    private String text;
    private Integer totalTokens;
    private Integer inputTokens;
    private Integer completionTokens;
    private String usedModel;
    private LocalDateTime dateCreate;

    public MessageDTO(Long id,
                      Long userId,
                      Long branchId,
                      String role,
                      String text,
                      Integer totalTokens,
                      Integer inputTokens,
                      Integer completionTokens,
                      LocalDateTime dateCreate,
                      String usedModel
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
        this.usedModel = usedModel;
    }


}
