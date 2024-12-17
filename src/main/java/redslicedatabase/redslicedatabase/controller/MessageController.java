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
import redslicedatabase.redslicedatabase.service.BranchService;
import redslicedatabase.redslicedatabase.service.MessageService;

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

    // Создание нового сообщения
    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@Valid @RequestBody CreateMessageDTO createMessageDTO) {

        Message message = messageService.convertToModel(createMessageDTO); // конвертируем в DTO
        logModel.logger(message, "Received message");

        Message createdMessage = messageService.createMessage(message);// Сохраняем сообщение

        Optional<Branch> branch = branchService.getBranchById(message.getBranch().getId()); // Привязка сообщения к ветке
        createdMessage.setBranch(branch.orElseThrow(() -> new RuntimeException("Branch not found")));

        MessageDTO messageDTO = messageService.convertToDTO(createdMessage); // Возвращаем DTO

        return ResponseEntity.ok(messageDTO);
    }

    // Создания сразу двух новых сообщений, от пользователя и нейросети
    @PostMapping("/pair")
    public ResponseEntity<List<MessageDTO>> saveMessagePair(@Valid @RequestBody CreateMessagePairDTO createMessagePairDTO) {
        logger.info("Received message pair for branch ID: {}", createMessagePairDTO.getBranchId());

        // Сохраняем сообщения в бд
        messageService.saveMessagePair(createMessagePairDTO);

        // Получаем историю сообщений ветки
        List<Message> branchMessages = messageService.getMessagesByBranchId(createMessagePairDTO.getBranchId());
        List<MessageDTO> messageDTOs = messageService.convertToDTO(branchMessages);

        logger.info("Saved message pair and retrieved branch history, total messages: {}", messageDTOs.size());
        return ResponseEntity.ok(messageDTOs);
    }

    // Получение сообщений для ветки
    @GetMapping("/branch/{id}")
    public ResponseEntity<List<MessageDTO>> getMessagesByBranchId(@PathVariable Long id) {
        logger.info("Got branch id: {}", id);
        List<Message> messages = messageService.getMessagesByBranchId(id);
        List<MessageDTO> messageDTOS = messageService.convertToDTO(messages);
        return messageDTOS.isEmpty() // Проверка, пустой ли массив или нет
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(messageDTOS);
    }

    // Получить сообщение по его ID
    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> getMessageById(@PathVariable Long id) {

        Optional<Message> message = messageService.getMessageById(id); // Получаем от сервиса данные
        if (message.isEmpty()) { // Если чат не найден, возвращаем 404 Not Found
            logger.warn("GET ID: Branch with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }

        MessageDTO messageDTO = messageService.convertToDTO(message.get()); // Преобразуем в DTO
        return ResponseEntity.ok(messageDTO);
    }

    // Получить все сообщения
    @GetMapping
    public ResponseEntity<List<MessageDTO>> getMessages() {
        List<Message> messages = messageService.getAllMessages();
        List<MessageDTO> messagesDTO = messageService.convertToDTO(messages);
        return messagesDTO.isEmpty() // Проверка, пустой ли массив или нет
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(messagesDTO);
    }


    // Удаление одного сообщения по его ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessageById(@PathVariable Long id) {
        logger.info("Deleting message with ID: {}", id);

        messageService.deleteMessageById(id);
        return ResponseEntity.noContent().build(); // Возвращает 204 No Content
    }

    // Удаление всех сообщений в ветке по ее ID
    @DeleteMapping("/branch/{branchId}")
    public ResponseEntity<Void> deleteMessagesByBranchId(@PathVariable Long branchId) {
        logger.info("Deleting all messages in branch with ID: {}", branchId);

        messageService.deleteMessagesByBranchId(branchId);
        return ResponseEntity.noContent().build(); // Возвращает 204 No Content
    }
}
