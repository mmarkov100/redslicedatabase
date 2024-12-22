package redslicedatabase.redslicedatabase.controller;

/*
Контроллер пользователя
Пока реализовано только создание, получение и изменение пользователя
 */

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import redslicedatabase.redslicedatabase.dto.UserDTO.outbound.UserDTO;
import redslicedatabase.redslicedatabase.dto.UserDTO.inbound.UpdateUserDTO;
import redslicedatabase.redslicedatabase.logging.LogModel;
import redslicedatabase.redslicedatabase.model.User;
import redslicedatabase.redslicedatabase.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private LogModel logModel;


    // Создание нового пользователя
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody User user) {
        logModel.logger(user, "Received user");
        User createdUser = userService.createUser(user); // Сохраняем в базе данных
        logModel.logger(createdUser, "Created user");
        return ResponseEntity.ok(userService.convertToDTO(createdUser)); // Отправляем в ответ какой пользователь был сохранен конвертируя в UserDTO
    }


    // Получить существующего пользователя по id
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id); // Получаем возможного пользователя
        if (user.isEmpty()) { // Проверка на то, что юзер с таким id существует
            logger.warn("GET: User with ID {} not found.", id);
            return ResponseEntity.notFound().build(); // Если нет пользователя с таким id, то выводим, что пользователя нет
        }
        logModel.logger(user.get(), "Got user");
        return ResponseEntity.ok(userService.convertToDTO(user.get())); // Конвертируем в UserDTO и отправляем ответ
    }


    // Получить пользователя по email или uidFirebase
    @GetMapping("/query")
    public ResponseEntity<UserDTO> getUserByQuery(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String uidFirebase) {
        if (email != null) {
            Optional<User> user = userService.getUserByEmail(email);
            if (user.isEmpty()) {
                logger.warn("GET: User with email {} not found.", email);
                return ResponseEntity.notFound().build();
            }
            logModel.logger(user.get(), "Got user");
            return ResponseEntity.ok(userService.convertToDTO(user.get()));
        }

        if (uidFirebase != null) {
            Optional<User> user = userService.getUserByUidFirebase(uidFirebase);
            if (user.isEmpty()) {
                logger.warn("GET: User with UID firebase {} not found.", uidFirebase);
                return ResponseEntity.notFound().build();
            }
            logModel.logger(user.get(), "Got user");
            return ResponseEntity.ok(userService.convertToDTO(user.get()));
        }

        // Если параметры отсутствуют
        return ResponseEntity.badRequest().build();
    }

    // Получить всех существующих пользователей
    @GetMapping()
    public ResponseEntity<List<UserDTO>> getUsers (){
        List<UserDTO> userDTOS = userService.convertToDTO(userService.getUsers()); // Получение всех пользователей UserDTO
        return userDTOS.isEmpty() // Проверка на пустоту списка перед отправкой
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(userDTOS);
    }

    // Изменить существующего пользователя по id
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO updatedUser) {

        Optional<User> existingUser = userService.getUserById(id); // Проверяем, существует ли пользователь с указанным id
        if (existingUser.isEmpty()) return ResponseEntity.notFound().build();  // Если пользователь не найден, возвращаем 404

        // Обновляем информацию у пользователя
        User savedUser = userService.updateSettings(existingUser.get(), updatedUser);

        // Возвращаем обновленного пользователя в UserDTO
        UserDTO userDTO = userService.convertToDTO(savedUser);
        return ResponseEntity.ok(userDTO);
    }

    // Обновление настроек пользователя по uid файрбейза
    @PutMapping("/uid/{uidFirebase}")
    public ResponseEntity<UserDTO> updateUserByUID(@PathVariable String uidFirebase, @Valid @RequestBody UpdateUserDTO updatedUser){

        Optional<User> existingUser = userService.getUserByUidFirebase(uidFirebase); // Проверяем, существует ли пользователь с указанным uid файрбейза
        if (existingUser.isEmpty()) return ResponseEntity.notFound().build();  // Если пользователь не найден, возвращаем 404

        // Обновляем информацию у пользователя
        User savedUser = userService.updateSettings(existingUser.get(), updatedUser);

        // Возвращаем обновленного пользователя в UserDTO
        UserDTO userDTO = userService.convertToDTO(savedUser);
        return ResponseEntity.ok(userDTO);

    }

    // Каскадное удаление пользователя по id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        logger.info("Deleting user with ID: {}", id);

        userService.deleteUserById(id);
        return ResponseEntity.noContent().build(); // Возвращает 204 No Content
    }

    // Каскадное удаление пользователя по uid файрбейза
    @DeleteMapping("/uid/{uidFirebase}")
    public ResponseEntity<Void> deleteUserByUid(@PathVariable String uidFirebase) {
        logger.info("Deleting user with UID: {}", uidFirebase);

        Optional<User> existingUser = userService.getUserByUidFirebase(uidFirebase); // Ищем пользователя по uid
        if (existingUser.isEmpty()) return ResponseEntity.notFound().build();  // Если пользователь не найден, возвращаем 404

        userService.deleteUserById(existingUser.get().getId()); // Удаляем пользователя по id
        return ResponseEntity.noContent().build(); // Возвращает 204 No Content
    }
}