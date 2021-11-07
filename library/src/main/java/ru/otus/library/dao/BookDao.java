package ru.otus.library.dao;

import ru.otus.library.domain.Book;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface BookDao {

    Book save(Book domainEntity);

    List<Book> findAll();

    Optional<Book> findById(BigInteger id);

    Optional<Book> findByTitle(String title);

    void delete(Book domainEntity);

}
