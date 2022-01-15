package ru.otus.library.service;

import ru.otus.library.domain.*;

import java.util.List;

public interface BookService {

    List<Book> readAll();

    List<Book> readByAuthor(Author author);

    void delete(Book book);

    void deleteByAuthorId(String authorId);

    Book createOrUpdate(Book book);

    Book addComment(Book book, Comment comment);

    Book deleteComment(Book book, String commentId);

}
