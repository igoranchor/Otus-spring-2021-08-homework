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
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.dao.impl.AuthorDaoImpl;
import ru.otus.library.domain.Author;
import ru.otus.library.service.impl.AuthorServiceImpl;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import({AuthorDaoImpl.class, AuthorServiceImpl.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorServiceImplTest extends AbstractPostgreSQLContainerTest {

    private static final String NEW_AUTHOR_NAME = "Александр Дюма";
    private static final BigInteger EXISTS_AUTHOR_NAME_ID = BigInteger.valueOf(2);
    private static final String EXISTS_AUTHOR_NAME_ESENIN = "Сергей Есенин";

    private static final String EXISTS_CAPTOR = "already exists";
    private static final String DOES_NOT_EXIST_CAPTOR = "does not exist";
    private static final String SUCCESS_CAPTOR = "successfully";

    @Autowired
    private AuthorService authorService;

    @Autowired
    private TestEntityManager em;

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
        em.detach(author);
        var authorAfterInsert = em.find(Author.class, author.getId());
        assertNotNull(authorAfterInsert);
        verify(authorDao, times(1)).save(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/authors.sql")
    @Test
    void createExistsAuthorTest() {
        var author = authorService.create(EXISTS_AUTHOR_NAME_ESENIN);
        em.detach(author);
        var authorAfterInsert = em.find(Author.class, author.getId());
        assertNotNull(authorAfterInsert);
        verify(authorDao, times(0)).save(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(EXISTS_CAPTOR));
    }

    @Sql("/sql/authors.sql")
    @Test
    void updateNotExistsAuthorByIdTest() {
        var author = new Author(NEW_AUTHOR_NAME);
        author.setId(BigInteger.valueOf(10));
        authorService.updateById(author.getId(), NEW_AUTHOR_NAME);
        em.detach(author);
        verify(authorDao, times(0)).save(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Sql("/sql/authors.sql")
    @Test
    void updateExistsAuthorByIdTest() {
        var existsAuthor = em.find(Author.class, EXISTS_AUTHOR_NAME_ID);
        em.detach(existsAuthor);
        authorService.updateById(existsAuthor.getId(), NEW_AUTHOR_NAME);
        em.detach(existsAuthor);
        var authorAfterUpdate = em.find(Author.class, EXISTS_AUTHOR_NAME_ID);
        assertEquals(NEW_AUTHOR_NAME, authorAfterUpdate.getName());
        verify(authorDao, times(1)).save(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/authors.sql")
    @Test
    void deleteNotExistsAuthorByIdTest() {
        authorService.deleteById(BigInteger.valueOf(10));
        verify(authorDao, times(0)).delete(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Sql("/sql/authors.sql")
    @Test
    void deleteExistsAuthorByIdTest() {
        var existsAuthor = em.find(Author.class, EXISTS_AUTHOR_NAME_ID);
        authorService.deleteById(existsAuthor.getId());
        var authorAfterDelete = em.find(Author.class, EXISTS_AUTHOR_NAME_ID);
        assertNull(authorAfterDelete);
        verify(authorDao, times(1)).delete(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

}