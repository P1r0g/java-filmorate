package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> findAllUsers() {
        return users.values();
    }

    public User findUsersById(Long id) {
        return users.get(id);
    }

    public User create(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        User oldUser = users.get(user.getId());
        oldUser.setName(user.getName());
        oldUser.setBirthday(user.getBirthday());
        oldUser.setLogin(user.getLogin());
        oldUser.setEmail(user.getEmail());
        return oldUser;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private Long getNextId() {
        long currentMaxId = users.keySet().stream().mapToLong(id -> id).max().orElse(0);
        return ++currentMaxId;
    }

}