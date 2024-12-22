package redslicedatabase.redslicedatabase.service;

/*
Сервис для пользователя
 */

import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redslicedatabase.redslicedatabase.dto.UserDTO.outbound.UserDTO;
import redslicedatabase.redslicedatabase.dto.UserDTO.inbound.UpdateUserDTO;
import redslicedatabase.redslicedatabase.model.Chat;
import redslicedatabase.redslicedatabase.model.User;
import redslicedatabase.redslicedatabase.repository.ChatRepository;
import redslicedatabase.redslicedatabase.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Getter
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRepository chatRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getUsers (){
        return userRepository.findAll();
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUidFirebase(String uidFirebase) {
        return userRepository.findByUidFirebase(uidFirebase);
    }

    // Получение id пользователя по uid файрбейза
    public Long getUserIdByUidFirebase(String uidFirebase) {
        Optional<User> user = userRepository.findByUidFirebase(uidFirebase);
        return user.map(User::getId).orElse(null); // проверка, существует ли вообще такой пользователю
    }

    // Преобразуем User в UserDTO для отправки ответа
    public UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getUidFirebase(),
                user.getTotalTokens(),
                user.getStarredChat() != null ? user.getStarredChat().getId() : null,
                user.getDateCreate()
        );
    }

    public List<UserDTO> convertToDTO(List<User> users) {
        return users.stream()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getUidFirebase(),
                        user.getTotalTokens(),
                        user.getStarredChat() != null ? user.getStarredChat().getId() : null,
                        user.getDateCreate()
                ))
                .toList();
    }

    // Метод обновления настроек пользователя
    public User updateSettings(User existingUser, UpdateUserDTO updatedUser) {

        if (updatedUser.getTotalTokens() != null) {
            existingUser.setTotalTokens(updatedUser.getTotalTokens());
        }
        if (updatedUser.getStarredChatId() != null) {
            Chat selectedChat = chatRepository.findById(updatedUser.getStarredChatId())
                    .orElseThrow(() -> new RuntimeException("Chat not found"));
            existingUser.setStarredChat(selectedChat);
        }
        return userRepository.save(existingUser);
    }



    // Метод удаления пользователя по ID
    @Transactional
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User with ID " + id + " not found.");
        }

        userRepository.deleteById(id);
    }
}

