package ru.otus.library.dao;

import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface CommentDao {

    Comment save(Comment domainEntity);

    List<Comment> findAll();

    Optional<Comment> findById(BigInteger id);

    void delete(Comment domainEntity);

}
