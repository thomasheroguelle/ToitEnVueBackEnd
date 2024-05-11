package com.masterpiece.ToitEnVueBackEnd.repository.user;

import com.masterpiece.ToitEnVueBackEnd.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    User findUserById(Long id);

}
