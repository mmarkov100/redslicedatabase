package redslicedatabase.redslicedatabase.repository;

/*
Репозиторий для веток
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import redslicedatabase.redslicedatabase.model.Branch;

import java.util.List;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    // Найти список веток по id чата
    List<Branch> findByChatId(Long chatId);
    // Получить дочерние ветки по ID родительской ветки
    List<Branch> findByParentBranchId(Long parentBranchId);
    // Поиск веток по id пользователя
    @Query("SELECT b FROM Branch b WHERE b.chat.user.id = :userId")
    List<Branch> findAllByUserId(@Param("userId") Long userId);
    // Поиск веток по uid файрбейза
    @Query("SELECT b FROM Branch b WHERE b.chat.user.uidFirebase = :uidFirebase")
    List<Branch> findAllByUserUidFirebase(@Param("uidFirebase") String uidFirebase);
}
