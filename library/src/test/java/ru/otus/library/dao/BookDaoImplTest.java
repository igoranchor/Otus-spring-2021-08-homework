package ru.otus.library.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.library.dao.impl.*;
import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;

import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Import({BookDaoImpl.class,
        AuthorDaoImpl.class,
        GenreDaoImpl.class})
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookDaoImplTest extends AbstractPostgreSQLContainerTest {

    private static final String EXISTS_BOOK_HOBBIT = "Хоббит";
    private static final String EXISTS_BOOK_LORD_OF_THE_RINGS = "Властелин колец";
    private static final String NEW_BOOK_SILMARILLION = "Cильмариллион";
    private static final String NEW_BOOK_ELRIC = "Элрик из Мелнибонэ";
    private static final String EXISTS_GENRE = "фэнтези";
    private static final String NEW_AUTHOR = "Майкл Муркок";
    private static final String EXISTS_AUTHOR = "Джон Рональд Руэл Толкин";
    private static final String EXISTS_ANOTHER_AUTHOR = "Терри Пратчетт";
    private static final int BOOKS_QUANTITY = 2;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private AuthorDao authorDao;

    @Autowired
    private GenreDao genreDao;

    @Sql("/sql/books.sql")
    @Test
    void insertNotExistsBookAndAuthorExistsAndGenreExistsTest() {
        var author = authorDao.getByName(EXISTS_AUTHOR);
        var genre = genreDao.getByName(EXISTS_GENRE);
        var book = new Book(NEW_BOOK_SILMARILLION, genre, author);
        var id = bookDao.insert(book).getId();
        var bookAfterInsert = bookDao.getById(id);
        assertNotNull(bookAfterInsert);
    }

    @Sql("/sql/books.sql")
    @Test
    void insertNotExistsBookAndAuthorDoesNotExistAndGenreExistsTest() {
        var author = new Author(NEW_AUTHOR);
        var genre = genreDao.getByName(EXISTS_GENRE);
        var book = new Book(NEW_BOOK_ELRIC, genre, author);
        var exception = assertThrows(DataIntegrityViolationException.class,
                () -> bookDao.insert(book));
        assertTrue(Objects.nonNull(exception.getMessage()));
        assertTrue(exception.getMessage().contains("books_fk2"));
    }

    @Sql("/sql/books.sql")
    @Test
    void insertExistsBookTest() {
        var author = authorDao.getByName(EXISTS_AUTHOR);
        var genre = genreDao.getByName(EXISTS_GENRE);
        var book = new Book(EXISTS_BOOK_HOBBIT, genre, author);
        var exception = assertThrows(DuplicateKeyException.class, () ->
                bookDao.insert(book));
        assertTrue(Objects.nonNull(exception.getMessage()));
        assertTrue(exception.getMessage().contains("books_u1"));
    }

    @Sql("/sql/books.sql")
    @Test
    void getAllBooksTest() {
        var books = bookDao.getAll();
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
        var book = bookDao.getById(2);
        assertEquals(EXISTS_BOOK_HOBBIT, book.getTitle());
        assertEquals(EXISTS_GENRE, book.getGenre().getName());
        assertEquals(EXISTS_AUTHOR, book.getAuthor().getName());
    }

    @Sql("/sql/books.sql")
    @Test
    void getNotExistsBookByIdTest() {
        assertNull(bookDao.getById(100));
    }

    @Sql("/sql/books.sql")
    @Test
    void getExistsBookByNameTest() {
        var book = bookDao.getByTitle(EXISTS_BOOK_HOBBIT);
        assertNotNull(book);
        assertEquals(EXISTS_GENRE, book.getGenre().getName());
        assertEquals(EXISTS_AUTHOR, book.getAuthor().getName());
    }

    @Sql("/sql/books.sql")
    @Test
    void getNotExistsBookByNameTest() {
        assertNull(bookDao.getByTitle(NEW_BOOK_SILMARILLION));
    }

    @Sql("/sql/books.sql")
    @Test
    void updateBookOnlyTitle() {
        var book = bookDao.getById(2);
        book.setTitle(NEW_BOOK_SILMARILLION);
        bookDao.update(book);
        var bookAfterUpdate = bookDao.getById(2);
        assertEquals(NEW_BOOK_SILMARILLION, bookAfterUpdate.getTitle());
    }

    @Sql("/sql/books.sql")
    @Test
    void updateBookAndNewAuthorDoesNotExist() {
        var book = bookDao.getById(2);
        book.setTitle(NEW_BOOK_ELRIC);
        book.setAuthor(new Author(NEW_AUTHOR));
        var exception = assertThrows(DataIntegrityViolationException.class,
                () -> bookDao.update(book));
        assertTrue(Objects.nonNull(exception.getMessage()));
        assertTrue(exception.getMessage().contains("books_fk2"));
    }

    @Sql("/sql/books.sql")
    @Test
    void updateBookAndNewAuthorExists() {
        var book = bookDao.getById(2);
        var author = authorDao.getByName(EXISTS_ANOTHER_AUTHOR);
        book.setTitle(NEW_BOOK_ELRIC);
        book.setAuthor(author);
        bookDao.update(book);
        var bookAfterUpdate = bookDao.getById(2);
        assertEquals(NEW_BOOK_ELRIC, bookAfterUpdate.getTitle());
        assertEquals(EXISTS_ANOTHER_AUTHOR, bookAfterUpdate.getAuthor().getName());
    }

    @Sql("/sql/books.sql")
    @Test
    void deleteBook() {
        var book = bookDao.getById(2);
        bookDao.delete(book);
        var books = bookDao.getAll();
        assertEquals(BOOKS_QUANTITY - 1, books.size());
    }

}