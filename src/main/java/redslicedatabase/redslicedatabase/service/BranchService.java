package redslicedatabase.redslicedatabase.service;

/*
Сервис для веток
 */

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redslicedatabase.redslicedatabase.dto.BranchDTO.inbound.CreateBranchDTO;
import redslicedatabase.redslicedatabase.dto.BranchDTO.outbound.BranchDTO;
import redslicedatabase.redslicedatabase.model.Branch;
import redslicedatabase.redslicedatabase.model.Chat;
import redslicedatabase.redslicedatabase.model.Message;
import redslicedatabase.redslicedatabase.repository.BranchRepository;
import redslicedatabase.redslicedatabase.repository.ChatRepository;
import redslicedatabase.redslicedatabase.repository.MessageRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MessageRepository messageRepository;

    // Метод создания ветки
    public Branch createBranch(Branch branch) {
        return branchRepository.save(branch);
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

    // Метод конвертации Branch в BranchDTO
    public BranchDTO convertToDTO(Branch branch) {
        return new BranchDTO(
                branch.getId(),
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
                .map(branch -> new BranchDTO(
                        branch.getId(),
                        branch.getChat() != null ? branch.getChat().getId() : null,
                        branch.getParentBranch() != null ? branch.getParentBranch().getId() : null,
                        branch.getMessageStart() != null ? branch.getMessageStart().getId() : null,
                        branch.getIsRoot(),
                        branch.getDateEdit(),
                        branch.getDateCreate()
                )).toList();
    }

    public Branch convertToModel(CreateBranchDTO createBranchDTO) {
        // Ищем чат, к которому привязана ветка
        Chat chat = chatRepository.findById(createBranchDTO.getChatId())
                .orElseThrow(() -> new RuntimeException("Chat with ID " + createBranchDTO.getChatId() + " not found"));

        // Создаем новый объект ветки и сохраняем данные
        Branch branch = new Branch();
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

    // Метод удаления ветки по ID
    @Transactional
    public void deleteBranchById(Long id) {
        if (!branchRepository.existsById(id)) {
            throw new RuntimeException("Branch with ID " + id + " not found.");
        }

        branchRepository.deleteById(id);
    }
}
