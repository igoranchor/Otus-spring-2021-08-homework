package ru.otus.library.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import({GenreDaoImpl.class, GenreServiceImpl.class})
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class GenreServiceImplTest extends AbstractPostgreSQLContainerTest {

    private static final String NEW_GENRE = "бульварное чтиво";
    private static final String EXISTS_GENRE = "фэнтези";

    private static final String EXISTS_CAPTOR = "already exists";
    private static final String DOES_NOT_EXIST_CAPTOR = "does not exist";
    private static final String SUCCESS_CAPTOR = "successfully";

    @Autowired
    private GenreService genreService;

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
        var genreAfterInsert = genreDao.getById(genre.getId());
        assertNotNull(genreAfterInsert);
        verify(genreDao, times(1)).insert(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/genres.sql")
    @Test
    void createExistsGenreTest() {
        doNothing().when(inputOutputComponent).write(any());
        var genre = genreService.create(EXISTS_GENRE);
        var genreAfterInsert = genreDao.getById(genre.getId());
        assertNotNull(genreAfterInsert);
        verify(genreDao, times(0)).insert(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(EXISTS_CAPTOR));
    }

    @Sql("/sql/genres.sql")
    @Test
    void updateNotExistsGenreByIdTest() {
        doNothing().when(inputOutputComponent).write(any());
        Genre genre = new Genre(10, NEW_GENRE);
        genreService.updateById(genre.getId(), NEW_GENRE);
        verify(genreDao, times(0)).update(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Sql("/sql/genres.sql")
    @Test
    void updateExistsGenreByIdTest() {
        var existsGenre = genreDao.getById(2);
        genreService.updateById(existsGenre.getId(), NEW_GENRE);
        var genreAfterUpdate = genreDao.getById(2);
        assertNotNull(NEW_GENRE, genreAfterUpdate.getName());
        verify(genreDao, times(1)).update(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/genres.sql")
    @Test
    void deleteNotExistsGenreByIdTest() {
        doNothing().when(inputOutputComponent).write(any());
        genreService.deleteById(10);
        verify(genreDao, times(0)).delete(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Sql("/sql/genres.sql")
    @Test
    void deleteExistsGenreByIdTest() {
        var existsGenre = genreDao.getById(2);
        genreService.deleteById(existsGenre.getId());
        var genreAfterDelete = genreDao.getById(2);
        assertNull(genreAfterDelete);
        verify(genreDao, times(1)).delete(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

}