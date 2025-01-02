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
import java.util.Optional;

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

    // Получение конкретной ветки. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable Long id) {

        // Получаем от сервиса данные
        Optional<Branch> branch = branchService.getBranchById(id);
        if (branch.isEmpty()) { // Если чат не найден, возвращаем 404 Not Found
            logger.warn("GET ID: Branch with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }

        // Преобразуем в DTO
        BranchDTO branchDTO = branchService.convertToDTO(branch.get());
        return ResponseEntity.ok(branchDTO);
    }

    // Получение конкретной ветки с валидацией пользователя. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @GetMapping("/{id}/validate")
    public ResponseEntity<BranchDTO> getBranchByIdAndUidFirebase(@PathVariable Long id,
                                                                 @RequestParam String uidFirebase) throws AccessDeniedException {

        Branch branch = branchService.getBranchByIdWithAccessCheck(id, uidFirebase);
        // Преобразуем в DTO и отправляем
        return ResponseEntity.ok(branchService.convertToDTO(branch));
    }

    // Получение всех веток чата. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @GetMapping("/chat/{id}")
    public ResponseEntity<List<BranchDTO>> getBranchByChatId(@PathVariable Long id) {
        logger.info("GET ID: Got chat id: {}", id);
        List<Branch> branches = branchService.getBranchesByChatId(id);
        List<BranchDTO> branchDTOs = branchService.convertToDTO(branches);
        return branchDTOs.isEmpty() // Проверка, пустой ли массив или нет
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(branchDTOs);
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

    // Получение всех веток пользователя. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @GetMapping("/user/{id}")
    public ResponseEntity<List<BranchDTO>> getBranchesByUserId(@PathVariable Long id) {
        List<Branch> branches = branchService.getBranchesByUserId(id);
        List<BranchDTO> branchDTOs = branchService.convertToDTO(branches);
        return branchDTOs.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(branchDTOs);
    }

    // Получение всех веток пользователя с валидацией. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @GetMapping("/user/uid/{uidFirebase}")
    public ResponseEntity<List<BranchDTO>> getBranchesByUID(@PathVariable String uidFirebase) {
        List<Branch> branches = branchService.getBranchesByUserUidFirebase(uidFirebase);
        List<BranchDTO> branchDTOs = branchService.convertToDTO(branches);
        return branchDTOs.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(branchDTOs);
    }

    // Получение всех веток. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @GetMapping
    public ResponseEntity<List<BranchDTO>> getAllBranches() {

        // Получаем все ветки
        List<Branch> branches = branchService.getBranches();

        // Конвертируем в DTO
        List<BranchDTO> branchDTOs = branchService.convertToDTO(branches);

        return branchDTOs.isEmpty() // Проверка, пустой ли массив или нет
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(branchDTOs);
    }

    // Получить дочерние ветки от ветки. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @GetMapping("/{id}/children")
    public ResponseEntity<List<BranchDTO>> getChildBranches(@PathVariable Long id) {

        // Получаем все ветки
        List<Branch> childBranches = branchService.getChildBranches(id);

        // Конвертируем в DTO
        List<BranchDTO> branchDTOs = branchService.convertToDTO(childBranches);

        return branchDTOs.isEmpty() // Проверка, пустой ли массив или нет
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(branchDTOs);
    }

    // Каскадное удаление ветки. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranchByIdAndFirebase(@PathVariable Long id) {
        branchService.deleteBranchById(id);
        return ResponseEntity.noContent().build(); // Возвращает 204 No Content
    }

    // Каскадное удаление ветки с проверкой доступа
    @DeleteMapping("/{id}/validate")
    public ResponseEntity<Void> deleteBranchById(@PathVariable Long id,
                                                 @RequestParam String uidFirebase) throws AccessDeniedException {
        branchService.deleteBranchByIdWithAccessCheck(id, uidFirebase);
        return ResponseEntity.noContent().build(); // Возвращает 204 No Content
    }
}
