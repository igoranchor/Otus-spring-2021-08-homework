package ru.otus.library.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Genre;
import ru.otus.library.service.GenreService;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreDao dao;
    private final InputOutputComponent component;

    @Override
    @Transactional
    public Genre create(String name) {
        var existsGenre = dao.findByName(name);
        if (existsGenre.isPresent()) {
            component.write("Genre \"" + name + "\" already exists. Return exists genre.");
            return existsGenre.get();
        } else {
            var genre = dao.save(new Genre(name));
            component.write("Genre \"" + name + "\" successfully created.");
            return genre;
        }
    }

    @Override
    public List<Genre> readAll() {
        var genres = dao.findAll();
        if (genres.isEmpty()) {
            component.write("Genres are not found");
        }
        return genres;
    }

    @Override
    public Genre readById(BigInteger id) {
        var existsGenre = dao.findById(id);
        if (existsGenre.isEmpty()) {
            component.write("Genre with ID \"" + id + "\" does not exist. Return null.");
            return null;
        }
        return existsGenre.get();
    }

    @Override
    public Genre readByName(String name) {
        var existsGenre = dao.findByName(name);
        if (existsGenre.isEmpty()) {
            component.write("Genre \"" + name + "\" does not exist. Return null.");
            return null;
        }
        return existsGenre.get();
    }

    @Override
    @Transactional
    public Genre updateById(BigInteger id, String newName) {
        var existsGenre = readById(id);
        if (Objects.nonNull(existsGenre)) {
            existsGenre.setName(newName);
            dao.save(existsGenre);
            component.write("Genre with ID \"" + id + "\" successfully updated.");
            return existsGenre;
        } else {
            component.write("Genre with ID \"" + id + "\" does not exist. Nothing to update.");
            return null;
        }
    }

    @Override
    @Transactional
    public void deleteById(BigInteger id) {
        var existsGenre = readById(id);
        if (Objects.nonNull(existsGenre)) {
            dao.delete(existsGenre);
            component.write("Genre with ID \"" + id + "\" successfully deleted.");
        } else {
            component.write("Genre with ID \"" + id + "\" does not exist. Nothing to delete.");
        }
    }

}
