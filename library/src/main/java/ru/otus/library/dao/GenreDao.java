package ru.otus.library.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Genre;

import java.math.BigInteger;
import java.util.Optional;

@Repository
public interface GenreDao extends JpaRepository<Genre, BigInteger> {

    Optional<Genre> findByName(String name);

}
