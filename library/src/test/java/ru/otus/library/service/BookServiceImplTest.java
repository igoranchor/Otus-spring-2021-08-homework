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
import ru.otus.library.dao.BookDao;
import ru.otus.library.dao.impl.*;
import ru.otus.library.domain.*;
import ru.otus.library.service.impl.*;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import({AuthorDaoImpl.class, AuthorServiceImpl.class,
        GenreDaoImpl.class, GenreServiceImpl.class,
        BookDaoImpl.class, BookServiceImpl.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookServiceImplTest extends AbstractPostgreSQLContainerTest {

    private static final BigInteger EXISTS_BOOK_HOBBIT_ID = BigInteger.valueOf(2);
    private static final String EXISTS_BOOK_LORD_OF_THE_RINGS = "Властелин колец";
    private static final String NEW_BOOK_SILMARILLION = "Cильмариллион";
    private static final String NEW_BOOK_ELRIC = "Элрик из Мелнибонэ";
    private static final String EXISTS_GENRE = "фэнтези";
    private static final String NEW_AUTHOR = "Майкл Муркок";
    private static final String EXISTS_AUTHOR = "Джон Рональд Руэл Толкин";
    private static final String EXISTS_ANOTHER_AUTHOR = "Терри Пратчетт";

    private static final String EXISTS_CAPTOR = "already exists";
    private static final String DOES_NOT_EXIST_CAPTOR = "does not exist";
    private static final String SUCCESS_CAPTOR = "successfully";
    private static final String GENRE_CAPTOR = "genre";
    private static final String AUTHOR_CAPTOR = "author";

    @Autowired
    private BookService bookService;

    @Autowired
    private TestEntityManager em;

    @SpyBean
    private BookDao bookDao;

    @SpyBean
    private AuthorService authorService;

    @SpyBean
    private GenreService genreService;

    @MockBean
    private InputOutputComponent inputOutputComponent;

    @Captor
    private ArgumentCaptor<String> captor;

    @Sql("/sql/books.sql")
    @Test
    void createNotExistsBookAndAuthorExistsTest() {
        var book = bookService.create(NEW_BOOK_SILMARILLION, EXISTS_AUTHOR, EXISTS_GENRE);
        em.detach(book);
        var bookAfterInsert = em.find(Book.class, book.getId());
        assertNotNull(bookAfterInsert);
        verify(authorService, times(1)).create(any());
        verify(genreService, times(1)).create(any());
        verify(inputOutputComponent, times(3)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(AUTHOR_CAPTOR));
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(EXISTS_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(GENRE_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(EXISTS_CAPTOR));
        assertTrue(captor.getAllValues().get(2).toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/books.sql")
    @Test
    void createNotExistsBookAndAuthorDoesNotExistTest() {
        var book = bookService.create(NEW_BOOK_ELRIC, NEW_AUTHOR, EXISTS_GENRE);
        em.detach(book);
        var bookAfterInsert = em.find(Book.class, book.getId());
        assertNotNull(bookAfterInsert);
        verify(authorService, times(1)).create(any());
        verify(genreService, times(1)).create(any());
        verify(inputOutputComponent, times(3)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(AUTHOR_CAPTOR));
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(SUCCESS_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(GENRE_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(EXISTS_CAPTOR));
        assertTrue(captor.getAllValues().get(2).toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/books.sql")
    @Test
    void createExistsBookTest() {
        var book = bookService.create(EXISTS_BOOK_LORD_OF_THE_RINGS, EXISTS_AUTHOR, EXISTS_GENRE);
        em.detach(book);
        var bookAfterInsert = em.find(Book.class, book.getId());
        assertNotNull(bookAfterInsert);
        verify(authorService, times(0)).create(any());
        verify(genreService, times(0)).create(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(EXISTS_CAPTOR));
    }

    @Sql("/sql/books.sql")
    @Test
    void updateNotExistsBookByIdTest() {
        Book book = new Book(NEW_BOOK_SILMARILLION, new Genre(EXISTS_GENRE), new Author(EXISTS_AUTHOR));
        book.setId(BigInteger.valueOf(10));
        bookService.updateById(book.getId(), NEW_BOOK_ELRIC, NEW_AUTHOR, null);
        verify(bookDao, times(0)).save(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Sql("/sql/books.sql")
    @Test
    void updateExistsBookOnlyTitleByIdTest() {
        var book = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        em.detach(book);
        bookService.updateById(book.getId(), NEW_BOOK_SILMARILLION, null, null);
        em.detach(book);
        var bookAfterUpdate = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        assertEquals(NEW_BOOK_SILMARILLION, bookAfterUpdate.getTitle());
        assertEquals(EXISTS_AUTHOR, bookAfterUpdate.getAuthor().getName());
        verify(bookDao, times(1)).save(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/books.sql")
    @Test
    void updateExistsBookByIdAndNewAuthorExistsTest() {
        var book = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        em.detach(book);
        bookService.updateById(book.getId(), NEW_BOOK_SILMARILLION, EXISTS_ANOTHER_AUTHOR, null);
        em.detach(book);
        var bookAfterUpdate = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        assertEquals(NEW_BOOK_SILMARILLION, bookAfterUpdate.getTitle());
        assertEquals(EXISTS_ANOTHER_AUTHOR, bookAfterUpdate.getAuthor().getName());
        verify(bookDao, times(1)).save(any());
        verify(authorService, times(1)).create(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(AUTHOR_CAPTOR));
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(EXISTS_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/books.sql")
    @Test
    void updateExistsBookByIdAndNewAuthorDoesNotExistTest() {
        var book = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        em.detach(book);
        bookService.updateById(book.getId(), NEW_BOOK_ELRIC, NEW_AUTHOR, null);
        em.detach(book);
        var bookAfterUpdate = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);;
        assertEquals(NEW_BOOK_ELRIC, bookAfterUpdate.getTitle());
        assertEquals(NEW_AUTHOR, bookAfterUpdate.getAuthor().getName());
        verify(bookDao, times(1)).save(any());
        verify(authorService, times(1)).create(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(AUTHOR_CAPTOR));
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(SUCCESS_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/books.sql")
    @Test
    void deleteNotExistsBookByIdTest() {
        bookService.deleteById(BigInteger.valueOf(10));
        verify(bookDao, times(0)).delete(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Sql("/sql/books.sql")
    @Test
    void deleteExistsBookByIdTest() {
        var book = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        bookService.deleteById(book.getId());
        var bookAfterInsert = em.find(Book.class, EXISTS_BOOK_HOBBIT_ID);
        assertNull(bookAfterInsert);
        verify(authorService, times(0)).create(any());
        verify(genreService, times(0)).create(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

}
