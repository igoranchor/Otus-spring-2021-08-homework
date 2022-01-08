package ru.otus.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.domain.Author;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.service.AuthorService;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final InputOutputComponent component;

    @Override
    public Author create(String authorName) {
        var existsAuthor = authorRepository.findByName(authorName);
        if (existsAuthor.isPresent()) {
            component.write("Author \"" + authorName + "\" already exists. Return exists author.");
            return existsAuthor.get();
        } else {
            var author = authorRepository.save(new Author(authorName));
            component.write("Book \"" + author + "\" successfully created.");
            return author;
        }
    }

    @Override
    public List<Author> readAll() {
        var authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            component.write("Authors are not found");
        }
        return authors;
    }

    @Override
    public Author readByName(String authorName) {
        var existsAuthor = authorRepository.findByName(authorName);
        if (existsAuthor.isEmpty()) {
            component.write("Author with title \"" + authorName + "\" does not exist.");
            return null;
        }
        return existsAuthor.get();
    }

    @Override
    public Author updateById(String id, String authorName) {
        var existsAuthor = readById(id);
        if (Objects.nonNull(existsAuthor)) {
            if (!existsAuthor.getName().equals(authorName)) {
                existsAuthor.setName(authorName);
            }
            return authorRepository.save(existsAuthor);
        } else {
            return null;
        }
    }

    @Override
    public void deleteById(String id) {
        var existsAuthor = readById(id);
        if (Objects.nonNull(existsAuthor)) {
            authorRepository.delete(existsAuthor);
            component.write("Author with ID and his books \"" + id + "\" successfully deleted.");
        } else {
            component.write("Author with ID \"" + id + "\" does not exist. Nothing to delete.");
        }
    }

    @Override
    public Author readById(String id) {
        var existsAuthor = authorRepository.findById(id);
        if (existsAuthor.isEmpty()) {
            component.write("Author with ID \"" + id + "\" does not exist.");
            return null;
        }
        return existsAuthor.get();
    }

}
