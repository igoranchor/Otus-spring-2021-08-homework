package ru.otus.library.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Genre;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class GenreDaoImpl implements GenreDao {

    private final NamedParameterJdbcOperations jdbc;

    public GenreDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Genre insert(Genre domainEntity) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", domainEntity.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update("insert into genres (name) values (:name)", params, keyHolder);
        domainEntity.setId(((BigDecimal) Objects.requireNonNull(keyHolder.getKeys()).get("id")).longValue());
        return domainEntity;
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select id, name from genres", new GenreDaoImpl.GenreMapper());
    }

    @Override
    public Genre getById(long id) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        try {
            return jdbc.queryForObject("select id, name from genres where id = :id",
                    params, new GenreDaoImpl.GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Genre getByName(String name) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("name", name);

        try {
            return jdbc.queryForObject("select id, name from genres where name = :name",
                    params, new GenreDaoImpl.GenreMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(Genre domainEntity) {
        final Map<String, Object> params = new HashMap<>(3);
        params.put("id", domainEntity.getId());
        params.put("name", domainEntity.getName());

        jdbc.update("update genres set name = :name where id = :id", params);
    }

    @Override
    public void delete(Genre domainEntity) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", domainEntity.getId());

        jdbc.update("delete from genres where id = :id", params);
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Genre(id, name);
        }

    }

}
