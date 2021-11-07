package ru.otus.library.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.BookDao;
import ru.otus.library.domain.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations jdbc;

    public BookDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Book insert(Book domainEntity) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", domainEntity.getTitle());
        params.addValue("author_id", domainEntity.getAuthor().getId());
        params.addValue("genre_id", domainEntity.getGenre().getId());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update("insert into books (title, author_id, genre_id) values (:title, :author_id, :genre_id)", params, keyHolder);
        domainEntity.setId(((BigDecimal) Objects.requireNonNull(keyHolder.getKeys()).get("id")).longValue());
        return domainEntity;
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select b.id, b.title, "
                + "       b.author_id, a.name as author_name, "
                + "       b.genre_id, g.name as genre_name"
                + "  from books b "
                + "  left join authors a "
                + "    on a.id = b.author_id "
                + "  left join genres g "
                + "    on g.id = b.genre_id", new BookDaoImpl.BookMapper());
    }

    @Override
    public Book getById(long id) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        try {
            return jdbc.queryForObject("select b.id, b.title, "
                    + "       b.author_id, a.name as author_name, "
                    + "       b.genre_id, g.name as genre_name"
                    + "  from books b "
                    + "  join authors a "
                    + "    on a.id = b.author_id "
                    + "  join genres g "
                    + "    on g.id = b.genre_id "
                    + " where b.id = :id", params, new BookDaoImpl.BookMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Book getByTitle(String title) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("title", title);

        try {
            return jdbc.queryForObject("select b.id, b.title, "
                    + "       b.author_id, a.name as author_name, "
                    + "       b.genre_id, g.name as genre_name"
                    + "  from books b "
                    + "  join authors a "
                    + "    on a.id = b.author_id "
                    + "  join genres g "
                    + "    on g.id = b.genre_id "
                    + " where b.title = :title", params, new BookDaoImpl.BookMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(Book domainEntity) {
        final Map<String, Object> params = new HashMap<>(3);
        params.put("id", domainEntity.getId());
        params.put("title", domainEntity.getTitle());
        params.put("author_id", domainEntity.getAuthor().getId());
        params.put("genre_id", domainEntity.getGenre().getId());

        jdbc.update("update books set title = :title, author_id = :author_id, genre_id = :genre_id where id = :id",
                params);
    }

    @Override
    public void delete(Book domainEntity) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", domainEntity.getId());

        jdbc.update("delete from books where id = :id", params);
    }

//    private void persistAuthor(Book domainEntity) {
//        var author = domainEntity.getAuthor();
//        if (author.getId() == 0) {
//            domainEntity.getAuthor().setId(authorDao.insert(author));
//        } else {
//            var existsAuthor = authorDao.getById(author.getId());
//            if (!existsAuthor.getName().equals(author.getName())) {
//                existsAuthor.setName(author.getName());
//                authorDao.update(existsAuthor);
//                domainEntity.setAuthor(existsAuthor);
//            }
//        }
//    }
//
//    private void persistGenre(Book domainEntity) {
//        var genre = domainEntity.getGenre();
//        if (genre.getId() == 0) {
//            domainEntity.getGenre().setId(genreDao.insert(genre));
//        } else {
//            var existsGenre = genreDao.getById(genre.getId());
//            if (!existsGenre.getName().equals(genre.getName())) {
//                existsGenre.setName(genre.getName());
//                genreDao.update(existsGenre);
//                domainEntity.setGenre(existsGenre);
//            }
//        }
//    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String title = resultSet.getString("title");
            Genre genre = new Genre(resultSet.getLong("genre_id"), resultSet.getString("genre_name"));
            Author author = new Author(resultSet.getLong("author_id"), resultSet.getString("author_name"));
            return new Book(id, title, genre, author);
        }
    }

}
