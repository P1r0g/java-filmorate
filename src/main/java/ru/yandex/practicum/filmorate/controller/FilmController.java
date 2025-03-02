package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Collection<Film> findAllFilms() {
        log.info("GET /film");
        return filmService.findAllFilms();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("POST /film/{}", film.getName());
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("PUT /film/{}", film.getName());
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{user-id}")
    public Film addLike(@PathVariable Long id, @PathVariable("user-id") Long idUser) {
        return filmService.addLike(id, idUser);
    }

    @DeleteMapping("/{id}/like/{user-id}")
    public Film deleteLike(@PathVariable Long id, @PathVariable("user-id") Long idUser) {
        return filmService.deleteLike(id, idUser);
    }

    @GetMapping("/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getTop(count);
    }

}