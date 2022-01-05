package ru.otus.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.domain.*;
import ru.otus.library.service.impl.BookServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import(BookServiceImpl.class)
@DataMongoTest
class BookServiceImplTest {

    private static final String BOOK_COLLECTION_NAME = "books";
    private static final String COMMENT_COLLECTION_NAME = "comments";

    private static final String NON_EXISTS_BOOK_ID = "NON_EXISTS_BOOK_ID";
    private static final String EXISTS_BOOK_TITLE = "The lord of the rings";
    private static final String EXISTS_BOOK_AUTHOR = "J.R.R. Tolkien";
    private static final String NEW_BOOK_TITLE_ELRIC = "Elric of Melnibone";
    private static final String NEW_BOOK_AUTHOR_ELRIC = "Michael John Moorcock";
    private static final String BOOK_GENRE = "Fantasy";

    private static final String DOES_NOT_EXIST_CAPTOR = "does not exist";
    private static final String SUCCESS_CAPTOR = "successfully";

    @Autowired
    private BookService bookService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @MockBean
    private InputOutputComponent inputOutputComponent;

    @Captor
    private ArgumentCaptor<String> captor;

    @BeforeEach
    void prepare() {
        mongoTemplate.dropCollection(BOOK_COLLECTION_NAME);
        mongoTemplate.dropCollection(COMMENT_COLLECTION_NAME);
        mongoTemplate.createCollection(BOOK_COLLECTION_NAME);
        mongoTemplate.createCollection(COMMENT_COLLECTION_NAME);
        mongoTemplate.save(new Book(EXISTS_BOOK_TITLE,
                new Author(EXISTS_BOOK_AUTHOR),
                new Genre(BOOK_GENRE)));
    }

    @Test
    void createNotExistsBookTest() {
        Book book = bookService.create(NEW_BOOK_TITLE_ELRIC, NEW_BOOK_AUTHOR_ELRIC, BOOK_GENRE);
        Book bookFromMongo = mongoTemplate.findById(book.getId(), Book.class);
        assertNotNull(bookFromMongo);
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Test
    void createExistsBookTest() {
        Book book = bookService.create(EXISTS_BOOK_TITLE, EXISTS_BOOK_AUTHOR, BOOK_GENRE);
        Book bookFromMongo = mongoTemplate.findById(book.getId(), Book.class);
        assertNotNull(bookFromMongo);
        verify(inputOutputComponent, times(1)).write(captor.capture());
    }

    @Test
    void updateExistsBookByIdTest() {
        Book bookFromMongo = mongoTemplate.findAll(Book.class).get(0);
        Book bookAfterUpdate = bookService.updateById(bookFromMongo.getId(), NEW_BOOK_TITLE_ELRIC, NEW_BOOK_AUTHOR_ELRIC, null);
        Book bookFromMongoAfterUpdate = mongoTemplate.findById(bookFromMongo.getId(), Book.class);
        assertEquals(bookAfterUpdate, bookFromMongoAfterUpdate);
        verify(inputOutputComponent, times(0)).write(captor.capture());
    }

    @Test
    void updateNotExistsBookByIdTest() {
        Book bookAfterUpdate = bookService.updateById(NON_EXISTS_BOOK_ID, NEW_BOOK_TITLE_ELRIC, null, null);
        assertNull(bookAfterUpdate);
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Test
    void deleteNotExistsBookByIdTest() {
        bookService.deleteById(NON_EXISTS_BOOK_ID);
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Test
    void deleteExistsBookByIdTest() {
        Book bookFromMongo = mongoTemplate.findAll(Book.class).get(0);
        bookService.deleteById(bookFromMongo.getId());
        Book bookFromMongoAfterDelete = mongoTemplate.findById(bookFromMongo.getId(), Book.class);
        assertNull(bookFromMongoAfterDelete);
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

}
