package ru.otus.library.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.library.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
class BookRepositoryTest {

    private static final String BOOK_COLLECTION_NAME = "books";
    private static final String COMMENT_COLLECTION_NAME = "comments";

    private static final String NON_EXISTS_BOOK_TITLE = "Non exists title";
    private static final Comment EXISTS_COMMENT = new Comment("Tolkien - the genius")
            .setId("comment_id");
    private static final Comment NON_EXISTS_COMMENT = new Comment("Non exists comment");

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void prepare() {
        mongoTemplate.dropCollection(BOOK_COLLECTION_NAME);
        mongoTemplate.dropCollection(COMMENT_COLLECTION_NAME);
        mongoTemplate.createCollection(BOOK_COLLECTION_NAME);
        mongoTemplate.createCollection(COMMENT_COLLECTION_NAME);
        mongoTemplate.save(EXISTS_COMMENT);
        mongoTemplate.save(new Book("The lord of the rings",
                new Author("J.R.R. Tolkien"),
                new Genre("Fantasy"),
                List.of(EXISTS_COMMENT)));
    }

    @Test
    void findByTitleTestAndBookExists() {
        Book book = getExistsBook();
        Book bookFromRepository = bookRepository.findByTitle(book.getTitle())
                .orElseThrow(() -> new RuntimeException("Book does not exists"));
        assertEquals(book, bookFromRepository);
    }

    @Test
    void findByTitleTestAndBookDoesNotExist() {
        Optional<Book> bookOptional = bookRepository.findByTitle(NON_EXISTS_BOOK_TITLE);
        assertEquals(Optional.empty(), bookOptional);
    }

    @Test
    void findByCommentsContainingTestAndBookExists() {
        Book book = getExistsBook();
        Book bookFromRepository = bookRepository.findByCommentsContaining(EXISTS_COMMENT)
                .orElseThrow(() -> new RuntimeException("Book does not exists"));
        assertEquals(book, bookFromRepository);
    }

    @Test
    void findByCommentsContainingTestAndBookDoesNotExist() {
        Optional<Book> bookOptional = bookRepository.findByCommentsContaining(NON_EXISTS_COMMENT);
        assertEquals(Optional.empty(), bookOptional);
    }

    private Book getExistsBook() {
        return mongoTemplate.findAll(Book.class).get(0);
    }


}