package ru.otus.library.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.library.domain.Author;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataMongoTest
class AuthorRepositoryTest {

    private static final String AUTHOR_COLLECTION_NAME = "authors";
    private static final Author EXISTS_AUTHOR_TOLKIEN = new Author("J.R.R. Tolkien");
    private static final Author NON_EXISTS_AUTHOR = new Author("NON_EXISTS_AUTHOR");

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void prepare() {
        mongoTemplate.dropCollection(AUTHOR_COLLECTION_NAME);
        mongoTemplate.createCollection(AUTHOR_COLLECTION_NAME);
        mongoTemplate.save(EXISTS_AUTHOR_TOLKIEN);
    }

    @Test
    void findByNameTestAndAuthorExists() {
        Author author = getExistsAuthor();
        Author authorFromRepository = authorRepository.findByName(author.getName())
                .orElseThrow(() -> new RuntimeException("Author does not exists"));
        assertEquals(author, authorFromRepository);
    }

    @Test
    void findByNameTestAndAuthorDoesNotExist() {
        Optional<Author> authorOptional = authorRepository.findByName(NON_EXISTS_AUTHOR.getName());
        assertEquals(Optional.empty(), authorOptional);
    }

    private Author getExistsAuthor() {
        return mongoTemplate.findAll(Author.class).get(0);
    }

}