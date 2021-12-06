package ru.otus.library.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Book;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface BookDao extends JpaRepository<Book, BigInteger> {

    Optional<Book> findByTitle(String title);

}
