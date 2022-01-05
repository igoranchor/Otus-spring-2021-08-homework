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
import ru.otus.library.service.impl.CommentServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import({CommentServiceImpl.class,
        BookServiceImpl.class})
@DataMongoTest
class CommentServiceImplTest {

    private static final String BOOK_COLLECTION_NAME = "books";
    private static final String COMMENT_COLLECTION_NAME = "comments";
    private static final String NON_EXISTS_ID = "NON_EXISTS_ID";
    private static final Comment EXISTS_COMMENT = new Comment("Tolkien - the genius")
            .setId("comment_id");
    private static final String NEW_COMMENT_TEXT = "NEW_COMMENT";

    private static final String DOES_NOT_EXIST_CAPTOR = "does not exist";
    private static final String IMPOSSIBLE_CAPTOR = "impossible";
    private static final String SUCCESS_CAPTOR = "successfully";

    @Autowired
    private CommentService commentService;

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
        mongoTemplate.save(EXISTS_COMMENT);
        mongoTemplate.save(new Book("The lord of the rings",
                new Author("J.R.R. Tolkien"),
                new Genre("Fantasy"),
                List.of(EXISTS_COMMENT)));
    }

    @Test
    void createNewCommentAndBookExists() {
        Book bookFromMongo = mongoTemplate.findAll(Book.class).get(0);
        Comment comment = commentService.create(NEW_COMMENT_TEXT, bookFromMongo.getId());
        assertNotNull(comment);
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Test
    void createNewCommentAndBookDoesNotExist() {
        var comment = commentService.create(NEW_COMMENT_TEXT, NON_EXISTS_ID);
        assertNull(comment);
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

    @Test
    void deleteExistsComment() {
        List<Comment> commentsFromMongo = mongoTemplate.findAll(Book.class).get(0).getComments();
        commentService.deleteById(commentsFromMongo.get(0).getId());
        List<Comment> commentsFromMongoAfterDelete = mongoTemplate.findAll(Book.class).get(0).getComments();
        assertEquals(commentsFromMongoAfterDelete.size() + 1, commentsFromMongo.size());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Test
    void deleteNotExistsComment() {
        commentService.deleteById(NON_EXISTS_ID);
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

}