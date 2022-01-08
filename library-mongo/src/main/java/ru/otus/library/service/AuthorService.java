package ru.otus.library.service;


import ru.otus.library.domain.Author;

import java.util.List;

public interface AuthorService {

    Author create(String authorName);

    List<Author> readAll();

    Author readByName(String authorName);

    Author updateById(String id, String authorName);

    void deleteById(String id);

    Author readById(String id);

}
