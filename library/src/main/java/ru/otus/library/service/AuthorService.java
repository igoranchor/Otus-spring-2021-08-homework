package ru.otus.library.service;

import ru.otus.library.domain.Author;

import java.math.BigInteger;
import java.util.List;

public interface AuthorService {

    Author create(String name);

    List<Author> readAll();

    Author readById(BigInteger id);

    Author readByName(String name);

    Author updateById(BigInteger id, String newName);

    void deleteById(BigInteger id);

}
