package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Collection<Film> findAll();

    Optional<Film> findById(Long id);

    Film create(Film film);

    Film update(Film newFilm);

    void addLike(FilmDto film, User user);

    boolean removeLike(FilmDto film, User user);
}