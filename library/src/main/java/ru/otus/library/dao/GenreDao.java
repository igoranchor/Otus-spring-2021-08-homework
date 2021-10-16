package ru.otus.library.dao;

import ru.otus.library.domain.Genre;

import java.util.List;

public interface GenreDao {

    Genre insert(Genre domainEntity);

    List<Genre> getAll();

    Genre getById(long id);

    Genre getByName(String name);

    void update(Genre domainEntity);

    void delete(Genre domainEntity);

}
