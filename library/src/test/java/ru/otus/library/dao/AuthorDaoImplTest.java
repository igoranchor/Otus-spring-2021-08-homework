package ru.otus.library.dao;

import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.library.domain.Author;

import java.math.BigInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorDaoImplTest extends AbstractPostgreSQLContainerTest {

    private static final String EXISTS_AUTHOR_NAME_ESENIN = "Сергей Есенин";
    private static final String EXISTS_AUTHOR_NAME_IVANOV = "Алексей Иванов";
    private static final String EXISTS_AUTHOR_NAME_NABOKOV = "Владимир Набоков";
    private static final String NEW_AUTHOR_NAME = "Александр Дюма";
    private static final int AUTHORS_QUANTITY = 3;

    private static final String NO_DATA_FOUND_EXCEPTION = "Не найдена доменная сущность";

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private TestEntityManager em;

    @Sql("/sql/authors.sql")
    @Test
    void insertNotExistsAuthorTest() {
        Author author = new Author(NEW_AUTHOR_NAME);
        authorDao.save(author);
        var authorAfterInsert = em.persistFlushFind(author);
        assertNotNull(authorAfterInsert);
    }

    @Sql("/sql/authors.sql")
    @Test
    void insertExistsAuthorTest() {
        var exception = assertThrows(
                DataIntegrityViolationException.class, () -> authorDao.save(new Author(EXISTS_AUTHOR_NAME_ESENIN)));
        var causeException = exception.getCause().getCause();
        assertEquals(PSQLException.class, causeException.getClass());
        assertNotNull(causeException.getMessage());
        assertTrue(causeException.getMessage().contains("authors_u1"));
    }

    @Sql("/sql/authors.sql")
    @Test
    void getAllAuthorsTest() {
        var authors = authorDao.findAll();
        assertEquals(AUTHORS_QUANTITY, authors.size());

        var authorNames = authors.stream().map(Author::getName).collect(Collectors.toList());
        assertTrue(authorNames.contains(EXISTS_AUTHOR_NAME_ESENIN));
        assertTrue(authorNames.contains(EXISTS_AUTHOR_NAME_IVANOV));
        assertTrue(authorNames.contains(EXISTS_AUTHOR_NAME_NABOKOV));
    }

    @Sql("/sql/authors.sql")
    @Test
    void getExistsAuthorByIdTest() {
        var author = authorDao.findById(BigInteger.valueOf(2))
                .orElseThrow(() -> new RuntimeException(NO_DATA_FOUND_EXCEPTION));
        assertEquals(EXISTS_AUTHOR_NAME_ESENIN, author.getName());
    }

    @Sql("/sql/authors.sql")
    @Test
    void getNotExistsAuthorByIdTest() {
        var author = authorDao.findById(BigInteger.valueOf(100));
        assertTrue(author.isEmpty());
    }

    @Sql("/sql/authors.sql")
    @Test
    void getExistsAuthorByNameTest() {
        var author = authorDao.findByName(EXISTS_AUTHOR_NAME_NABOKOV);
        assertFalse(author.isEmpty());
    }

    @Sql("/sql/authors.sql")
    @Test
    void getNotExistsAuthorByNameTest() {
        var author = authorDao.findByName(NEW_AUTHOR_NAME);
        assertTrue(author.isEmpty());
    }

    @Sql("/sql/authors.sql")
    @Test
    void updateAuthor() {
        var author = em.find(Author.class, BigInteger.valueOf(2));
        author.setName(NEW_AUTHOR_NAME);
        authorDao.save(author);
        em.flush();
        em.detach(author);
        var authorAfterUpdate = em.find(Author.class, BigInteger.valueOf(2));
        assertEquals(NEW_AUTHOR_NAME, authorAfterUpdate.getName());
    }

    @Sql("/sql/authors.sql")
    @Test
    void deleteAuthor() {
        var author = em.find(Author.class, BigInteger.valueOf(2));
        em.detach(author);
        authorDao.delete(author);
        var authorAfterDelete = em.find(Author.class, BigInteger.valueOf(2));
        assertNull(authorAfterDelete);
    }

}
