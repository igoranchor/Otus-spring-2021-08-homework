package ru.otus.library.service;

import ru.otus.library.domain.*;

import java.math.BigInteger;
import java.util.List;

public interface CommentService {

    Comment create(String text, BigInteger bookId);

    List<Comment> readAll();

    List<Comment> readByBookId(BigInteger bookId);

    Comment updateById(BigInteger id, String newText);

    void deleteById(BigInteger id);

}
