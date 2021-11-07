package ru.otus.library.dao;

import ru.otus.library.domain.Author;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface AuthorDao {

    Author save(Author domainEntity);

    List<Author> findAll();

    Optional<Author> findById(BigInteger id);

    Optional<Author> findByName(String name);

    void delete(Author domainEntity);

}
