package io.project.worktousers.controller;

import io.project.worktousers.entity.Users;
import io.project.worktousers.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/save")
    public ResponseEntity<Users> register(@RequestBody @Validated Users user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @GetMapping("/get")
    public ResponseEntity<Users> getCurrentUser(@RequestHeader("User-Id") Long userId) {
        return ResponseEntity.ok(userService.getById(userId));
    }

    @PutMapping("/update")
    public ResponseEntity<Users> update(
            @RequestHeader("User-Id") Long userId,
            @RequestBody @Validated Users updates) {
        return ResponseEntity.ok(userService.update(userId, updates));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestHeader("User-Id") Long userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}