package org.raf.usermanagement.db;

import org.raf.usermanagement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByEmail (String email);
    Optional<User> findUserByEmailAndPassword (String email, String password);
}
