package io.project.worktousers.service;

import io.project.worktousers.entity.Role;
import io.project.worktousers.entity.Users;
import io.project.worktousers.repository.RoleRepository;
import io.project.worktousers.repository.UserRepository;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CacheManager cacheManager;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.cacheManager = cacheManager;
    }

public Users register(Users user) {
    if (userRepository.existsByEmail(user.getEmail())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введенный Email уже используется");
    }
    if (userRepository.existsByUsername(user.getUsername())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введенный Username уже используется");
    }
    Role defaultRole = roleRepository.findByName("USER").orElseThrow();
    user.setRole(defaultRole);
    Users savedUser = userRepository.save(user);
    cacheManager.getCache("users").clear();
    return savedUser;
}

    public Users getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));
    }

    public Users update(Long id, Users updates) {
        Users user = getById(id);

        if (updates.getUsername() != null && !updates.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(updates.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
            }
            user.setUsername(updates.getUsername());
        }

        if (updates.getName() != null) {
            user.setName(updates.getName());
        }

        return userRepository.save(user);
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден!");
        }
        userRepository.deleteById(id);
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }


}