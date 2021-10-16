package ru.otus.library.dao;

import ru.otus.library.domain.Book;

import java.util.List;

public interface BookDao {

    Book insert(Book domainEntity);

    List<Book> getAll();

    Book getById(long id);

    Book getByTitle(String title);

    void update(Book domainEntity);

    void delete(Book domainEntity);

}
