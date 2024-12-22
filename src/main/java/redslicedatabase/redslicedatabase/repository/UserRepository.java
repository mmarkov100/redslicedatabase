package redslicedatabase.redslicedatabase.repository;

/*
Репозиторий для пользователей
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import redslicedatabase.redslicedatabase.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Найти пользователя по email
    Optional<User> findByEmail(String email);
    // Найти пользователя по id firebas'a
    Optional<User> findByUidFirebase(String uidFirebase);
}
