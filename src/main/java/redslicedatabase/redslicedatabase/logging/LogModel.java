package redslicedatabase.redslicedatabase.logging;

/*
Общий файл для логов, для сокращения кода
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import redslicedatabase.redslicedatabase.model.Branch;
import redslicedatabase.redslicedatabase.model.Chat;
import redslicedatabase.redslicedatabase.model.Message;
import redslicedatabase.redslicedatabase.model.User;

@Component
public class LogModel {
    private static final Logger logger = LoggerFactory.getLogger(LogModel.class);

    // Логи для пользователя
    public void logger(User user, String method) {
        logger.info("Method UserController: {}", method);
        logger.info("User ID: {}", user.getId());
        logger.debug("User Email: {}", user.getEmail());
        logger.debug("User uidFirebase: {}", user.getUidFirebase());
        logger.debug("User total tokens: {}", user.getTotalTokens());
        logger.debug("User starred chat: {}", user.getStarredChat() != null ? user.getStarredChat().getId() : "null");
        logger.debug("Date create: {}", user.getDateCreate());
    }

    // Логи для чата
    public void logger(Chat chat, String method) {
        logger.info("Method ChatController: {}", method);
        logger.info("Chat ID: {}", chat.getId() != null ? chat.getId() : "null");
        logger.info("User ID: {}", chat.getUser() != null ? chat.getUser().getId() : "null");
        logger.debug("Chat Name: {}", chat.getChatName());
        logger.debug("Temperature: {}", chat.getTemperature());
        logger.debug("Context: {}", chat.getContext());
        logger.debug("Model URI: {}", chat.getModelUri());
        logger.debug("Selected Branch ID: {}", chat.getSelectedBranch() != null ? chat.getSelectedBranch().getId() : "null");
        logger.debug("Date Edit: {}", chat.getDateEdit());
        logger.debug("Date Create: {}", chat.getDateCreate());
    }

    // Логи для ветки
    public void logger(Branch branch, String method) {
        logger.info("Method BranchController: {}", method);
        logger.info("Branch ID: {}", branch.getId() != null ? branch.getId() : "null");
        logger.info("Chat ID: {}", branch.getChat().getId());
        logger.debug("Branch parent: {}", branch.getParentBranch() != null ? branch.getParentBranch().getId() : "null");
        logger.debug("Branch start message: {}", branch.getMessageStart() != null ? branch.getMessageStart().getId() : "null");
        logger.debug("Branch is rooted: {}", branch.getIsRoot().toString());
        logger.debug("Date Edit: {}", branch.getDateEdit());
        logger.debug("Date Create: {}", branch.getDateCreate());
    }

    // Логи для сообщения
    public void logger(Message message, String method) {
        logger.info("Method MessageController: {}", method);
        logger.info("Message ID: {}", message.getId() != null ? message.getId() : "null");
        logger.info("Branch ID: {}", message.getBranch());
        logger.debug("Role: {}", message.getRole());
        logger.debug("Text: {}", message.getText());
        logger.debug("Total tokens: {}", message.getTotalTokens());
        logger.debug("Input tokens: {}", message.getInputTokens());
        logger.debug("Completion tokens: {}", message.getCompletionTokens());
        logger.debug("Date create: {}", message.getDateCreate());
    }
}
