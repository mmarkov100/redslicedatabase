package redslicedatabase.redslicedatabase.repository;

/*
Репозиторий для сообщений
 */

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import redslicedatabase.redslicedatabase.model.Message;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // Найти все сообщения в ветке по id ветки отсортированные по дате
    List<Message> findByBranchIdOrderByDateCreate(Long branch_id, Sort dateCreate);
}
