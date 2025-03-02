package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private long like;
    private Set<Long> likes = new HashSet<>();
    private Genre genre;
    private Set<Genre> genres = new HashSet<>();
    private MPA rating;

}
