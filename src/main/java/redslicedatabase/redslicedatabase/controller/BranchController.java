package redslicedatabase.redslicedatabase.controller;

/*
Контроллер ветки чата.
Пока реализовано только создание ветки и посмотреть ветку по id, чату и пользователю
 */

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redslicedatabase.redslicedatabase.config.AppConfig;
import redslicedatabase.redslicedatabase.dto.BranchDTO.inbound.CreateBranchDTO;
import redslicedatabase.redslicedatabase.dto.BranchDTO.outbound.BranchDTO;
import redslicedatabase.redslicedatabase.model.Branch;
import redslicedatabase.redslicedatabase.service.BranchService;
import redslicedatabase.redslicedatabase.service.ChatService;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/branches")
public class BranchController {

    private static final Logger logger = LoggerFactory.getLogger(BranchController.class);

    @Autowired
    private BranchService branchService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private AppConfig appConfig;

    // Создать новую ветку
    @PostMapping
    public ResponseEntity<?> createBranch(@Valid @RequestBody CreateBranchDTO createBranchDTO,
                                                  @RequestHeader String apiDBKey) {
        if (!Objects.equals(apiDBKey, appConfig.getApiDatabaseKey())) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Wrong apiDBKey"
                    ));
        }

        Branch createdBranch = branchService.createBranch(createBranchDTO);
        return ResponseEntity.ok(branchService.convertToDTO(createdBranch));
    }

    // Получение всех веток чата с валидацией пользователя
    @GetMapping("/chat/{chatId}/validate")
    public ResponseEntity<?> getBranchByChatIdAndFirebase(@PathVariable Long chatId,
                                                                        @RequestParam String uidFirebase,
                                                                        @RequestHeader String apiDBKey) throws AccessDeniedException {
        if (!Objects.equals(apiDBKey, appConfig.getApiDatabaseKey())) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Wrong apiDBKey"
                    ));
        }

        chatService.getChatByIdWithAccessCheck(chatId, uidFirebase); // Проверка на доступность чата пользователю

        logger.info("GET UID: Got chat id: {}", chatId);
        List<Branch> branches = branchService.getBranchesByChatId(chatId);
        List<BranchDTO> branchDTOs = branchService.convertToDTO(branches);
        return branchDTOs.isEmpty() // Проверка, пустой ли массив или нет
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(branchDTOs);
    }

    // Каскадное удаление ветки с проверкой доступа
    @DeleteMapping("/{id}/validate")
    public ResponseEntity<?> deleteBranchById(@PathVariable Long id,
                                                 @RequestParam String uidFirebase,
                                                 @RequestHeader String apiDBKey) throws AccessDeniedException {
        if (!Objects.equals(apiDBKey, appConfig.getApiDatabaseKey())) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Wrong apiDBKey"
                    ));
        }

        branchService.deleteBranchByIdWithAccessCheck(id, uidFirebase);
        return ResponseEntity.noContent().build(); // Возвращает 204 No Content
    }
}
