package redslicedatabase.redslicedatabase.dto.BranchDTO.inbound;

/*
DTO класс для получения данных о создании новой ветки от клиента
 */

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class CreateBranchDTO {

    // Геттеры и сеттеры
    @NotNull(message = "UID Firebase cannot be null")
    private String uidFirebase;
    @NotNull(message = "Chat ID cannot be null")
    private Long chatId;
    private Long parentBranchId;
    private Long messageStartId;


}
