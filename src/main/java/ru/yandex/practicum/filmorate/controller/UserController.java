package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAllUsers() {
        log.info("GET /user");
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("POST /user/{}", user.getName());
        // проверяем выполнение необходимых условий
        validateUser(user);
        // формируем дополнительные данные
        user.setId(getNextId());
        // сохраняем нового пользователя в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (log.isInfoEnabled()) {
            log.info("PUT /user/{}", user.getName());
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пост с id = " + user.getId() + " не найден");
        }
        User updatedUser = users.get(user.getId());
        validateUser(user);
        updatedUser.setName(user.getName());
        updatedUser.setBirthday(user.getBirthday());
        updatedUser.setLogin(user.getLogin());
        updatedUser.setEmail(user.getEmail());
        users.put(updatedUser.getId(), updatedUser);
        return updatedUser;

    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }
}
