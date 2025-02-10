package redslicedatabase.redslicedatabase.controller;

/*
Контроллер ветки чата.
Пока реализовано только создание ветки и посмотреть ветку по id, чату и пользователю
 */

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redslicedatabase.redslicedatabase.dto.BranchDTO.inbound.CreateBranchDTO;
import redslicedatabase.redslicedatabase.dto.BranchDTO.outbound.BranchDTO;
import redslicedatabase.redslicedatabase.model.Branch;
import redslicedatabase.redslicedatabase.service.BranchService;
import redslicedatabase.redslicedatabase.service.ChatService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/branches")
public class BranchController {

    private static final Logger logger = LoggerFactory.getLogger(BranchController.class);

    @Autowired
    private BranchService branchService;
    @Autowired
    private ChatService chatService;

    // Создать новую ветку
    @PostMapping
    public ResponseEntity<BranchDTO> createBranch(@Valid @RequestBody CreateBranchDTO createBranchDTO) {

        Branch createdBranch = branchService.createBranch(createBranchDTO);
        return ResponseEntity.ok(branchService.convertToDTO(createdBranch));
    }

    // Получение всех веток чата с валидацией пользователя
    @GetMapping("/chat/{chatId}/validate")
    public ResponseEntity<List<BranchDTO>> getBranchByChatIdAndFirebase(@PathVariable Long chatId,
                                                                        @RequestParam String uidFirebase) throws AccessDeniedException {
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
    public ResponseEntity<Void> deleteBranchById(@PathVariable Long id,
                                                 @RequestParam String uidFirebase) throws AccessDeniedException {
        branchService.deleteBranchByIdWithAccessCheck(id, uidFirebase);
        return ResponseEntity.noContent().build(); // Возвращает 204 No Content
    }
}
