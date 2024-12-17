package redslicedatabase.redslicedatabase.repository;

/*
Репозиторий для чатов
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import redslicedatabase.redslicedatabase.model.Chat;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    // Найти чаты по ID пользователя
    List<Chat> findByUserId(Long userId);
}
