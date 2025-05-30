package io.project.worktousers.repository;

import io.project.worktousers.entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findById(Long id);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
