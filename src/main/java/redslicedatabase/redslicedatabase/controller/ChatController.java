package redslicedatabase.redslicedatabase.controller;

/*
Контроллер чата
Реализовано пока только создание чата и его просмотр и обновление настроек чата
 */

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redslicedatabase.redslicedatabase.dto.ChatDTO.inbound.CreateChatDTO;
import redslicedatabase.redslicedatabase.dto.ChatDTO.inbound.UpdateChatDTO;
import redslicedatabase.redslicedatabase.dto.ChatDTO.outbound.ChatDTO;
import redslicedatabase.redslicedatabase.logging.LogModel;
import redslicedatabase.redslicedatabase.model.Chat;
import redslicedatabase.redslicedatabase.model.User;
import redslicedatabase.redslicedatabase.service.ChatService;
import redslicedatabase.redslicedatabase.service.UserService;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/chats")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;
    @Autowired
    private LogModel logModel;

    // Создать новый чат
    @PostMapping
    public ResponseEntity<ChatDTO> createChat(@RequestBody CreateChatDTO createChatDTO) {

        Chat chat = chatService.convertToModel(createChatDTO); // Конвертируем полученный DTO в модель
        logModel.logger(chat, "Received chat"); // Вывод логов пришедших данных в чат

        User user = userService.getUserById(chat.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found")); // Или кастомное исключение

        chat.setUser(user); // Привязываем чат к юзеру
        Chat createdChat = chatService.createChat(chat);  // Сохраняем

        logModel.logger(chat, "Chat created"); // Вывод данных чата, которые сохранены в бд
        ChatDTO chatDTO = chatService.convertToDTO(createdChat); // Преобразуем в DTO
        return ResponseEntity.ok(chatDTO);
    }

    // Получение конкретного чата по Id и проверка, есть ли доступ у пользователя к этому чату
    @GetMapping("/{id}/validate")
    public ResponseEntity<ChatDTO> getChatByIdAndUidFirebase(@PathVariable Long id,
                                                             @RequestParam String uidFirebase) throws AccessDeniedException {

        Chat chat = chatService.getChatByIdWithAccessCheck(id, uidFirebase); // Отправляем в сервис
        logModel.logger(chat, "Got chat by id");
        return ResponseEntity.ok(chatService.convertToDTO(chat));
    }

    // Получить все чаты пользователя по его uid файрбейза
    @GetMapping("/user/uid/{uidFirebase}")
    public ResponseEntity<List<ChatDTO>> getChatsByUserIdAndFirebase(@PathVariable String uidFirebase) {
        logger.info("Got user uid: {}", uidFirebase);
        Long id = userService.getUserIdByUidFirebase(uidFirebase); // Получаем id пользователя по uid файрбейза
        List<Chat> chats = chatService.getChatsByUserId(id); // Получаем список чатов пользователя
        List<ChatDTO> chatDTOS = chatService.convertToDTO(chats); // Преобразуем список чатов в DTO список
        return chatDTOS.isEmpty() // Проверка на пустоту списка вместе с отправкой
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(chatDTOS);
    }


    // Изменить настройки существующего чата по id
    @PutMapping("/{id}")
    public ResponseEntity<ChatDTO> updateChat(@PathVariable Long id,
                                              @Valid @RequestBody UpdateChatDTO updatedChatDTO) throws AccessDeniedException {

        // Отправляем проверку доступа и получение чата в сервис
        Chat chat = chatService.getChatByIdWithAccessCheck(id,updatedChatDTO.getUidFirebase());

        // Сохраняем обновленный чат и обновляем настройки
        Chat savedChat = chatService.updateSettings(chat, updatedChatDTO);

        // Обновление dateEdit у чата, к которому принадлежит ветка
        savedChat.setDateEdit(LocalDateTime.now()); // Обновляем дату
        chatService.saveChat(savedChat); // Сохраняем изменения в базе данных

        return ResponseEntity.ok(chatService.convertToDTO(savedChat));// Преобразуем в DTO и возвращаем
    }

    // Каскадное удаление чата с проверкой доступа
    @DeleteMapping("/{id}/validate")
    public ResponseEntity<Void> deleteChatById(@PathVariable Long id,
                                               @RequestParam String uidFirebase) throws AccessDeniedException {
        chatService.deleteChatByIdWithAccessCheck(id, uidFirebase);
        return ResponseEntity.noContent().build();
    }
}