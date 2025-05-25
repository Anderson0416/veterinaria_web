package com.veterinaria.vet.repositories;

import com.veterinaria.vet.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    List<User> findByRolId(Long rolId);
}
