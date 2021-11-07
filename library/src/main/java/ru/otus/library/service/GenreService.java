package ru.otus.library.service;

import ru.otus.library.domain.Genre;

import java.util.List;

public interface GenreService {

    Genre create(String name);

    List<Genre> readAll();

    Genre readById(long id);

    Genre readByName(String name);

    Genre updateById(long id, String newName);

    void deleteById(long id);

}
