package ru.otus.library.service;

import ru.otus.library.domain.Comment;

import java.util.List;

public interface CommentService {

    Comment create(String text, String bookId);

    void deleteById(String id);

    List<Comment> readAll();

}
