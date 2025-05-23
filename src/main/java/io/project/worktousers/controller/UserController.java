package io.project.worktousers.controller;

import io.project.worktousers.entity.Users;
import io.project.worktousers.service.PermissionService;
import io.project.worktousers.service.UserService;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PermissionService permissionService;
    private final CacheManager cacheManager;

    public UserController(UserService userService, PermissionService permissionService,  CacheManager cacheManager) {
        this.userService = userService;
        this.permissionService = permissionService;
        this.cacheManager = cacheManager;
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

    @GetMapping("/all")
    @Cacheable("users")
    public ResponseEntity<List<Users>> getAllUsers(@RequestHeader("User-Id") Long userId) {
        Users currentUser = userService.getById(userId);
        permissionService.checkIsAdmin(currentUser);
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Void> deleteUserByAdmin(@RequestHeader("User-Id") Long adminId, @PathVariable Long id) {
        Users currentUser = userService.getById(adminId);
        permissionService.checkIsAdmin(currentUser);
        userService.delete(id);
        cacheManager.getCache("users").clear(); // сброс кэша
        return ResponseEntity.noContent().build();
    }
}