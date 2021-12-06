package ru.otus.library.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.library.domain.*;

import java.math.BigInteger;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookDaoImplTest extends AbstractPostgreSQLContainerTest {

    private static final BigInteger EXISTS_BOOK_HOBBIT_ID = BigInteger.valueOf(2);
    private static final String EXISTS_BOOK_HOBBIT = "Хоббит";
    private static final String EXISTS_BOOK_LORD_OF_THE_RINGS = "Властелин колец";
    private static final String NEW_BOOK_SILMARILLION = "Cильмариллион";
    private static final String NEW_BOOK_ELRIC = "Элрик из Мелнибонэ";

    private static final BigInteger EXISTS_GENRE_ID = BigInteger.valueOf(2);
    private static final String EXISTS_GENRE = "фэнтези";

    private static final String NEW_AUTHOR = "Майкл Муркок";
    private static final BigInteger EXISTS_AUTHOR_ID = BigInteger.valueOf(2);
    private static final String EXISTS_AUTHOR = "Джон Рональд Руэл Толкин";
    private static final BigInteger EXISTS_ANOTHER_AUTHOR_ID = BigInteger.valueOf(3);
    private static final String EXISTS_ANOTHER_AUTHOR = "Терри Пратчетт";

    private static final int BOOKS_QUANTITY = 2;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private TestEntityManager em;

    @Sql("/sql/books.sql")
    @Test
    void insertNotExistsBookAndAuthorExistsAndGenreExistsTest() {
        var author = em.find(Author.class, EXISTS_AUTHOR_ID);
        var genre = em.find(Genre.class, EXISTS_GENRE_ID);
        var book = new Book(NEW_BOOK_SILMARILLION, genre, author);
        bookDao.save(book);
        var bookAfterInsert = em.find(Book.class, book.getId());
        assertNotNull(bookAfterInsert);
    }

    @Sql("/sql/books.sql")
    @Test
    void insertNotExistsBookAndAuthorDoesNotExistAndGenreExistsTest() {
        var author = new Author(NEW_AUTHOR);
        var genre = em.find(Genre.class, EXISTS_GENRE_ID);
        var book = new Book(NEW_BOOK_ELRIC, genre, author);
        bookDao.save(book);
        var bookAfterInsert = em.find(Book.class, book.getId());
        var authorAfterInsert = em.find(Author.class, book.getAuthor().getId());
        assertNotNull(bookAfterInsert);
        assertNotNull(authorAfterInsert);
    }

    @Sql("/sql/books.sql")
    @Test
    void insertExistsBookTest() {
        var author = em.find(Author.class, EXISTS_AUTHOR_ID);
        var genre = em.find(Genre.class, EXISTS_GENRE_ID);
        var book = new Book(EXISTS_BOOK_HOBBIT, genre, author);
        var exception = assertThrows(DataIntegrityViolationException.class, () -> bookDao.save(book));
        var pgSqlException = exception.getCause().getCause();
        assertTrue(Objects.nonNull(pgSqlException.getMessage()));
        assertTrue(pgSqlException.getMessage().contains("books_u1"));
    }

    @Sql("/sql/books.sql")
    @Test
    void getAllBooksTest() {
        var books = bookDao.findAll();
        assertEquals(BOOKS_QUANTITY, books.size());

        var bookTitles = books.stream().map(Book::getTitle).collect(Collectors.toList());
        var authors = books.stream().map(x -> x.getAuthor().getName()).distinct().collect(Collectors.toList());
        var genres = books.stream().map(x -> x.getGenre().getName()).distinct().collect(Collectors.toList());

        assertTrue(bookTitles.contains(EXISTS_BOOK_HOBBIT));
        assertTrue(bookTitles.contains(EXISTS_BOOK_LORD_OF_THE_RINGS));
        assertEquals(1, authors.size());
        assertEquals(1, genres.size());
        assertEquals(EXISTS_AUTHOR, authors.get(0));
        assertEquals(EXISTS_GENRE, genres.get(0));
    }

    @Sql("/sql/books.sql")
    @Test
    void getExistsBookByIdTest() {
        var book = bookDao.findById(EXISTS_BOOK_HOBBIT_ID)
                .orElseThrow(() -> new RuntimeException("Error"));
        assertEquals(EXISTS_BOOK_HOBBIT, book.getTitle());
        assertEquals(EXISTS_GENRE, book.getGenre().getName());
        assertEquals(EXISTS_AUTHOR, book.getAuthor().getName());
    }

    @Sql("/sql/books.sql")
    @Test
    void getNotExistsBookByIdTest() {
        assertTrue(bookDao.findById(BigInteger.valueOf(100)).isEmpty());
    }

    @Sql("/sql/books.sql")
    @Test
    void getExistsBookByNameTest() {
        var book = bookDao.findByTitle(EXISTS_BOOK_HOBBIT)
                .orElseThrow(() -> new RuntimeException("Error"));
        assertEquals(EXISTS_GENRE, book.getGenre().getName());
        assertEquals(EXISTS_AUTHOR, book.getAuthor().getName());
    }

    @Sql("/sql/books.sql")
    @Test
    void getNotExistsBookByTitleTest() {
        assertTrue(bookDao.findByTitle(NEW_BOOK_SILMARILLION).isEmpty());
    }

    @Sql("/sql/books.sql")
    @Test
    void updateBookOnlyTitle() {
        var book = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        book.setTitle(NEW_BOOK_SILMARILLION);
        bookDao.save(book);
        em.flush();
        em.detach(book);
        var bookAfterUpdate = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        assertEquals(NEW_BOOK_SILMARILLION, bookAfterUpdate.getTitle());
    }

    @Sql("/sql/books.sql")
    @Test
    void updateBookAndNewAuthorDoesNotExist() {
        var book = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        book.setTitle(NEW_BOOK_ELRIC);
        book.setAuthor(new Author(NEW_AUTHOR));
        bookDao.save(book);
        em.flush();
        em.detach(book);
        var bookAfterUpdate = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        var authorAfterUpdate = em.find(Author.class, bookAfterUpdate.getAuthor().getId());
        assertNotNull(bookAfterUpdate);
        assertNotNull(authorAfterUpdate);
        assertEquals(NEW_AUTHOR, authorAfterUpdate.getName());
    }

    @Sql("/sql/books.sql")
    @Test
    void updateBookAndNewAuthorExists() {
        var book = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        var author = em.find(Author.class, EXISTS_ANOTHER_AUTHOR_ID);
        book.setTitle(NEW_BOOK_ELRIC);
        book.setAuthor(author);
        bookDao.save(book);
        em.flush();
        em.detach(book);
        var bookAfterUpdate = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        assertEquals(NEW_BOOK_ELRIC, bookAfterUpdate.getTitle());
        assertEquals(EXISTS_ANOTHER_AUTHOR, bookAfterUpdate.getAuthor().getName());
    }

    @Sql("/sql/books.sql")
    @Test
    void deleteBook() {
        var book = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        em.detach(book);
        bookDao.delete(book);
        var bookAfterDelete = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        assertNull(bookAfterDelete);
    }

}