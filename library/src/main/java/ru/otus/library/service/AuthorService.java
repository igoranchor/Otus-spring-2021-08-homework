package ru.otus.library.service;

import ru.otus.library.domain.Author;

import java.util.List;

public interface AuthorService {

    Author create(String name);

    List<Author> readAll();

    Author readById(long id);

    Author readByName(String name);

    Author updateById(long id, String newName);

    void deleteById(long id);

}
