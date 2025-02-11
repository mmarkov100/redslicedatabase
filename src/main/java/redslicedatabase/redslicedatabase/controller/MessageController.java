package redslicedatabase.redslicedatabase.controller;

/*
Контроллер сообщения
Пока реализовано только создание 1 и 2 сразу сообщений и получение сообщений
 */

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redslicedatabase.redslicedatabase.config.AppConfig;
import redslicedatabase.redslicedatabase.dto.MessageDTO.inbound.CreateMessagePairDTO;
import redslicedatabase.redslicedatabase.dto.MessageDTO.outbound.MessageDTO;
import redslicedatabase.redslicedatabase.logging.LogModel;
import redslicedatabase.redslicedatabase.model.Branch;
import redslicedatabase.redslicedatabase.model.Message;
import redslicedatabase.redslicedatabase.model.User;
import redslicedatabase.redslicedatabase.service.BranchService;
import redslicedatabase.redslicedatabase.service.MessageService;
import redslicedatabase.redslicedatabase.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private BranchService branchService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private LogModel logModel;
    @Autowired
    private UserService userService;
    @Autowired
    private AppConfig appConfig;

    // Создания сразу двух новых сообщений, от пользователя и нейросети. Возвращает обновленный список сообщений ветки
    @PostMapping("/pair")
    public ResponseEntity<?> saveMessagePair(@Valid @RequestBody CreateMessagePairDTO createMessagePairDTO,
                                                            @RequestHeader String apiDBKey) {
        if (!Objects.equals(apiDBKey, appConfig.getApiDatabaseKey())) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Wrong apiDBKey"
                    ));
        }
        logger.info("Received message pair for branch ID: {}", createMessagePairDTO.toString());

        // Проверяем доступ к ветке
        Branch branch = messageService.validateBranchAccess(createMessagePairDTO.getBranchId(),
                createMessagePairDTO.getMessages().getFirst().getUidFirebase());

        // Сохраняем сообщения в базе данных
        messageService.saveMessagePair(createMessagePairDTO);

        // Получаем историю сообщений ветки
        List<Message> branchMessages = messageService.getMessagesByBranchId(createMessagePairDTO.getBranchId());
        List<MessageDTO> messageDTOs = messageService.convertToDTO(branchMessages);

        // Обновляем даты у ветки и чата
        branchService.updateBranchAndChatDates(branch);

        logger.info("Saved message pair and retrieved branch history, last message: {}", messageDTOs.getLast().toString());
        return ResponseEntity.ok(messageDTOs);
    }

    // Получение сообщений для ветки с валидацией
    @GetMapping("/branch/{id}/validate")
    public ResponseEntity<?> getMessagesByBranchIdAndFirebase(@PathVariable Long id,
                                                                             @RequestParam String uidFirebase,
                                                                             @RequestHeader String apiDBKey) {
        if (!Objects.equals(apiDBKey, appConfig.getApiDatabaseKey())) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "error", "Wrong apiDBKey"
                    ));
        }
        Branch branch = branchService.getBranchById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        User user = userService.getUserByUidFirebase(uidFirebase)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (branch.getUser() != user)
        {
            return ResponseEntity.badRequest().build();
        }
        logger.info("GET UID: Got branch id: {}", id);
        List<Message> messages = messageService.getMessagesByBranchId(id);
        List<MessageDTO> messageDTOS = messageService.convertToDTO(messages);
        return messageDTOS.isEmpty() // Проверка, пустой ли массив или нет
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(messageDTOS);
    }
}
