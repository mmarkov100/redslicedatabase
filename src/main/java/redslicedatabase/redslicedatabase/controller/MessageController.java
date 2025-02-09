package redslicedatabase.redslicedatabase.controller;

/*
Контроллер сообщения
Пока реализовано только создание 1 и 2 сразу сообщений и получение сообщений
 */

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redslicedatabase.redslicedatabase.dto.MessageDTO.inbound.CreateMessageDTO;
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
import java.util.Optional;

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

    // Создание нового сообщения. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@Valid @RequestBody CreateMessageDTO createMessageDTO) {
        // Проверяем доступ к ветке
        Branch branch = messageService.validateBranchAccess(createMessageDTO.getBranchId(), createMessageDTO.getUidFirebase());

        // Конвертируем DTO в модель
        Message message = messageService.convertToModel(createMessageDTO);

        // Логирование полученного сообщения
        logModel.logger(message, "Received message");

        // Сохраняем сообщение
        Message createdMessage = messageService.createMessage(message);

        // Обновляем даты у ветки и чата
        branchService.updateBranchAndChatDates(branch);

        // Возвращаем DTO
        MessageDTO messageDTO = messageService.convertToDTO(createdMessage);
        return ResponseEntity.ok(messageDTO);
    }

    // Создания сразу двух новых сообщений, от пользователя и нейросети. Возвращает обновленный список сообщений ветки
    @PostMapping("/pair")
    public ResponseEntity<List<MessageDTO>> saveMessagePair(@Valid @RequestBody CreateMessagePairDTO createMessagePairDTO) {
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

    // Получение сообщений для ветки. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @GetMapping("/branch/{id}")
    public ResponseEntity<List<MessageDTO>> getMessagesByBranchId(@PathVariable Long id) {
        logger.info("GET: Got branch id: {}", id);
        List<Message> messages = messageService.getMessagesByBranchId(id);
        List<MessageDTO> messageDTOS = messageService.convertToDTO(messages);
        return messageDTOS.isEmpty() // Проверка, пустой ли массив или нет
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(messageDTOS);
    }

    // Получение сообщений для ветки с валидацией
    @GetMapping("/branch/{id}/validate")
    public ResponseEntity<List<MessageDTO>> getMessagesByBranchIdAndFirebase(@PathVariable Long id,
                                                                             @RequestParam String uidFirebase) {
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

    // Получить сообщение по его ID. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> getMessageById(@PathVariable Long id) {

        Optional<Message> message = messageService.getMessageById(id); // Получаем от сервиса данные
        if (message.isEmpty()) { // Если чат не найден, возвращаем 404 Not Found
            logger.warn("GET ID: Branch with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }

        MessageDTO messageDTO = messageService.convertToDTO(message.get()); // Преобразуем в DTO
        return ResponseEntity.ok(messageDTO);
    }

    // Получить сообщение по его ID с валидацией. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @GetMapping("/{id}/validate")
    public ResponseEntity<MessageDTO> getMessageByIdAndFirebase(@PathVariable Long id,
                                                                @RequestParam String uidFirebase) {
        Message message = messageService.getMessageById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        User user = userService.getUserByUidFirebase(uidFirebase)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (message.getUser() != user)
        {
            return ResponseEntity.badRequest().build();
        }

        MessageDTO messageDTO = messageService.convertToDTO(message); // Преобразуем в DTO
        return ResponseEntity.ok(messageDTO);
    }
    // Получить все сообщения. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @GetMapping
    public ResponseEntity<List<MessageDTO>> getMessages() {
        List<Message> messages = messageService.getAllMessages();
        List<MessageDTO> messagesDTO = messageService.convertToDTO(messages);
        return messagesDTO.isEmpty() // Проверка, пустой ли массив или нет
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(messagesDTO);
    }

    // Удаление одного сообщения по его ID. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessageById(@PathVariable Long id) {
        logger.info("Deleting message with ID: {}", id);

        messageService.deleteMessageById(id);
        return ResponseEntity.noContent().build(); // Возвращает 204 No Content
    }

    // Удаление всех сообщений в ветке по ее ID. НЕ ИСПОЛЬЗУЕТСЯ В КОНЕЧНОМ ПРОДУКТЕ
//    @DeleteMapping("/branch/{branchId}")
    public ResponseEntity<Void> deleteMessagesByBranchId(@PathVariable Long branchId) {
        logger.info("Deleting all messages in branch with ID: {}", branchId);

        messageService.deleteMessagesByBranchId(branchId);
        return ResponseEntity.noContent().build(); // Возвращает 204 No Content
    }
}
