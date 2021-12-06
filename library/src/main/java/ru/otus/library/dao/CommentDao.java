package ru.otus.library.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface CommentDao extends JpaRepository<Comment, BigInteger> {

    List<Comment> findByBook(Book book);

}
