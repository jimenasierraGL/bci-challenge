package bci.challenge.persistence;

import bci.challenge.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
    Optional<User> findByToken(String token);
    Optional<User> findByIdOrEmail(UUID id, String email);
}
