package ru.otus.library.service;

import ru.otus.library.domain.Book;

import java.math.BigInteger;
import java.util.List;

public interface BookService {

    Book create(String title, String authorName, String genreName);

    List<Book> readAll();

    Book readById(BigInteger id);

    Book readByTitle(String title);

    Book updateById(BigInteger id, String newTitle, String authorName, String genreName);

    void deleteById(BigInteger id);

}
