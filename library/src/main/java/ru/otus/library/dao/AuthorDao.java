package ru.otus.library.dao;

import ru.otus.library.domain.Author;

import java.util.List;

public interface AuthorDao {

    Author insert(Author domainEntity);

    List<Author> getAll();

    Author getById(long id);

    Author getByName(String name);

    void update(Author domainEntity);

    void delete(Author domainEntity);

}
