package ru.otus.library.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.library.dao.impl.AuthorDaoImpl;
import ru.otus.library.domain.Author;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Import(AuthorDaoImpl.class)
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorDaoImplTest extends AbstractPostgreSQLContainerTest {

    private static final String EXISTS_AUTHOR_NAME_ESENIN = "Сергей Есенин";
    private static final String EXISTS_AUTHOR_NAME_IVANOV = "Алексей Иванов";
    private static final String EXISTS_AUTHOR_NAME_NABOKOV = "Владимир Набоков";
    private static final String NEW_AUTHOR_NAME = "Александр Дюма";
    private static final int AUTHORS_QUANTITY = 3;

    @Autowired
    private AuthorDao authorDao;

    @Sql("/sql/authors.sql")
    @Test
    void insertNotExistsAuthorTest() {
        Author author = new Author(NEW_AUTHOR_NAME);
        var id = authorDao.insert(author).getId();
        var authorAfterInsert = authorDao.getById(id);
        assertNotNull(authorAfterInsert);
    }

    @Sql("/sql/authors.sql")
    @Test
    void insertExistsAuthorTest() {
        var exception = assertThrows(
                DuplicateKeyException.class, () -> authorDao.insert(new Author(EXISTS_AUTHOR_NAME_ESENIN)));
        assertTrue(Objects.nonNull(exception.getMessage()));
        assertTrue(exception.getMessage().contains("authors_u1"));
    }

    @Sql("/sql/authors.sql")
    @Test
    void getAllAuthorsTest() {
        var authors = authorDao.getAll();
        assertEquals(AUTHORS_QUANTITY, authors.size());

        var authorNames = authors.stream().map(Author::getName).collect(Collectors.toList());
        assertTrue(authorNames.contains(EXISTS_AUTHOR_NAME_ESENIN));
        assertTrue(authorNames.contains(EXISTS_AUTHOR_NAME_IVANOV));
        assertTrue(authorNames.contains(EXISTS_AUTHOR_NAME_NABOKOV));
    }

    @Sql("/sql/authors.sql")
    @Test
    void getExistsAuthorByIdTest() {
        assertEquals(EXISTS_AUTHOR_NAME_ESENIN, authorDao.getById(2).getName());
    }

    @Sql("/sql/authors.sql")
    @Test
    void getNotExistsAuthorByIdTest() {
        assertNull(authorDao.getById(100));
    }

    @Sql("/sql/authors.sql")
    @Test
    void getExistsAuthorByNameTest() {
        assertNotNull(authorDao.getByName(EXISTS_AUTHOR_NAME_NABOKOV));
    }

    @Sql("/sql/authors.sql")
    @Test
    void getNotExistsAuthorByNameTest() {
        assertNull(authorDao.getByName(NEW_AUTHOR_NAME));
    }

    @Sql("/sql/authors.sql")
    @Test
    void updateAuthor() {
        var author = authorDao.getById(2);
        author.setName(NEW_AUTHOR_NAME);
        authorDao.update(author);
        var authorAfterUpdate = authorDao.getById(2);
        assertEquals(NEW_AUTHOR_NAME, authorAfterUpdate.getName());
    }

    @Sql("/sql/authors.sql")
    @Test
    void deleteAuthor() {
        var author = authorDao.getById(2);
        authorDao.delete(author);
        var authors = authorDao.getAll();
        assertEquals(AUTHORS_QUANTITY - 1, authors.size());
    }

}
