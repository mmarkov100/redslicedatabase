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
import redslicedatabase.redslicedatabase.dto.MessageDTO.inbound.CreateMessagePairDTO;
import redslicedatabase.redslicedatabase.dto.MessageDTO.outbound.MessageDTO;
import redslicedatabase.redslicedatabase.logging.LogModel;
import redslicedatabase.redslicedatabase.model.Branch;
import redslicedatabase.redslicedatabase.model.Message;
import redslicedatabase.redslicedatabase.service.BranchService;
import redslicedatabase.redslicedatabase.service.MessageService;
import redslicedatabase.redslicedatabase.service.UserService;

import java.util.List;

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
}
