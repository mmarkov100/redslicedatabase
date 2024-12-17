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
import redslicedatabase.redslicedatabase.logging.LogModel;
import redslicedatabase.redslicedatabase.model.Branch;
import redslicedatabase.redslicedatabase.model.Chat;
import redslicedatabase.redslicedatabase.service.BranchService;
import redslicedatabase.redslicedatabase.service.ChatService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/branches")
public class BranchController {

    private static final Logger logger = LoggerFactory.getLogger(BranchController.class);

    @Autowired
    private ChatService chatService;
    @Autowired
    private BranchService branchService;
    @Autowired
    private LogModel logModel;

    // Создать новую ветку
    @PostMapping
    public ResponseEntity<BranchDTO> createBranch(@Valid @RequestBody CreateBranchDTO createBranchDTO) {

        // Преобразуем в класс модели ветки
        Branch branch = branchService.convertToModel(createBranchDTO);
        logModel.logger(branch, "Received branch");

        Branch createdBranch = branchService.createBranch(branch);// Сохраняем ветку

        // Привязка ветки к чату
        Optional<Chat> chat = chatService.getChatById(branch.getChat().getId());
        createdBranch.setChat(chat.orElseThrow(() -> new RuntimeException("Chat not found")));

        // Возвращаем DTO вместо сущности
        return ResponseEntity.ok(branchService.convertToDTO(createdBranch));
    }



    // Получение конкретной ветки
    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> getBranchById(@PathVariable Long id) {

        Optional<Branch> branch = branchService.getBranchById(id); // Получаем от сервиса данные
        if (branch.isEmpty()) { // Если чат не найден, возвращаем 404 Not Found
            logger.warn("GET ID: Branch with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }

        BranchDTO branchDTO = branchService.convertToDTO(branch.get()); // Преобразуем в DTO
        return ResponseEntity.ok(branchDTO);
    }

    // Получение всех веток чата
    @GetMapping("/chat/{id}")
    public ResponseEntity<List<BranchDTO>> getBranchByChatId(@PathVariable Long id) {
        logger.info("Got chat id: {}", id);
        List<Branch> branches = branchService.getBranchesByChatId(id);
        List<BranchDTO> branchDTOs = branchService.convertToDTO(branches);
        return branchDTOs.isEmpty() // Проверка, пустой ли массив или нет
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(branchDTOs);
    }

    // Получение всех веток пользователя
    @GetMapping("/user/{id}")
    public ResponseEntity<List<List<BranchDTO>>> getBranchByUserId(@PathVariable Long id) {
        logger.info("Got user id: {}", id);

        // Получаем все чаты пользователя
        List<Chat> chats = chatService.getChatsByUserId(id);

        // Получаем ветки для каждого чата и преобразовать их в BranchDTO
        List<List<BranchDTO>> branchesByChats = chats.stream()
                .map(chat -> {
                    List<Branch> branches = branchService.getBranchesByChatId(chat.getId());
                    return branches.stream()
                            .map(branchService::convertToDTO) // Преобразование Branch -> BranchDTO
                            .toList();
                })
                .toList();

        return ResponseEntity.ok(branchesByChats);
    }

    // Получение всех веток
    @GetMapping
    public ResponseEntity<List<BranchDTO>> getAllBranches() {
        List<Branch> branches = branchService.getBranches(); // Получаем все чаты
        List<BranchDTO> branchDTOs = branchService.convertToDTO(branches); // Конвертируем в DTO
        return branchDTOs.isEmpty() // Проверка, пустой ли массив или нет
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(branchDTOs);
    }

    // Получить дочерние ветки от ветки
    @GetMapping("/{id}/children")
    public ResponseEntity<List<BranchDTO>> getChildBranches(@PathVariable Long id) {
        List<Branch> childBranches = branchService.getChildBranches(id);
        List<BranchDTO> branchDTOs = branchService.convertToDTO(childBranches); // Конвертируем в DTO
        return branchDTOs.isEmpty() // Проверка, пустой ли массив или нет
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(branchDTOs);
    }

    // Каскадное удаление ветки
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranchById(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);

        branchService.deleteBranchById(id);
        return ResponseEntity.noContent().build(); // Возвращает 204 No Content
    }
}
