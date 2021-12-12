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
import ru.otus.library.dao.CommentDao;
import ru.otus.library.dao.impl.*;
import ru.otus.library.domain.Comment;
import ru.otus.library.service.impl.*;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Import({CommentDaoImpl.class, CommentServiceImpl.class,
        AuthorDaoImpl.class, AuthorServiceImpl.class,
        GenreDaoImpl.class, GenreServiceImpl.class,
        BookDaoImpl.class, BookServiceImpl.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentServiceImplTest extends AbstractPostgreSQLContainerTest {

    private static final BigInteger EXISTS_COMMENT_ID = BigInteger.valueOf(2);
    private static final BigInteger EXISTS_BOOK_ID = BigInteger.valueOf(2);

    private static final String COMMENT = "Comment";

    private static final String DOES_NOT_EXIST_CAPTOR = "does not exist";
    private static final String IMPOSSIBLE_CAPTOR = "impossible";
    private static final String SUCCESS_CAPTOR = "successfully";

    @Autowired
    private CommentService commentService;

    @Autowired
    private TestEntityManager em;

    @SpyBean
    private CommentDao commentDao;

    @MockBean
    private InputOutputComponent inputOutputComponent;

    @Captor
    private ArgumentCaptor<String> captor;

    @Sql("/sql/comments.sql")
    @Test
    void createNewCommentAndBookExists() {
        var comment = commentService.create(COMMENT, EXISTS_BOOK_ID);
        em.detach(comment);
        var commentAfterInsert = em.find(Comment.class, comment.getId());
        assertNotNull(commentAfterInsert);
        verify(commentDao, times(1)).save(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/comments.sql")
    @Test
    void createNewCommentAndBookDoesNotExist() {
        var comment = commentService.create(COMMENT, BigInteger.valueOf(10));
        assertNull(comment);
        verify(commentDao, times(0)).save(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(IMPOSSIBLE_CAPTOR));
    }

    @Sql("/sql/comments.sql")
    @Test
    void updateCommentOnlyText() {
        var comment = em.find(Comment.class, EXISTS_COMMENT_ID);
        em.detach(comment);
        commentService.updateById(comment.getId(), COMMENT);
        em.detach(comment);
        var commentAfterUpdate = em.find(Comment.class, EXISTS_COMMENT_ID);
        assertEquals(COMMENT, commentAfterUpdate.getText());
        verify(commentDao, times(1)).save(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/comments.sql")
    @Test
    void deleteExistsComment() {
        var existsComment = em.find(Comment.class, EXISTS_COMMENT_ID);
        commentService.deleteById(existsComment.getId());
        var commentAfterDelete = em.find(Comment.class, EXISTS_COMMENT_ID);
        assertNull(commentAfterDelete);
        verify(commentDao, times(1)).delete(any());
        verify(inputOutputComponent, times(1)).write(captor.capture());
        assertTrue(captor.getValue().toLowerCase().contains(SUCCESS_CAPTOR));
    }

    @Sql("/sql/comments.sql")
    @Test
    void deleteNotExistsComment() {
        commentService.deleteById(BigInteger.valueOf(10));
        verify(commentDao, times(0)).delete(any());
        verify(inputOutputComponent, times(2)).write(captor.capture());
        assertTrue(captor.getAllValues().get(0).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
        assertTrue(captor.getAllValues().get(1).toLowerCase().contains(DOES_NOT_EXIST_CAPTOR));
    }

}