package ru.otus.library.service;

import ru.otus.library.domain.Genre;

import java.math.BigInteger;
import java.util.List;

public interface GenreService {

    Genre create(String name);

    List<Genre> readAll();

    Genre readById(BigInteger id);

    Genre readByName(String name);

    Genre updateById(BigInteger id, String newName);

    void deleteById(BigInteger id);

}
