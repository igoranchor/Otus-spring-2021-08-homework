package ru.otus.library.service;


import ru.otus.library.domain.Author;

import java.util.List;

public interface AuthorService {

    List<Author> readAll();

    Author readByName(String authorName);

    void delete(Author author);

    Author readById(String id);

    Author createOrUpdate(Author author);

}
