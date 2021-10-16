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
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.dao.impl.AuthorDaoImpl;
import ru.otus.library.domain.Author;
import ru.otus.library.service.impl.AuthorServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import({AuthorDaoImpl.class, AuthorServiceImpl.class})
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorServiceImplTest extends AbstractPostgreSQLContainerTest {

    private static final String NEW_AUTHOR_NAME = "Александр Дюма";
    private static final String EXISTS_AUTHOR_NAME_ESENIN = "Сергей Есенин";

    private static final String EXISTS_CAPTOR = "already exists";
    private static final String DOES_NOT_EXIST_CAPTOR = "does not exist";
    private static final String SUCCESS_CAPTOR = "successfully";

    @Autowired
    private AuthorService authorService;

    @SpyBean
    private AuthorDao authorDao;

    @MockBean
    private InputOutputComponent inputOutputComponent;

    @Captor
    private ArgumentCaptor<String> captor;

    @Sql("/sql/authors.sql")
    @Test
    void createNotExistsAuthorTest() {
        var author = authorService.create(NEW_AUTHOR_NAME);
        var authorAfterInsert = authorDao.getById(author.getId());
        assertNotNull(authorAfterInsert);
        verify(authorDao, times(1)).insert(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/authors.sql")
    @Test
    void createExistsAuthorTest() {
        doNothing().when(inputOutputComponent).write(any());
        var author = authorService.create(EXISTS_AUTHOR_NAME_ESENIN);
        var authorAfterInsert = authorDao.getById(author.getId());
        assertNotNull(authorAfterInsert);
        verify(authorDao, times(0)).insert(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(EXISTS_CAPTOR));
    }

    @Sql("/sql/authors.sql")
    @Test
    void updateNotExistsAuthorByIdTest() {
        doNothing().when(inputOutputComponent).write(any());
        var author = new Author(10, NEW_AUTHOR_NAME);
        authorService.updateById(author.getId(), NEW_AUTHOR_NAME);
        verify(authorDao, times(0)).update(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Sql("/sql/authors.sql")
    @Test
    void updateExistsAuthorByIdTest() {
        var existsAuthor = authorDao.getById(2);
        authorService.updateById(existsAuthor.getId(), NEW_AUTHOR_NAME);
        var authorAfterUpdate = authorDao.getById(2);
        assertNotNull(NEW_AUTHOR_NAME, authorAfterUpdate.getName());
        verify(authorDao, times(1)).update(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/authors.sql")
    @Test
    void deleteNotExistsAuthorByIdTest() {
        doNothing().when(inputOutputComponent).write(any());
        authorService.deleteById(10);
        verify(authorDao, times(0)).delete(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Sql("/sql/authors.sql")
    @Test
    void deleteExistsAuthorByIdTest() {
        var existsAuthor = authorDao.getById(2);
        authorService.deleteById(existsAuthor.getId());
        var authorAfterDelete = authorDao.getById(2);
        assertNull(authorAfterDelete);
        verify(authorDao, times(1)).delete(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

}