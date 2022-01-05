package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.domain.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
}
