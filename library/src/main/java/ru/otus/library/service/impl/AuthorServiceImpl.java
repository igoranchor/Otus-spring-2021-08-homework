package ru.otus.library.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.domain.Author;
import ru.otus.library.service.AuthorService;

import java.util.List;
import java.util.Objects;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao dao;
    private final InputOutputComponent component;

    public AuthorServiceImpl(AuthorDao dao, InputOutputComponent component) {
        this.dao = dao;
        this.component = component;
    }

    @Override
    public Author create(String name) {
        var existsAuthor = dao.getByName(name);
        if (Objects.nonNull(existsAuthor)) {
            component.write("Author \"" + name + "\" already exists. Return exists author.");
            return existsAuthor;
        } else {
            var author = dao.insert(new Author(name));
            component.write("Author \"" + name + "\" successfully created.");
            return author;
        }
    }

    @Override
    public List<Author> readAll() {
        var authors = dao.getAll();
        if (authors.isEmpty()) {
            component.write("Authors are not found");
        }
        return authors;
    }

    @Override
    public Author readById(long id) {
        var existsAuthor = dao.getById(id);
        if (Objects.isNull(existsAuthor)) {
            component.write("Author with ID \"" + id + "\" does not exist. Return null.");
        }
        return existsAuthor;
    }

    @Override
    public Author readByName(String name) {
        var existsAuthor = dao.getByName(name);
        if (Objects.isNull(existsAuthor)) {
            component.write("Author \"" + name + "\" does not exist. Return null.");
        }
        return existsAuthor;
    }

    @Override
    public Author updateById(long id, String newName) {
        var existsAuthor = readById(id);
        if (Objects.nonNull(existsAuthor)) {
            existsAuthor.setName(newName);
            dao.update(existsAuthor);
            component.write("Author with ID \"" + id + "\" successfully updated.");
            return existsAuthor;
        } else {
            component.write("Author with ID \"" + id + "\" does not exist. Nothing to update.");
            return null;
        }
    }

    @Override
    public void deleteById(long id) {
        var existsAuthor = readById(id);
        if (Objects.nonNull(existsAuthor)) {
            dao.delete(existsAuthor);
            component.write("Author with ID \"" + id + "\" successfully deleted.");
        } else {
            component.write("Author with ID \"" + id + "\" does not exist. Nothing to delete.");
        }
    }

}
