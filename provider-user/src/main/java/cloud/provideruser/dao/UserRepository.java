package cloud.provideruser.dao;

import cloud.provideruser.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Created  by  jinboYu  on  2019/2/14
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
