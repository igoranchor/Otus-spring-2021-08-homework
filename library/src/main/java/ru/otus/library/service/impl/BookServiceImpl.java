package ru.otus.library.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.dao.BookDao;
import ru.otus.library.domain.Book;
import ru.otus.library.service.*;

import java.util.List;
import java.util.Objects;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao dao;
    private final AuthorService authorService;
    private final GenreService genreService;
    private final InputOutputComponent component;

    public BookServiceImpl(BookDao dao, AuthorService authorService, GenreService genreService, InputOutputComponent component) {
        this.dao = dao;
        this.authorService = authorService;
        this.genreService = genreService;
        this.component = component;
    }

    @Override
    public Book create(String title, String authorName, String genreName) {
        var existsBook = dao.getByTitle(title);
        if (Objects.nonNull(existsBook)) {
            component.write("Book \"" + title + "\" already exists. Return exists book.");
            return existsBook;
        } else {
            var author = authorService.create(authorName);
            var genre = genreService.create(genreName);
            var book = dao.insert(new Book(title, genre, author));
            component.write("Book \"" + title + "\" successfully created.");
            return book;
        }

    }

    @Override
    public List<Book> readAll() {
        var books = dao.getAll();
        if (books.isEmpty()) {
            component.write("Books are not found");
        }
        return books;
    }

    @Override
    public Book readById(long id) {
        var existsBook = dao.getById(id);
        if (Objects.isNull(existsBook)) {
            component.write("Book with ID\"" + id + "\" does not exist.");
        }
        return existsBook;
    }

    @Override
    public Book readByTitle(String title) {
        var existsBook = dao.getByTitle(title);
        if (Objects.isNull(existsBook)) {
            component.write("Book \"" + title + "\" does not exist.");
        }
        return existsBook;
    }

    @Override
    public Book updateById(long id, String newTitle, String authorName, String genreName) {
        var existsBook = readById(id);
        if (Objects.nonNull(existsBook)) {
            if (Objects.nonNull(newTitle)) {
                existsBook.setTitle(newTitle);
            }
            if (Objects.nonNull(authorName) && !authorName.equals(existsBook.getAuthor().getName())) {
                existsBook.setAuthor(authorService.create(authorName));
            }
            if (Objects.nonNull(genreName) && !genreName.equals(existsBook.getGenre().getName())) {
                existsBook.setGenre(genreService.create(genreName));
            }
            dao.update(existsBook);
            component.write("Book with ID \"" + id + "\" successfully updated.");
            return existsBook;
        } else {
            component.write("Book with ID \"" + id + "\" does not exist. Nothing to update.");
            return null;
        }
    }

    @Override
    public void deleteById(long id) {
        var existsBook = readById(id);
        if (Objects.nonNull(existsBook)) {
            dao.delete(existsBook);
            component.write("Book with ID \"" + id + "\" successfully deleted.");
        } else {
            component.write("Book with ID \"" + id + "\" does not exist. Nothing to delete.");
        }
    }

}
