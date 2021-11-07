package ru.otus.library.dao.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.domain.Author;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class AuthorDaoImpl implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    public AuthorDaoImpl(NamedParameterJdbcOperations jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Author insert(Author domainEntity) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", domainEntity.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update("insert into authors (name) values (:name)", params, keyHolder);
        domainEntity.setId(((BigDecimal) Objects.requireNonNull(keyHolder.getKeys()).get("id")).longValue());
        return domainEntity;
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select id, name from authors", new AuthorMapper());
    }

    @Override
    public Author getById(long id) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        try {
            return jdbc.queryForObject("select id, name from authors where id = :id",
                    params, new AuthorMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Author getByName(String name) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("name", name);

        try {
            return jdbc.queryForObject("select id, name from authors where name = :name",
                    params, new AuthorMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void update(Author domainEntity) {
        final Map<String, Object> params = new HashMap<>(3);
        params.put("id", domainEntity.getId());
        params.put("name", domainEntity.getName());

        jdbc.update("update authors set name = :name where id = :id", params);
    }

    @Override
    public void delete(Author domainEntity) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", domainEntity.getId());

        jdbc.update("delete from authors where id = :id", params);
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            return new Author(id, name);
        }

    }

}
