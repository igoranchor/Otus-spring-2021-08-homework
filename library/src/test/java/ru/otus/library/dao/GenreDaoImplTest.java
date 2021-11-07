package ru.otus.library.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.library.dao.impl.GenreDaoImpl;
import ru.otus.library.domain.Genre;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Import(GenreDaoImpl.class)
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreDaoImplTest extends AbstractPostgreSQLContainerTest {

    private static final String EXISTS_GENRE_FANTASY = "фэнтези";
    private static final String EXISTS_GENRE_SPY = "шпионский роман";
    private static final String EXISTS_GENRE_FABLE = "басня";
    private static final String NEW_GENRE = "бульварное чтиво";
    private static final int GENRES_QUANTITY = 3;

    @Autowired
    private GenreDao genreDao;

    @Sql("/sql/genres.sql")
    @Test
    void insertNotExistsGenreTest() {
        Genre genre = new Genre(NEW_GENRE);
        var id = genreDao.insert(genre).getId();
        var genreAfterInsert = genreDao.getById(id);
        assertNotNull(genreAfterInsert);
    }

    @Sql("/sql/genres.sql")
    @Test
    void insertExistsGenreTest() {
        var exception = assertThrows(
                DuplicateKeyException.class, () -> genreDao.insert(new Genre(EXISTS_GENRE_FANTASY)));
        assertTrue(Objects.nonNull(exception.getMessage()));
        assertTrue(exception.getMessage().contains("genres_u1"));
    }

    @Sql("/sql/genres.sql")
    @Test
    void getAllGenresTest() {
        var genres = genreDao.getAll();
        assertEquals(GENRES_QUANTITY, genres.size());

        var GenreNames = genres.stream().map(Genre::getName).collect(Collectors.toList());
        assertTrue(GenreNames.contains(EXISTS_GENRE_FANTASY));
        assertTrue(GenreNames.contains(EXISTS_GENRE_SPY));
        assertTrue(GenreNames.contains(EXISTS_GENRE_FABLE));
    }

    @Sql("/sql/genres.sql")
    @Test
    void getExistsGenreByIdTest() {
        assertEquals(EXISTS_GENRE_FANTASY, genreDao.getById(2).getName());
    }

    @Sql("/sql/genres.sql")
    @Test
    void getNotExistsGenreByIdTest() {
        assertNull(genreDao.getById(100));
    }

    @Sql("/sql/genres.sql")
    @Test
    void getExistsGenreByNameTest() {
        assertNotNull(genreDao.getByName(EXISTS_GENRE_SPY));
    }

    @Sql("/sql/genres.sql")
    @Test
    void getNotExistsGenreByNameTest() {
        assertNull(genreDao.getByName(NEW_GENRE));
    }

    @Sql("/sql/genres.sql")
    @Test
    void updateGenre() {
        var genre = genreDao.getById(2);
        genre.setName(NEW_GENRE);
        genreDao.update(genre);
        var genreAfterUpdate = genreDao.getById(2);
        assertEquals(NEW_GENRE, genreAfterUpdate.getName());
    }

    @Sql("/sql/genres.sql")
    @Test
    void deleteGenre() {
        var genre = genreDao.getById(2);
        genreDao.delete(genre);
        var genres = genreDao.getAll();
        assertEquals(GENRES_QUANTITY - 1, genres.size());
    }

}
