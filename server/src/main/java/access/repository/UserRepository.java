package access.repository;

import access.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryRewriter;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, QueryRewriter {

    Optional<User> findBySubIgnoreCase(String sub);

}
