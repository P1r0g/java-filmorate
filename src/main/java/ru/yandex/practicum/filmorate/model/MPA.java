package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

@Data
public class MPA {
    private Integer id;
    @NonNull
    private String name;
}