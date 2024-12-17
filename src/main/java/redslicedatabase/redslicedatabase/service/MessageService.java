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
import redslicedatabase.redslicedatabase.repository.BranchRepository;
import redslicedatabase.redslicedatabase.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private BranchRepository branchRepository;

    // Создание сообщения
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }
    // Поиск сообщения по Id сообщения
    public Optional<Message> getMessageById(Long id) {
        return messageRepository.findById(id);
    }

    // Получить все сообщения
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // Поиск списка сообщений по определенной ветке в отсортированном виде
    public List<Message> getMessagesByBranchId(Long branchId) {
        // Вернем сообщения по branchId в порядке их создания (по полю dateCreate)
        return messageRepository.findByBranchIdOrderByDateCreate(branchId, Sort.by(Sort.Direction.ASC, "dateCreate"));
    }

    // Метод для удаления сообщения по ID
    public void deleteMessageById(Long id) {
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("Message with ID " + id + " not found");
        }
        messageRepository.deleteById(id);
    }

    // Метод удаления всех сообщений в ветке
    @Transactional
    public void deleteMessagesByBranchId(Long branchId) {
        // Проверяем, существует ли ветка с таким ID
        if (!branchRepository.existsById(branchId)) {
            throw new RuntimeException("Branch with ID " + branchId + " not found.");
        }

        // Удаляем все сообщения по branchId
        List<Message> messages = messageRepository.findByBranchId(branchId);
        messageRepository.deleteAll(messages);
    }

    // Метод конвертирования в Message
    public Message convertToModel(CreateMessageDTO createMessageDTO) {
        // Загружаем ветку из базы данных
        Branch branch = branchRepository.findById(createMessageDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch with ID " + createMessageDTO.getBranchId() + " not found"));

        Message message = new Message(); // Создаем новый объект Message
        message.setBranch(branch);
        message.setRole(createMessageDTO.getRole());
        message.setText(createMessageDTO.getText());
        message.setTotalTokens(createMessageDTO.getTotalTokens());
        message.setInputTokens(createMessageDTO.getInputTokens());
        message.setCompletionTokens(createMessageDTO.getCompletionTokens());
        message.setDateCreate(java.time.LocalDateTime.now());
        System.out.println("CreateMessageDTO: branchId=" + createMessageDTO.getBranchId() +
                ", role=" + message.getRole() +
                ", text=" + message.getText() +
                ", totalTokens=" + message.getTotalTokens() +
                ", inputTokens=" + message.getInputTokens() +
                ", completionTokens=" + message.getCompletionTokens());

        return message;
    }

    // Метод конвертирования в MessageDTO
    public MessageDTO convertToDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getBranch() != null ? message.getBranch().getId() : null,
                message.getRole(),
                message.getText(),
                message.getTotalTokens()  != null ? message.getTotalTokens() : 0,
                message.getInputTokens() != null ? message.getInputTokens() : 0,
                message.getCompletionTokens() != null ? message.getCompletionTokens() : 0,
                message.getDateCreate()
        );
    }

    // Метод конвертирования в лист MessageDTO
    public List<MessageDTO> convertToDTO(List<Message> messages) {
        return messages.stream()
                .map(message -> new MessageDTO(
                        message.getId(),
                        message.getBranch() != null ? message.getBranch().getId() : null,
                        message.getRole(),
                        message.getText(),
                        message.getTotalTokens()  != null ? message.getTotalTokens() : 0,
                        message.getInputTokens() != null ? message.getInputTokens() : 0,
                        message.getCompletionTokens() != null ? message.getCompletionTokens() : 0,
                        message.getDateCreate()
                ))
                .toList();
    }

    // Метод сохранения сразу двух сообщений
    @Transactional // Если 1 из 2‑х сообщений не смогло сохраниться, то и 2 не сохранится
    public List<Message> saveMessagePair(CreateMessagePairDTO createMessagePairDTO) {
        // Проверяем существование ветки
        Branch branch = branchRepository.findById(createMessagePairDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        // Конвертируем и сохраняем оба сообщения
        List<Message> messages = createMessagePairDTO.getMessages().stream()
                .map(dto -> {
                    Message message = new Message();
                    message.setBranch(branch);
                    message.setRole(dto.getRole());
                    message.setText(dto.getText());
                    message.setTotalTokens(dto.getTotalTokens());
                    message.setInputTokens(dto.getInputTokens());
                    message.setCompletionTokens(dto.getCompletionTokens());
                    message.setDateCreate(java.time.LocalDateTime.now());
                    return message;
                })
                .toList();

        return messageRepository.saveAll(messages);
    }
}
