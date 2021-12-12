package ru.otus.library.dao;

import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.otus.library.dao.impl.CommentDaoImpl;
import ru.otus.library.domain.*;

import javax.persistence.PersistenceException;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@Import(CommentDaoImpl.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CommentDaoImplTest extends AbstractPostgreSQLContainerTest {

    private static final String NEW_COMMENT = "Ну такое себе.";
    private static final String EXISTS_COMMENT = "Отличная книга!";
    private static final String NOT_EXISTS_BOOK_SILMARILLION = "Cильмариллион";
    private static final int COMMENTS_QUANTITY = 3;
    private static final int COMMENTS_QUANTITY_OF_BOOK = 2;

    private static final String NO_DATA_FOUND_EXCEPTION = "Не найдена доменная сущность";

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private TestEntityManager em;

    @Sql("/sql/comments.sql")
    @Test
    void insertNotExistsCommentAndBookExistsTest() {
        Book book = em.find(Book.class, BigInteger.valueOf(2));
        Comment comment = new Comment(NEW_COMMENT, book);
        commentDao.save(comment);
        var commentAfterInsert = em.persistFlushFind(comment);
        assertNotNull(commentAfterInsert);
    }

    @Sql("/sql/comments.sql")
    @Test
    void insertNotExistsCommentAndBookDoesNotExitTest() {
        Author author = em.find(Author.class, BigInteger.valueOf(2));
        Genre genre = em.find(Genre.class, BigInteger.valueOf(2));
        var exception = assertThrows(
                PersistenceException.class, () -> commentDao.save(new Comment(NEW_COMMENT,
                        new Book(NOT_EXISTS_BOOK_SILMARILLION, genre, author))));
        var causeException = exception.getCause().getCause();
        assertEquals(PSQLException.class, causeException.getClass());
        assertNotNull(exception.getMessage());
        assertTrue(causeException.getMessage().contains("violates not-null constraint"));
    }

    @Sql("/sql/comments.sql")
    @Test
    void insertExistsCommentTest() {
        Book book = em.find(Book.class, BigInteger.valueOf(2));
        Comment comment = new Comment(EXISTS_COMMENT, book);
        commentDao.save(comment);
        var commentAfterInsert = em.persistFlushFind(comment);
        assertNotNull(commentAfterInsert);
    }

    @Sql("/sql/comments.sql")
    @Test
    void getAllCommentsTest() {
        var comments = commentDao.findAll();
        assertEquals(COMMENTS_QUANTITY, comments.size());
    }

    @Sql("/sql/comments.sql")
    @Test
    void getExistsCommentByIdTest() {
        var comment = commentDao.findById(BigInteger.valueOf(2))
                .orElseThrow(() -> new RuntimeException(NO_DATA_FOUND_EXCEPTION));
        assertEquals(EXISTS_COMMENT, comment.getText());
    }

    @Sql("/sql/comments.sql")
    @Test
    void getNotExistsCommentByIdTest() {
        var comment = commentDao.findById(BigInteger.valueOf(100));
        assertTrue(comment.isEmpty());
    }

    @Sql("/sql/comments.sql")
    @Test
    void updateComment() {
        var comment = em.find(Comment.class, BigInteger.valueOf(2));
        comment.setText(NEW_COMMENT);
        commentDao.save(comment);
        em.flush();
        em.detach(comment);
        var commentAfterUpdate = em.find(Comment.class, BigInteger.valueOf(2));
        assertEquals(NEW_COMMENT, commentAfterUpdate.getText());
    }

    @Sql("/sql/comments.sql")
    @Test
    void deleteComment() {
        var comment = em.find(Comment.class, BigInteger.valueOf(2));
        em.detach(comment);
        commentDao.delete(comment);
        var commentAfterDelete = em.find(Comment.class, BigInteger.valueOf(2));
        assertNull(commentAfterDelete);
    }

}
