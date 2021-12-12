package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.domain.Genre;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final EntityManager em;

    @Override
    public Genre save(Genre domainEntity) {
        if (domainEntity.getId() == null) {
            em.persist(domainEntity);
            return domainEntity;
        } else {
            return em.merge(domainEntity);
        }
    }

    @Override
    public List<Genre> findAll() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Optional<Genre> findById(BigInteger id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public Optional<Genre> findByName(String name) {
        try {
            TypedQuery<Genre> query = em.createQuery("select g " +
                            "from Genre g " +
                            "where g.name = :name",
                    Genre.class);
            query.setParameter("name", name);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Genre domainEntity) {
        em.remove(em.contains(domainEntity) ? domainEntity : em.merge(domainEntity));
    }

}
