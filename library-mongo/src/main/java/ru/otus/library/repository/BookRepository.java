package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;

import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    Optional<Book> findByTitle(String title);

    Optional<Book> findByCommentsContaining(Comment comment);

}
