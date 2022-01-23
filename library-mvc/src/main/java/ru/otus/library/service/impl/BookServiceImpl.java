package ru.otus.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.library.domain.*;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.service.BookService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    public List<Book> readAll() {
        var books = bookRepository.findAll();
        if (books.isEmpty()) {
            System.out.println("Books are not found");
        }
        return books;
    }

    @Override
    public Book readById(String id) {
        var existsBook = bookRepository.findById(id);
        if (existsBook.isEmpty()) {
            System.out.println("Book with ID \"" + id + "\" does not exist.");
            return null;
        }
        return existsBook.get();
    }

    @Override
    public List<Book> readByAuthor(Author author) {
        var books = bookRepository.findByAuthor(author);
        if (books.isEmpty()) {
            System.out.println("Books by author \"" + author.getName() + "\" are not found");
        }
        return books;
    }

    @Override
    public Book createOrUpdate(Book book) {
        Author author = authorRepository.findById(book.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("text"));
        book.setAuthor(author)
                .setId(StringUtils.isBlank(book.getId()) ? UUID.randomUUID().toString() : book.getId());
        return bookRepository.save(book);
    }

    @Override
    public Book addComment(Book book, Comment comment) {
        comment.setId(StringUtils.isBlank(comment.getId()) ? UUID.randomUUID().toString() : comment.getId());
        if (Objects.isNull(book.getComments())) {
            book.setComments(List.of(comment));
        } else {
            book.getComments().add(comment);
        }
        return bookRepository.save(book);
    }

    @Override
    public Book deleteComment(Book book, String commentId) {
        book.setComments(book.getComments()
                .stream()
                .filter(x -> !x.getId().equals(commentId))
                .collect(Collectors.toList()));
        return bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public void deleteByAuthorId(String authorId) {
        authorRepository.findById(authorId).ifPresent(
                author -> bookRepository.findByAuthor(author)
                        .forEach(this::delete));
    }

}
