package ru.otus.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.otus.library.domain.Author;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.service.AuthorService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public List<Author> readAll() {
        var authors = authorRepository.findAll();
        if (authors.isEmpty()) {
            System.out.println("Authors are not found");
        }
        return authors;
    }

    @Override
    public Author readByName(String authorName) {
        var existsAuthor = authorRepository.findByName(authorName);
        if (existsAuthor.isEmpty()) {
            System.out.println("Author with title \"" + authorName + "\" does not exist.");
            return null;
        }
        return existsAuthor.get();
    }

    @Override
    public Author readById(String id) {
        var existsAuthor = authorRepository.findById(id);
        if (existsAuthor.isEmpty()) {
            System.out.println("Author with ID \"" + id + "\" does not exist.");
            return null;
        }
        return existsAuthor.get();
    }

    @Override
    public Author createOrUpdate(Author author) {
        author.setId(StringUtils.isBlank(author.getId()) ? UUID.randomUUID().toString() : author.getId());
        return authorRepository.save(author);
    }

    @Override
    public void delete(Author author) {
        authorRepository.delete(author);
    }

}
