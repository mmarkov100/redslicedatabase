package redslicedatabase.redslicedatabase.repository;

/*
Репозиторий для веток
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import redslicedatabase.redslicedatabase.model.Branch;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    // Найти список веток по id чата
    List<Branch> findByChatId(Long chatId);
    // Получить дочерние ветки по ID родительской ветки
    List<Branch> findByParentBranchId(Long parentBranchId);
}
