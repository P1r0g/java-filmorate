package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private long filmIdCounter = 1;

    // Генерация следующего идентификатора
    private synchronized long getNextId() {
        return filmIdCounter++;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        try {
            validateFilm(film);
            film.setId(getNextId());
            films.add(film);
            log.info("Film added: {}", film);
            return film;
        } catch (ValidationException e) {
            log.error("Failed to add film: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        try {
            validateFilm(film);
            for (int i = 0; i < films.size(); i++) {
                if (films.get(i).getId() == film.getId()) {
                    films.set(i, film);
                    log.info("Film updated: {}", film);
                    return film;
                }
            }
            throw new IllegalArgumentException("Film not found");
        } catch (ValidationException | IllegalArgumentException e) {
            log.error("Failed to update film: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films;
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Film name cannot be empty");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Film description cannot exceed 200 characters");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Release date cannot be before December 28, 1895");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Film duration must be a positive number");
        }
    }
}