package ru.otus.library.service;

import ru.otus.library.domain.Author;
import ru.otus.library.domain.Book;

import java.util.List;

public interface BookService {

    Book create(String title, String authorName, String genreName);

    List<Book> readAll();

    Book readByTitle(String title);

    List<Book> readByAuthor(Author author);

    Book updateById(String id, String newTitle, String authorName, String genreName);

    void deleteById(String id);

    void deleteByAuthorId(String authorId);

}
