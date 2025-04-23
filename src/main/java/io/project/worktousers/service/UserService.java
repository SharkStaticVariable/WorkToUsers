package io.project.worktousers.service;

import io.project.worktousers.entity.Users;
import io.project.worktousers.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users register(Users user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введенный Email уже используется");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Введенный Username уже используется");
        }
        return userRepository.save(user);
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


}