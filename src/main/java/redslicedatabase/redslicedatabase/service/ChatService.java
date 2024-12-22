package redslicedatabase.redslicedatabase.service;

/*
Сервис для чатов
 */

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redslicedatabase.redslicedatabase.dto.ChatDTO.inbound.CreateChatDTO;
import redslicedatabase.redslicedatabase.dto.ChatDTO.inbound.UpdateChatDTO;
import redslicedatabase.redslicedatabase.dto.ChatDTO.outbound.ChatDTO;
import redslicedatabase.redslicedatabase.model.Branch;
import redslicedatabase.redslicedatabase.model.Chat;
import redslicedatabase.redslicedatabase.model.User;
import redslicedatabase.redslicedatabase.repository.BranchRepository;
import redslicedatabase.redslicedatabase.repository.ChatRepository;
import redslicedatabase.redslicedatabase.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ChatService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private BranchRepository branchRepository;

    // Сохраняет чат
    public void saveChat(Chat chat) {
        chatRepository.save(chat);
    }

    // Создать чат, сразу вместе с выбранной корневой веткой
    @Transactional
    public Chat createChat(Chat chat) {
        Chat savedChat = chatRepository.save(chat);// Сохраняем чат
        Branch rootBranch = createRootBranch(savedChat); // Создаем корневую ветку
        savedChat.setSelectedBranch(rootBranch); // Сохраняем корневую ветку в чате как выбранную ветку
        return savedChat;
    }

    // Создание корневой ветки при создании чата
    private Branch createRootBranch(Chat chat) {
        Branch rootBranch = new Branch();
        rootBranch.setChat(chat);
        rootBranch.setUser(chat.getUser());
        rootBranch.setParentBranch(null);
        rootBranch.setMessageStart(null);
        rootBranch.setIsRoot(true);
        rootBranch.setDateCreate(LocalDateTime.now());
        rootBranch.setDateEdit(LocalDateTime.now());
        return branchRepository.save(rootBranch);
    }

    // Поучение чата по его Id
    public Optional<Chat> getChatById(Long id) {
        return chatRepository.findById(id);
    }

    // Получение всех чатов
    public List<Chat> getChats() {
        return chatRepository.findAll();
    }

    // Получение всех чатов пользователя
    public List<Chat> getChatsByUserId(Long userId) {
        return chatRepository.findByUserId(userId);
    }

    // Преобразование обычного класса Chat в ChatDTO для отправки
    public ChatDTO convertToDTO(Chat chat) {
        return new ChatDTO(
                chat.getId(),
                Optional.ofNullable(chat.getUser()).map(User::getId).orElse(null),
                chat.getChatName(),
                chat.getTemperature(),
                chat.getContext(),
                chat.getModelUri(),
                Optional.ofNullable(chat.getSelectedBranch()).map(Branch::getId).orElse(null),
                chat.getDateEdit(),
                chat.getDateCreate()
        );
    }

    // Преобразуем список чатов класса Chat в список ChatDTO
    public List<ChatDTO> convertToDTO(List<Chat> chats) {
        return chats.stream() // Преобразуем список чатов в DTO список
                .map(chat -> new ChatDTO(
                        chat.getId(),
                        Optional.ofNullable(chat.getUser()).map(User::getId).orElse(null),
                        chat.getChatName(),
                        chat.getTemperature(),
                        chat.getContext(),
                        chat.getModelUri(),
                        Optional.ofNullable(chat.getSelectedBranch()).map(Branch::getId).orElse(null),
                        chat.getDateEdit(),
                        chat.getDateCreate()
                ))
                .toList();
    }

    // Метод конвертации CreateChatDTO, класса для получения данных об создании чата, в класс Chat
    public Chat convertToModel(CreateChatDTO createChatDTO) {
        // Загружаем пользователя из базы данных
        User user = userRepository.findByUidFirebase(createChatDTO.getUidFirebase()) // Если пользователь не найден, выкинет ошибку
                .orElseThrow(() -> new RuntimeException("User with UID " + createChatDTO.getUidFirebase() + " not found"));

        Chat chat = new Chat(); // Создаем новый объект Chat и сохраняем в него данные
        chat.setUser(user);
        chat.setChatName(createChatDTO.getChatName());
        chat.setTemperature(createChatDTO.getTemperature());
        chat.setContext(createChatDTO.getContext());
        chat.setModelUri(createChatDTO.getModelUri());
        chat.setDateCreate(java.time.LocalDateTime.now());
        chat.setDateEdit(java.time.LocalDateTime.now());

        return chat;
    }

    // Метод изменения настроек чата
    public Chat updateSettings(Chat existingChat, UpdateChatDTO updatedChatDTO) {
        if (updatedChatDTO.getChatName() != null) {
            existingChat.setChatName(updatedChatDTO.getChatName());
        }
        if (updatedChatDTO.getTemperature() != null) {
            existingChat.setTemperature(updatedChatDTO.getTemperature());
        }
        if (updatedChatDTO.getContext() != null) {
            existingChat.setContext(updatedChatDTO.getContext());
        }
        if (updatedChatDTO.getModelUri() != null) {
            existingChat.setModelUri(updatedChatDTO.getModelUri());
        }
        if (updatedChatDTO.getSelectedBranchId() != null) {
            Branch selectedBranch = branchRepository.findById(updatedChatDTO.getSelectedBranchId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            existingChat.setSelectedBranch(selectedBranch);
        }
        existingChat.setDateEdit(LocalDateTime.now()); // Обновляем дату редактирования
        return chatRepository.save(existingChat);
    }

    // Метод проверки чата конкретному пользователю
    public Chat getChatByIdWithAccessCheck(Long chatId, String uidFirebase) throws AccessDeniedException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat with ID " + chatId + " not found"));

        if (!Objects.equals(chat.getUser().getUidFirebase(), uidFirebase)) {
            throw  new AccessDeniedException("Access denied for user UID: " + uidFirebase);
        }

        return chat;
    }

    // Метод каскадного удаления чата (не удаляет, если не подтвердится удаление всего) по ID с учетом доступа юзера к чату
    @Transactional
    public void deleteChatByIdWithAccessCheck(Long chatId, String uidFirebase) throws AccessDeniedException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat with ID " + chatId + " not found"));

        if (!Objects.equals(chat.getUser().getUidFirebase(), uidFirebase)) {
            throw new AccessDeniedException("User does not have access to this chat");
        }

        chatRepository.deleteById(chatId);
    }
}
