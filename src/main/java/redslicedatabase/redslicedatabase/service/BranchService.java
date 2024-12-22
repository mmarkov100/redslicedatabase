package redslicedatabase.redslicedatabase.service;

/*
Сервис для веток
 */

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redslicedatabase.redslicedatabase.dto.BranchDTO.inbound.CreateBranchDTO;
import redslicedatabase.redslicedatabase.dto.BranchDTO.outbound.BranchDTO;
import redslicedatabase.redslicedatabase.model.Branch;
import redslicedatabase.redslicedatabase.model.Chat;
import redslicedatabase.redslicedatabase.model.Message;
import redslicedatabase.redslicedatabase.model.User;
import redslicedatabase.redslicedatabase.repository.BranchRepository;
import redslicedatabase.redslicedatabase.repository.ChatRepository;
import redslicedatabase.redslicedatabase.repository.MessageRepository;
import redslicedatabase.redslicedatabase.repository.UserRepository;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatService chatService;

    // Сохранение ветки
    public void saveBranch(Branch branch) {
        branchRepository.save(branch);
    }

    // Метод создания ветки
    @Transactional
    public Branch createBranch(CreateBranchDTO createBranchDTO) {
        Branch branch = convertToModel(createBranchDTO);
        Chat chat = chatRepository.findById(createBranchDTO.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        // Проверяем, принадлежит ли пользователь чату
        if (!Objects.equals(chat.getUser().getUidFirebase(), createBranchDTO.getUidFirebase())) {
            throw new RuntimeException("User does not have access to this chat");
        }

        branch.setChat(chat);
        branch.setDateCreate(LocalDateTime.now());
        branch.setDateEdit(LocalDateTime.now());
        branchRepository.save(branch);

        // Обновляем дату у чата
        chat.setDateEdit(LocalDateTime.now());
        chatRepository.save(chat);

        return branch;
    }

    // Метод получения ветки по id
    public Optional<Branch> getBranchById(Long id) {
        return branchRepository.findById(id);
    }

    // Метод получения всех веток
    public List<Branch> getBranches() {
        return branchRepository.findAll();
    }

    // Метод получения веток пользователя
    public List<Branch> getBranchesByChatId(Long chatId) {
        return branchRepository.findByChatId(chatId);
    }

    // Метод получения дочерних веток
    public List<Branch> getChildBranches(Long parentBranchId) {
        return branchRepository.findByParentBranchId(parentBranchId);
    }

    // Получение всех веток пользователя по ID
    public List<Branch> getBranchesByUserId(Long userId) {
        return branchRepository.findAllByUserId(userId);
    }

    // Получение всех веток пользователя по UID Firebase
    public List<Branch> getBranchesByUserUidFirebase(String uidFirebase) {
        return branchRepository.findAllByUserUidFirebase(uidFirebase);
    }

    // Метод конвертации Branch в BranchDTO
    public BranchDTO convertToDTO(Branch branch) {
        return new BranchDTO(
                branch.getId(),
                branch.getUser() != null ? branch.getUser().getId() : null,
                branch.getChat() != null ? branch.getChat().getId() : null,
                branch.getParentBranch() != null ? branch.getParentBranch().getId() : null,
                branch.getMessageStart() != null ? branch.getMessageStart().getId() : null,
                branch.getIsRoot(),
                branch.getDateEdit(),
                branch.getDateCreate()
        );
    }

    // Метод конвертации списка веток в список BranchDTO
    public List<BranchDTO> convertToDTO(List<Branch> branches) {
        return branches.stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Метод конвертации пришедшего DTO в модель для сохранения
    public Branch convertToModel(CreateBranchDTO createBranchDTO) {
        // Ищем чат, к которому привязана ветка
        Chat chat = chatRepository.findById(createBranchDTO.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat with ID " + createBranchDTO.getChatId() + " not found"));
        // Проверяем, существует ли пользователь по указанному uid
        User user = userRepository.findByUidFirebase(createBranchDTO.getUidFirebase())
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Создаем новый объект ветки и сохраняем данные
        Branch branch = new Branch();
        branch.setUser(user);
        branch.setChat(chat);

        // Проверка на null для родительской ветки
        if (createBranchDTO.getParentBranchId() != null) {
            Branch parentBranch = branchRepository.findById(createBranchDTO.getParentBranchId())
                    .orElseThrow(() -> new RuntimeException("Parent branch with ID " + createBranchDTO.getParentBranchId() + " not found"));
            branch.setParentBranch(parentBranch);
        }

        // Проверка на null для стартового сообщения
        if (createBranchDTO.getMessageStartId() != null) {
            Message messageStart = messageRepository.findById(createBranchDTO.getMessageStartId())
                    .orElseThrow(() -> new RuntimeException("Message start with ID " + createBranchDTO.getMessageStartId() + " not found"));
            branch.setMessageStart(messageStart);
        }

        branch.setIsRoot(!(branch.getParentBranch() != null || branch.getMessageStart() != null));
        branch.setDateEdit(java.time.LocalDateTime.now());
        branch.setDateCreate(java.time.LocalDateTime.now());

        return branch;
    }

    // Метод проверки ветки конкретному пользователю
    public Branch getBranchByIdWithAccessCheck(Long chatId, String uidFirebase) throws AccessDeniedException {
        Branch branch = branchRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Chat with ID " + chatId + " not found"));

        if (!Objects.equals(branch.getUser().getUidFirebase(), uidFirebase)) {
            throw  new AccessDeniedException("Access denied for user UID: " + uidFirebase);
        }

        return branch;
    }

    // Метод удаления ветки по ID
    @Transactional
    public void deleteBranchById(Long branchId) {
        branchRepository.findById(branchId).orElseThrow(() -> new RuntimeException("Branch with ID " + branchId + " not found"));

        branchRepository.deleteById(branchId);
    }

    // Метод удаления ветки по ID с валидацией
    @Transactional
    public void deleteBranchByIdWithAccessCheck(Long branchId, String uidFirebase) throws AccessDeniedException {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch with ID " + branchId + " not found"));
        if (!Objects.equals(branch.getUser().getUidFirebase(), uidFirebase)){
            throw new AccessDeniedException("User does not have access to this chat");
        }

        branchRepository.deleteById(branchId);
    }

    public void updateBranchAndChatDates(Branch branch) {
        // Обновляем дату изменения у ветки
        branch.setDateEdit(LocalDateTime.now());
        saveBranch(branch);

        // Обновляем дату изменения у чата
        Chat chat = branch.getChat();
        chat.setDateEdit(LocalDateTime.now());
        chatService.saveChat(chat);
    }
}
