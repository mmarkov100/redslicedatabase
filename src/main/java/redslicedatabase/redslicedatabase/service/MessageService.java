package redslicedatabase.redslicedatabase.service;

/*
Сервис для сообщений
 */

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import redslicedatabase.redslicedatabase.dto.MessageDTO.inbound.CreateMessageDTO;
import redslicedatabase.redslicedatabase.dto.MessageDTO.inbound.CreateMessagePairDTO;
import redslicedatabase.redslicedatabase.dto.MessageDTO.outbound.MessageDTO;
import redslicedatabase.redslicedatabase.model.Branch;
import redslicedatabase.redslicedatabase.model.Message;
import redslicedatabase.redslicedatabase.model.User;
import redslicedatabase.redslicedatabase.repository.BranchRepository;
import redslicedatabase.redslicedatabase.repository.MessageRepository;
import redslicedatabase.redslicedatabase.repository.UserRepository;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BranchService branchService;
    @Autowired
    private UserService userService;

    // Поиск списка сообщений по определенной ветке в отсортированном виде
    public List<Message> getMessagesByBranchId(Long branchId) {
        // Вернем сообщения по branchId в порядке их создания (по полю dateCreate)
        return messageRepository.findByBranchIdOrderByDateCreate(branchId, Sort.by(Sort.Direction.ASC, "dateCreate"));
    }

    private Message newMessage(CreateMessageDTO createMessageDTO, Branch branch, User user) {
        Message message = new Message(); // Создаем новый объект Message
        message.setUser(user);
        message.setBranch(branch);
        message.setRole(createMessageDTO.getRole());
        message.setText(createMessageDTO.getText());
        message.setTotalTokens(createMessageDTO.getTotalTokens());
        message.setInputTokens(createMessageDTO.getInputTokens());
        message.setCompletionTokens(createMessageDTO.getCompletionTokens());
        message.setDateCreate(java.time.LocalDateTime.now());
        message.setUsedModel(createMessageDTO.getUsedModel());
        return message;
    }

    // Метод конвертирования в MessageDTO
    public MessageDTO convertToDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getUser() != null ? message.getUser().getId() : null,
                message.getBranch() != null ? message.getBranch().getId() : null,
                message.getRole(),
                message.getText(),
                message.getTotalTokens()  != null ? message.getTotalTokens() : 0,
                message.getInputTokens() != null ? message.getInputTokens() : 0,
                message.getCompletionTokens() != null ? message.getCompletionTokens() : 0,
                message.getDateCreate(),
                message.getUsedModel()
        );
    }

    // Метод конвертирования в лист MessageDTO
    public List<MessageDTO> convertToDTO(List<Message> messages) {
        return messages.stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Метод сохранения сразу двух сообщений
    @Transactional // Если 1 из 2‑х сообщений не смогло сохраниться, то и 2 не сохранится
    public void saveMessagePair(CreateMessagePairDTO createMessagePairDTO) {
        // Проверяем существование ветки
        Branch branch = branchRepository.findById(createMessagePairDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        // Проверяем существование пользователя
        User user = userRepository.findByUidFirebase(createMessagePairDTO.getMessages().getFirst().getUidFirebase())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Конвертируем и сохраняем оба сообщения
        List<Message> messages = createMessagePairDTO.getMessages().stream()
                .map(dto -> newMessage(dto, branch, user))
                .toList();


        messageRepository.saveAll(messages);
    }

    public Branch validateBranchAccess(Long branchId, String uidFirebase) {
        // Проверяем, что ветка существует
        Branch branch = branchService.getBranchById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch with ID " + branchId + " not found"));

        // Проверяем, что пользователь существует
        User user = userService.getUserByUidFirebase(uidFirebase)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Проверяем, что ветка принадлежит указанному пользователю
        if (!branch.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("User does not have access to this branch");
        }

        return branch;
    }
}
