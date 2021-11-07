package ru.otus.library.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.dao.AbstractPostgreSQLContainerTest;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.dao.impl.GenreDaoImpl;
import ru.otus.library.domain.Genre;
import ru.otus.library.service.impl.GenreServiceImpl;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import({GenreDaoImpl.class, GenreServiceImpl.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreServiceImplTest extends AbstractPostgreSQLContainerTest {

    private static final String NEW_GENRE = "бульварное чтиво";
    private static final BigInteger EXISTS_GENRE_ID = BigInteger.valueOf(2);
    private static final String EXISTS_GENRE = "фэнтези";

    private static final String EXISTS_CAPTOR = "already exists";
    private static final String DOES_NOT_EXIST_CAPTOR = "does not exist";
    private static final String SUCCESS_CAPTOR = "successfully";

    @Autowired
    private GenreService genreService;

    @Autowired
    private TestEntityManager em;

    @SpyBean
    private GenreDao genreDao;

    @MockBean
    private InputOutputComponent inputOutputComponent;

    @Captor
    private ArgumentCaptor<String> captor;

    @Sql("/sql/genres.sql")
    @Test
    void createNotExistsGenreTest() {
        var genre = genreService.create(NEW_GENRE);
        em.detach(genre);
        var genreAfterInsert = em.find(Genre.class, genre.getId());
        assertNotNull(genreAfterInsert);
        verify(genreDao, times(1)).save(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/genres.sql")
    @Test
    void createExistsGenreTest() {
        var genre = genreService.create(EXISTS_GENRE);
        em.detach(genre);
        var genreAfterInsert = em.find(Genre.class, genre.getId());
        assertNotNull(genreAfterInsert);
        verify(genreDao, times(0)).save(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(EXISTS_CAPTOR));
    }

    @Sql("/sql/genres.sql")
    @Test
    void updateNotExistsGenreByIdTest() {
        Genre genre = new Genre(NEW_GENRE);
        genre.setId(BigInteger.valueOf(10));
        genreService.updateById(genre.getId(), NEW_GENRE);
        em.detach(genre);
        verify(genreDao, times(0)).save(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Sql("/sql/genres.sql")
    @Test
    void updateExistsGenreByIdTest() {
        var existsGenre = em.find(Genre.class, EXISTS_GENRE_ID);
        genreService.updateById(existsGenre.getId(), NEW_GENRE);
        em.detach(existsGenre);
        var genreAfterUpdate = em.find(Genre.class, EXISTS_GENRE_ID);
        assertNotNull(NEW_GENRE, genreAfterUpdate.getName());
        verify(genreDao, times(1)).save(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/genres.sql")
    @Test
    void deleteNotExistsGenreByIdTest() {
        genreService.deleteById(BigInteger.valueOf(10));
        verify(genreDao, times(0)).delete(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Sql("/sql/genres.sql")
    @Test
    void deleteExistsGenreByIdTest() {
        var existsGenre = em.find(Genre.class, EXISTS_GENRE_ID);
        genreService.deleteById(existsGenre.getId());
        em.detach(existsGenre);
        var genreAfterDelete = em.find(Genre.class, EXISTS_GENRE_ID);
        assertNull(genreAfterDelete);
        verify(genreDao, times(1)).delete(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

}