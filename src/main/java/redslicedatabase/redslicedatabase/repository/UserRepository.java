package redslicedatabase.redslicedatabase.repository;

/*
Репозиторий для пользователей
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import redslicedatabase.redslicedatabase.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Найти пользователя по email
    User findByEmail(String email);
    // Найти пользователя по id firebas'a
    User findByUidFirebase(String uidFirebase);
}
