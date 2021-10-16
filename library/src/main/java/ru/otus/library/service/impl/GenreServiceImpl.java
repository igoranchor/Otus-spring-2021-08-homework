package ru.otus.library.service.impl;

import org.springframework.stereotype.Service;
import ru.otus.library.component.InputOutputComponent;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Genre;
import ru.otus.library.service.GenreService;

import java.util.List;
import java.util.Objects;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDao dao;
    private final InputOutputComponent component;

    public GenreServiceImpl(GenreDao dao, InputOutputComponent component) {
        this.dao = dao;
        this.component = component;
    }

    @Override
    public Genre create(String name) {
        var existsGenre = dao.getByName(name);
        if (Objects.nonNull(existsGenre)) {
            component.write("Genre \"" + name + "\" already exists. Return exists genre.");
            return existsGenre;
        } else {
            var genre = dao.insert(new Genre(name));
            component.write("Genre \"" + name + "\" successfully created.");
            return genre;
        }
    }

    @Override
    public List<Genre> readAll() {
        var genres = dao.getAll();
        if (genres.isEmpty()) {
            component.write("Genres are not found");
        }
        return genres;
    }

    @Override
    public Genre readById(long id) {
        var existsGenre = dao.getById(id);
        if (Objects.isNull(existsGenre)) {
            component.write("Genre with ID \"" + id + "\" does not exist. Return null.");
        }
        return existsGenre;
    }

    @Override
    public Genre readByName(String name) {
        var existsGenre = dao.getByName(name);
        if (Objects.isNull(existsGenre)) {
            component.write("Genre \"" + name + "\" does not exist. Return null.");
        }
        return existsGenre;
    }

    @Override
    public Genre updateById(long id, String newName) {
        var existsGenre = readById(id);
        if (Objects.nonNull(existsGenre)) {
            existsGenre.setName(newName);
            dao.update(existsGenre);
            component.write("Genre with ID \"" + id + "\" successfully updated.");
            return existsGenre;
        } else {
            component.write("Genre with ID \"" + id + "\" does not exist. Nothing to update.");
            return null;
        }
    }

    @Override
    public void deleteById(long id) {
        var existsGenre = readById(id);
        if (Objects.nonNull(existsGenre)) {
            dao.delete(existsGenre);
            component.write("Genre with ID \"" + id + "\" successfully deleted.");
        } else {
            component.write("Genre with ID \"" + id + "\" does not exist. Nothing to delete.");
        }
    }

}
