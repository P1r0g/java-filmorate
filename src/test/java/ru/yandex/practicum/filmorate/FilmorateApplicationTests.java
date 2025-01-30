package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class FilmorateApplicationTests {

	private FilmController filmController;
	private UserController userController;

	@BeforeEach
	public void setUp() {
		filmController = new FilmController();
		userController = new UserController();
	}

	@Test
	public void testValidFilm() {
		Film film = new Film();
		film.setName("Inception");
		film.setDescription("A mind-bending thriller");
		film.setReleaseDate(LocalDate.of(2010, 7, 16));
		film.setDuration(148);

		assertDoesNotThrow(() -> filmController.addFilm(film));
	}

	@Test
	public void testInvalidFilmName() {
		Film film = new Film();
		film.setName("");
		film.setDescription("A mind-bending thriller");
		film.setReleaseDate(LocalDate.of(2010, 7, 16));
		film.setDuration(148);

		ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
		assertEquals("Название фильма не может быть пустым", exception.getMessage());
	}

	@Test
	public void testInvalidFilmReleaseDate() {
		Film film = new Film();
		film.setName("Inception");
		film.setDescription("A mind-bending thriller");
		film.setReleaseDate(LocalDate.of(1890, 1, 1));
		film.setDuration(148);

		ValidationException exception = assertThrows(ValidationException.class, () -> filmController.addFilm(film));
		assertEquals("дата релиза — не раньше 28 декабря 1895 года;", exception.getMessage());
	}

	@Test
	public void testValidUser() {
		User user = new User();
		user.setEmail("user@example.com");
		user.setLogin("user123");
		user.setName("User Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));

		assertDoesNotThrow(() -> userController.createUser(user));
	}

	@Test
	public void testInvalidUserEmail() {
		User user = new User();
		user.setEmail("userexample.com");
		user.setLogin("user123");
		user.setName("User Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));

		ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
		assertEquals("электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
	}

	@Test
	public void testInvalidUserLogin() {
		User user = new User();
		user.setEmail("user@example.com");
		user.setLogin("user 123");
		user.setName("User Name");
		user.setBirthday(LocalDate.of(1990, 1, 1));

		ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
		assertEquals("логин не может быть пустым и содержать пробелы", exception.getMessage());
	}

	@Test
	public void testInvalidUserBirthday() {
		User user = new User();
		user.setEmail("user@example.com");
		user.setLogin("user123");
		user.setName("User Name");
		user.setBirthday(LocalDate.now().plusDays(1));

		ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
		assertEquals("дата рождения не может быть в будущем", exception.getMessage());
	}
}
