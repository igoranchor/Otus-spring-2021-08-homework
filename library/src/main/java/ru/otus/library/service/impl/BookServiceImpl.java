package ru.otus.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.dao.BookDao;
import ru.otus.library.domain.Book;
import ru.otus.library.service.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao dao;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final InputOutputComponent component;

    @Override
    @Transactional
    public Book create(String title, String authorName, String genreName) {
        var existsBook = dao.findByTitle(title);
        if (existsBook.isPresent()) {
            component.write("Book \"" + title + "\" already exists. Return exists book.");
            return existsBook.get();
        } else {
            var author = authorService.create(authorName);
            var genre = genreService.create(genreName);
            var book = dao.save(new Book(title, genre, author));
            component.write("Book \"" + title + "\" successfully created.");
            return book;
        }
    }

    @Override
    public List<Book> readAll() {
        var books = dao.findAll();
        if (books.isEmpty()) {
            component.write("Books are not found");
        }
        return books;
    }

    @Override
    public Book readById(BigInteger id) {
        var existsBook = dao.findById(id);
        if (existsBook.isEmpty()) {
            component.write("Book with ID\"" + id + "\" does not exist.");
            return null;
        }
        return existsBook.get();
    }

    @Override
    public Book readByTitle(String title) {
        var existsBook = dao.findByTitle(title);
        if (existsBook.isEmpty()) {
            component.write("Book \"" + title + "\" does not exist.");
            return null;
        }
        return existsBook.get();
    }

    @Override
    @Transactional
    public Book updateById(BigInteger id, String newTitle, String authorName, String genreName) {
        var existsBook = readById(id);
        if (Objects.nonNull(existsBook)) {
            existsBook.setTitle(newTitle);
            existsBook.setAuthor(authorName == null ? existsBook.getAuthor() : authorService.create(authorName));
            existsBook.setGenre(genreName == null ? existsBook.getGenre() : genreService.create(genreName));
            dao.save(existsBook);
            component.write("Author with ID \"" + id + "\" successfully updated.");
            return existsBook;
        } else {
            component.write("Author with ID \"" + id + "\" does not exist. Nothing to update.");
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteById(BigInteger id) {
        var existsBook = readById(id);
        if (Objects.nonNull(existsBook)) {
            dao.delete(existsBook);
            component.write("Book with ID \"" + id + "\" successfully deleted.");
        } else {
            component.write("Book with ID \"" + id + "\" does not exist. Nothing to delete.");
        }
    }

}
