package ru.otus.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.dao.CommentDao;
import ru.otus.library.domain.Comment;
import ru.otus.library.service.BookService;
import ru.otus.library.service.CommentService;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentDao dao;
    private final BookService bookService;
    private final InputOutputComponent component;

    @Override
    @Transactional
    public Comment create(String text, BigInteger bookId) {
        var book = bookService.readById(bookId);
        if (Objects.isNull(book)) {
            component.write("it is impossible to add a comment for a non-existent book.");
            return null;
        }
        var comment = dao.save(new Comment(text, book));
        component.write("Comment \"" + comment.getText() + "\" on book \"" + comment.getBook().getTitle()
                + "\" successfully created.");
        return comment;
    }

    @Override
    public List<Comment> readAll() {
        var comments = dao.findAll();
        if (comments.isEmpty()) {
            component.write("Comments are not found");
        }
        return comments;
    }

    @Override
    public List<Comment> readByBookId(BigInteger bookId) {
        var book = bookService.readById(bookId);
        if (Objects.isNull(book)) {
            component.write("it is impossible to add a comment for a non-existent book.");
            return null;
        }
        var existsComment = dao.findByBook(book);
        if (existsComment.isEmpty()) {
            component.write("Comments on book\"" + book.getTitle() + "\" does not exist.");
        }
        return existsComment;
    }

    @Override
    @Transactional
    public Comment updateById(BigInteger id, String newText) {
        var existsComment = readById(id);
        if (Objects.nonNull(existsComment)) {
            existsComment.setText(newText);
            dao.save(existsComment);
            component.write("Comment with ID \"" + id + "\" successfully updated.");
            return existsComment;
        } else {
            component.write("Comment with ID \"" + id + "\" does not exist. Nothing to update.");
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteById(BigInteger id) {
        var existsComment = readById(id);
        if (Objects.nonNull(existsComment)) {
            dao.delete(existsComment);
            component.write("Comment with ID \"" + id + "\" successfully deleted.");
        } else {
            component.write("Comment with ID \"" + id + "\" does not exist. Nothing to delete.");
        }
    }

    private Comment readById(BigInteger id) {
        var existsComment = dao.findById(id);
        if (existsComment.isEmpty()) {
            component.write("Comment with ID\"" + id + "\" does not exist.");
            return null;
        }
        return existsComment.get();
    }

}
