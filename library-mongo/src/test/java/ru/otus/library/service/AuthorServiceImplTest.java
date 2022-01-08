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
import ru.otus.library.domain.Author;
import ru.otus.library.service.impl.AuthorServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import(AuthorServiceImpl.class)
@DataMongoTest
class AuthorServiceImplTest {

    private static final String NON_EXISTS_AUTHOR_ID = "NON_EXISTS_BOOK_ID";
    private static final String AUTHOR_COLLECTION_NAME = "authors";
    private static final Author EXISTS_AUTHOR_TOLKIEN = new Author("J.R.R. Tolkien");
    private static final String NEW_AUTHOR_NAME_DUMAS = "A. Dumas";

    private static final String DOES_NOT_EXIST_CAPTOR = "does not exist";
    private static final String ALREADY_EXISTS_CAPTOR = "already exists";
    private static final String SUCCESS_CAPTOR = "successfully";

    @Autowired
    private AuthorService authorService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @MockBean
    private InputOutputComponent inputOutputComponent;

    @Captor
    private ArgumentCaptor<String> captor;

    @BeforeEach
    void prepare() {
        mongoTemplate.dropCollection(AUTHOR_COLLECTION_NAME);
        mongoTemplate.createCollection(AUTHOR_COLLECTION_NAME);
        mongoTemplate.save(EXISTS_AUTHOR_TOLKIEN);
    }

    @Test
    void createNotExistsAuthorTest() {
        Author author = authorService.create(NEW_AUTHOR_NAME_DUMAS);
        Author authorFromMongo = mongoTemplate.findById(author.getId(), Author.class);
        assertEquals(author, authorFromMongo);
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Test
    void createExistsAuthorTest() {
        Author author = authorService.create(getExistsAuthor().getName());
        Author authorFromMongo = mongoTemplate.findById(author.getId(), Author.class);
        assertEquals(author, authorFromMongo);
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(ALREADY_EXISTS_CAPTOR));
    }

    @Test
    void updateExistsAuthorByIdTest() {
        Author authorFromMongo = getExistsAuthor();
        Author authorAfterUpdate = authorService.updateById(authorFromMongo.getId(), NEW_AUTHOR_NAME_DUMAS);
        Author authorFromMongoAfterUpdate = mongoTemplate.findById(authorFromMongo.getId(), Author.class);
        assertEquals(authorAfterUpdate, authorFromMongoAfterUpdate);
        verify(inputOutputComponent, times(0)).write(captor.capture());
    }

    @Test
    void updateNotExistsAuthorByIdTest() {
        Author authorAfterUpdate = authorService.updateById(NON_EXISTS_AUTHOR_ID, NEW_AUTHOR_NAME_DUMAS);
        assertNull(authorAfterUpdate);
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Test
    void deleteNotExistsAuthorByIdTest() {
        authorService.deleteById(NON_EXISTS_AUTHOR_ID);
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Test
    void deleteExistsAuthorByIdTest() {
        Author authorFromMongo = getExistsAuthor();
        authorService.deleteById(authorFromMongo.getId());
        Author authorFromMongoAfterDelete = mongoTemplate.findById(authorFromMongo.getId(), Author.class);
        assertNull(authorFromMongoAfterDelete);
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    private Author getExistsAuthor() {
        return mongoTemplate.findAll(Author.class).get(0);
    }

}
