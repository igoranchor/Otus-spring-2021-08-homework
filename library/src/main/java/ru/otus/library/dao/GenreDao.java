package ru.otus.library.dao;

import ru.otus.library.domain.Genre;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface GenreDao {

    Genre save(Genre domainEntity);

    List<Genre> findAll();

    Optional<Genre> findById(BigInteger id);

    Optional<Genre> findByName(String name);

    void delete(Genre domainEntity);

}
