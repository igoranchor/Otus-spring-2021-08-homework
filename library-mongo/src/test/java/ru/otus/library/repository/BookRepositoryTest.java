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
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataMongoTest
class BookRepositoryTest {

    private static final String BOOK_COLLECTION_NAME = "books";
    private static final String AUTHOR_COLLECTION_NAME = "authors";
    private static final String EXISTS_BOOK_TITLE = "The lord of the rings";
    private static final String NON_EXISTS_BOOK_TITLE = "NON_EXISTS_BOOK_TITLE";
    private static final Author EXISTS_AUTHOR_TOLKIEN = new Author("J.R.R. Tolkien");
    private static final Author NON_EXISTS_AUTHOR = new Author("NON_EXISTS_AUTHOR");

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void prepare() {
        mongoTemplate.dropCollection(BOOK_COLLECTION_NAME);
        mongoTemplate.dropCollection(AUTHOR_COLLECTION_NAME);
        mongoTemplate.createCollection(BOOK_COLLECTION_NAME);
        mongoTemplate.createCollection(AUTHOR_COLLECTION_NAME);
        mongoTemplate.save(EXISTS_AUTHOR_TOLKIEN);
        mongoTemplate.save(new Book(EXISTS_BOOK_TITLE,
                EXISTS_AUTHOR_TOLKIEN,
                new Genre("Fantasy"),
                List.of(new Comment("Tolkien - the genius"))));
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
    void findByAuthorTestAndBookExists() {
        Book book = getExistsBook();
        List<Book> booksFromRepository = bookRepository.findByAuthor(book.getAuthor());
        assertEquals(1, booksFromRepository.size());
        assertEquals(book, booksFromRepository.get(0));
    }

    @Test
    void findByAuthorTestAndBookDoesNotExist() {
        List<Book> booksFromRepository = bookRepository.findByAuthor(NON_EXISTS_AUTHOR);
        assertTrue(booksFromRepository.isEmpty());
    }

    private Book getExistsBook() {
        return mongoTemplate.findAll(Book.class).get(0);
    }

}