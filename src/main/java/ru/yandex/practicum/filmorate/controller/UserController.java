package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final List<User> users = new ArrayList<>();
    private long userIdCounter = 1;

    // Генерация следующего идентификатора
    private synchronized long getNextId() {
        return userIdCounter++;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        try {
            validateUser(user);
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            user.setId(getNextId());
            users.add(user);
            log.info("User created: {}", user);
            return user;
        } catch (ValidationException e) {
            log.error("Failed to create user: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        try {
            validateUser(user);
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getId() == user.getId()) {
                    users.set(i, user);
                    log.info("User updated: {}", user);
                    return user;
                }
            }
            throw new IllegalArgumentException("User not found");
        } catch (ValidationException | IllegalArgumentException e) {
            log.error("Failed to update user: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email must not be empty and must contain '@'");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Login must not be empty and must not contain spaces");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday cannot be in the future");
        }
    }
}
