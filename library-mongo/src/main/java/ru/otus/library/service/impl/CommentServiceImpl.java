package ru.otus.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;
import ru.otus.library.service.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final InputOutputComponent component;

    @Override
    @Transactional
    public Comment create(String text, String bookId) {
        var existsBookOptional = bookRepository.findById(bookId);
        if (existsBookOptional.isPresent()) {
            Book existsBook = existsBookOptional.get();
            Comment comment = new Comment(text);
            existsBook.getComments().add(comment);
            commentRepository.save(comment);
            bookRepository.save(existsBook);
            component.write("Comment for book \"" + bookId + "\" successfully created.");
            return comment;
        } else {
            component.write("Book with ID \"" + bookId + "\" does not exists.");
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        var existsCommentOptional = commentRepository.findById(id);
        if (existsCommentOptional.isPresent()) {
            Comment existsComment = existsCommentOptional.get();
            var existsBookOptional = bookRepository.findByCommentsContaining(existsComment);
            if (existsBookOptional.isPresent()) {
                Book existsBook = existsBookOptional.get();
                existsBook.setComments(existsBook.getComments()
                        .stream()
                        .filter(x -> !x.getId().equals(id))
                        .collect(Collectors.toList()));
                bookRepository.save(existsBook);
                commentRepository.deleteById(id);
                component.write("Comment with ID \"" + id + "\" successfully deleted.");
            } else {
                component.write("Book with comment ID \"" + id + "\" does not exists.");
            }
        } else {
            component.write("Comment with ID \"" + id + "\" does not exist. Nothing to delete.");
        }
    }

    @Override
    public List<Comment> readAll() {
        return commentRepository.findAll();
    }

}
