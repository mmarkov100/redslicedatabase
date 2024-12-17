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

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<ChatDTO> createChat(@Valid @RequestBody CreateChatDTO createChatDTO) {

        Chat chat = chatService.convertToModel(createChatDTO); // Конвертируем полученный DTO в модель
        logModel.logger(chat, "Received chat"); // Вывод логов пришедших данных в чат

        User user = userService.getUserById(chat.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found")); // Или кастомное исключение

        chat.setUser(user); // Привязываем чат к юзеру
        Chat createdChat = chatService.createChat(chat);  // Сохраняем

        logModel.logger(chat, "Chat created"); // Вывод данных чата, которые сохранены в бд
        ChatDTO chatDTO = chatService.convertToDTO(createdChat); // Преобразуем в DTO
        return ResponseEntity.ok(chatDTO);
    }

    // Получение конкретного чата по Id
    @GetMapping("/{id}")
    public ResponseEntity<ChatDTO> getChatById(@PathVariable Long id) {

        Optional<Chat> chat = chatService.getChatById(id); // Получаем от сервиса данные
        if (chat.isEmpty()) {// Если чат не найден, возвращаем 404 Not Found
            logger.warn("GET ID: Chat with ID {} not found.", id);
            return ResponseEntity.notFound().build();
        }
        logModel.logger(chat.get(), "Got chat by id");
        ChatDTO chatDTO = chatService.convertToDTO(chat.get()); // Преобразуем в DTO
        return ResponseEntity.ok(chatDTO);
    }

    // Получить все чаты пользователя по его id
    @GetMapping("/user/{id}")
    public ResponseEntity<List<ChatDTO>> getChatsByUserId(@PathVariable Long id) {
        logger.info("Got user id: {}", id);
        List<Chat> chats = chatService.getChatsByUserId(id); // Получаем список чатов пользователя
        List<ChatDTO> chatDTOS = chatService.convertToDTO(chats); // Преобразуем список чатов в DTO список
        return chatDTOS.isEmpty() // Проверка на пустоту списка вместе с отправкой
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(chatDTOS);
    }

    // Получить все чаты
    @GetMapping
    public ResponseEntity<List<ChatDTO>> getAllChats() {
        List<Chat> chats = chatService.getChats();  // Получаем список всех чатов
        List<ChatDTO> chatDTOS = chatService.convertToDTO(chats); // Преобразуем список чатов в DTO список
        return chatDTOS.isEmpty() // Проверка на пустоту списка
                ? ResponseEntity.notFound().build() // Если пустой, то возвращает пустоту и ничего не найдено
                : ResponseEntity.ok(chatDTOS); // Иначе список чатов и статус ок
    }

    // Изменить настройки существующего чата по id
    @PutMapping("/{id}")
    public ResponseEntity<ChatDTO> updateChat(@PathVariable Long id, @Valid @RequestBody UpdateChatDTO updatedChatDTO) {
        Optional<Chat> existingChatOpt = chatService.getChatById(id); // Получаем существующий чат по id
        if (existingChatOpt.isEmpty()) { // Если чат найденный по id пустой, то выдаем, что чат пустой
            logger.warn("Chat with ID {} not found.", id);
            return ResponseEntity.notFound().build();
        }

        Chat savedChat = chatService.updateSettings(existingChatOpt.get(), updatedChatDTO); // Сохраняем обновленный чат и обновляем настройки

        return ResponseEntity.ok(chatService.convertToDTO(savedChat));// Преобразуем в DTO и возвращаем
    }

    // Каскадное удаление чата
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChatById(@PathVariable Long id) {
        logger.info("Deleting chat with ID: {}", id);

        chatService.deleteChatById(id);
        return ResponseEntity.noContent().build(); // Возвращает 204 No Content
    }
}