package ru.otus.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.domain.*;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.service.AuthorService;
import ru.otus.library.service.BookService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;
    private final InputOutputComponent component;

    @Override
    public Book create(String title, String authorName, String genreName) {
        var existsBook = bookRepository.findByTitle(title);
        if (existsBook.isPresent()) {
            component.write("Book \"" + title + "\" already exists. Return exists book.");
            return existsBook.get();
        } else {
            var author = authorService.readByName(authorName);
            if (Objects.isNull(author)) {
                author = authorService.create(authorName);
            }
            var book = bookRepository.save(new Book(title, author, new Genre(genreName)));
            component.write("Book \"" + title + "\" successfully created.");
            return book;
        }
    }

    @Override
    public List<Book> readAll() {
        var books = bookRepository.findAll();
        if (books.isEmpty()) {
            component.write("Books are not found");
        }
        return books;
    }

    @Override
    public Book readByTitle(String title) {
        var existsBook = bookRepository.findByTitle(title);
        if (existsBook.isEmpty()) {
            component.write("Book with title \"" + title + "\" does not exist.");
            return null;
        }
        return existsBook.get();
    }

    @Override
    public List<Book> readByAuthor(Author author) {
        var books = bookRepository.findByAuthor(author);
        if (books.isEmpty()) {
            component.write("Books by author \"" + author.getName() + "\" are not found");
        }
        return books;
    }

    @Override
    public Book updateById(String id, String newTitle, String authorName, String genreName) {
        var existsBook = readById(id);
        if (Objects.nonNull(existsBook)) {
            existsBook.setTitle(newTitle);
            existsBook.setAuthor(authorName == null ? existsBook.getAuthor() : authorService.create(authorName));
            existsBook.setGenre(genreName == null ? existsBook.getGenre() : new Genre(genreName));
            return bookRepository.save(existsBook);
        } else {
            return null;
        }
    }

    @Override
    public void deleteById(String id) {
        var existsBook = readById(id);
        if (Objects.nonNull(existsBook)) {
            bookRepository.delete(existsBook);
            component.write("Book with ID \"" + id + "\" successfully deleted.");
        } else {
            component.write("Book with ID \"" + id + "\" does not exist. Nothing to delete.");
        }
    }

    @Override
    public void deleteByAuthorId(String authorId) {
        var author = authorService.readById(authorId);
        var books = bookRepository.findByAuthor(author);
        for (Book book : books) {
            deleteById(book.getId());
        }
    }

    private Book readById(String id) {
        var existsBook = bookRepository.findById(id);
        if (existsBook.isEmpty()) {
            component.write("Book with ID \"" + id + "\" does not exist.");
            return null;
        }
        return existsBook.get();
    }

}
