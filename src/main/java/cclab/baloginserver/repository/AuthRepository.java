package cclab.baloginserver.repository;

import cclab.baloginserver.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByUuid(String uuid);
}
