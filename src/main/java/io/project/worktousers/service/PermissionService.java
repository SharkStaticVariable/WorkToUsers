package io.project.worktousers.service;

import io.project.worktousers.entity.Users;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PermissionService {
    public void checkIsAdmin(Users user) {
        if (!"ADMIN".equals(user.getRole().getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Доступ запрещён");
        }
    }
}