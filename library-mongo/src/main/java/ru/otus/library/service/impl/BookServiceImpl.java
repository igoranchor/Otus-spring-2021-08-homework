package ru.otus.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.domain.*;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.service.BookService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final InputOutputComponent component;

    @Override
    public Book create(String title, String authorName, String genreName) {
        var existsBook = bookRepository.findByTitle(title);
        if (existsBook.isPresent()) {
            component.write("Book \"" + title + "\" already exists. Return exists book.");
            return existsBook.get();
        } else {
            var book = bookRepository.save(new Book(title, new Author(authorName), new Genre(genreName)));
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
    public Book updateById(String id, String newTitle, String authorName, String genreName) {
        var existsBook = readById(id);
        if (Objects.nonNull(existsBook)) {
            if (!existsBook.getTitle().equals(newTitle)) {
                existsBook.setTitle(newTitle);
            }
            if (!existsBook.getAuthor().getName().equals(authorName)) {
                existsBook.setAuthor(new Author(authorName));
            }
            if (!existsBook.getGenre().getName().equals(genreName)) {
                existsBook.setGenre(new Genre(genreName));
            }
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

    public Book readById(String id) {
        var existsBook = bookRepository.findById(id);
        if (existsBook.isEmpty()) {
            component.write("Book with ID \"" + id + "\" does not exist.");
            return null;
        }
        return existsBook.get();
    }

}
