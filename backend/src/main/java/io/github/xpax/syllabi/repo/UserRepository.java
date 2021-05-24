package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.UserWithoutPassword;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Page<UserWithoutPassword> findAllProjectedBy(Pageable page);
}
