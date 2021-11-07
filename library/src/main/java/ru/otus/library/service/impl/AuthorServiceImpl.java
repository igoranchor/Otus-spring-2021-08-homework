package ru.otus.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.domain.Author;
import ru.otus.library.service.AuthorService;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao dao;
    private final InputOutputComponent component;

    @Override
    @Transactional
    public Author create(String name) {
        var existsAuthor = dao.findByName(name);
        if (existsAuthor.isPresent()) {
            component.write("Author \"" + name + "\" already exists. Return exists author.");
            return existsAuthor.get();
        } else {
            var author = dao.save(new Author(name));
            component.write("Author \"" + name + "\" successfully created.");
            return author;
        }
    }

    @Override
    public List<Author> readAll() {
        var authors = dao.findAll();
        if (authors.isEmpty()) {
            component.write("Authors are not found");
        }
        return authors;
    }

    @Override
    public Author readById(BigInteger id) {
        var existsAuthor = dao.findById(id);
        if (existsAuthor.isEmpty()) {
            component.write("Author with ID \"" + id + "\" does not exist. Return null.");
            return null;
        }
        return existsAuthor.get();
    }

    @Override
    public Author readByName(String name) {
        var existsAuthor = dao.findByName(name);
        if (existsAuthor.isEmpty()) {
            component.write("Author \"" + name + "\" does not exist. Return null.");
            return null;
        }
        return existsAuthor.get();
    }

    @Override
    @Transactional
    public Author updateById(BigInteger id, String newName) {
        var existsAuthor = readById(id);
        if (Objects.nonNull(existsAuthor)) {
            existsAuthor.setName(newName);
            dao.save(existsAuthor);
            component.write("Author with ID \"" + id + "\" successfully updated.");
            return existsAuthor;
        } else {
            component.write("Author with ID \"" + id + "\" does not exist. Nothing to update.");
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteById(BigInteger id) {
        var existsAuthor = readById(id);
        if (Objects.nonNull(existsAuthor)) {
            dao.delete(existsAuthor);
            component.write("Author with ID \"" + id + "\" successfully deleted.");
        } else {
            component.write("Author with ID \"" + id + "\" does not exist. Nothing to delete.");
        }
    }

}
