package ru.otus.library.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Author;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface AuthorDao extends JpaRepository<Author, BigInteger> {

    Optional<Author> findByName(String name);

}
