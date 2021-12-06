package ru.otus.library.dao;

import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.library.domain.Genre;

import java.math.BigInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreDaoImplTest extends AbstractPostgreSQLContainerTest {

    private static final String EXISTS_GENRE_FANTASY = "фэнтези";
    private static final String EXISTS_GENRE_SPY = "шпионский роман";
    private static final String EXISTS_GENRE_FABLE = "басня";
    private static final String NEW_GENRE = "бульварное чтиво";
    private static final int GENRES_QUANTITY = 3;

    private static final String NO_DATA_FOUND_EXCEPTION = "Не найдена доменная сущность";

    @Autowired
    private GenreDao genreDao;

    @Autowired
    private TestEntityManager em;

    @Sql("/sql/genres.sql")
    @Test
    void insertNotExistsGenreTest() {
        Genre genre = new Genre(NEW_GENRE);
        genreDao.save(genre);
        var genreAfterInsert = em.persistFlushFind(genre);
        assertNotNull(genreAfterInsert);
    }

    @Sql("/sql/genres.sql")
    @Test
    void insertExistsGenreTest() {
        var exception = assertThrows(
                DataIntegrityViolationException.class, () -> genreDao.save(new Genre(EXISTS_GENRE_FANTASY)));
        var causeException = exception.getCause().getCause();
        assertEquals(PSQLException.class, causeException.getClass());
        assertNotNull(exception.getMessage());
        assertTrue(causeException.getMessage().contains("genres_u1"));
    }

    @Sql("/sql/genres.sql")
    @Test
    void getAllGenresTest() {
        var genres = genreDao.findAll();
        assertEquals(GENRES_QUANTITY, genres.size());

        var GenreNames = genres.stream().map(Genre::getName).collect(Collectors.toList());
        assertTrue(GenreNames.contains(EXISTS_GENRE_FANTASY));
        assertTrue(GenreNames.contains(EXISTS_GENRE_SPY));
        assertTrue(GenreNames.contains(EXISTS_GENRE_FABLE));
    }

    @Sql("/sql/genres.sql")
    @Test
    void getExistsGenreByIdTest() {
        var genre = genreDao.findById(BigInteger.valueOf(2))
                .orElseThrow(() -> new RuntimeException(NO_DATA_FOUND_EXCEPTION));
        assertEquals(EXISTS_GENRE_FANTASY, genre.getName());
    }

    @Sql("/sql/genres.sql")
    @Test
    void getNotExistsGenreByIdTest() {
        var genre = genreDao.findById(BigInteger.valueOf(100));
        assertTrue(genre.isEmpty());
    }

    @Sql("/sql/genres.sql")
    @Test
    void getExistsGenreByNameTest() {
        var genre = genreDao.findByName(EXISTS_GENRE_SPY);
        assertFalse(genre.isEmpty());
    }

    @Sql("/sql/genres.sql")
    @Test
    void getNotExistsGenreByNameTest() {
        var genre = genreDao.findByName(NEW_GENRE);
        assertTrue(genre.isEmpty());
    }

    @Sql("/sql/genres.sql")
    @Test
    void updateGenre() {
        var genre = em.find(Genre.class, BigInteger.valueOf(2));
        genre.setName(NEW_GENRE);
        genreDao.save(genre);
        em.flush();
        em.detach(genre);
        var genreAfterUpdate = em.find(Genre.class, BigInteger.valueOf(2));
        assertEquals(NEW_GENRE, genreAfterUpdate.getName());
    }

    @Sql("/sql/genres.sql")
    @Test
    void deleteGenre() {
        var genre = em.find(Genre.class, BigInteger.valueOf(2));
        em.detach(genre);
        genreDao.delete(genre);
        var genreAfterDelete = em.find(Genre.class, BigInteger.valueOf(2));
        assertNull(genreAfterDelete);
    }

}
