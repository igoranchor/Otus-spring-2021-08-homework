package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.domain.Author;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuthorDaoImpl implements AuthorDao {

    private final EntityManager em;

    @Override
    public Author save(Author domainEntity) {
        if (domainEntity.getId() == null) {
            em.persist(domainEntity);
            return domainEntity;
        } else {
            return em.merge(domainEntity);
        }
    }

    @Override
    public List<Author> findAll() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Optional<Author> findById(BigInteger id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public Optional<Author> findByName(String name) {
        try {
            TypedQuery<Author> query = em.createQuery("select a " +
                            "from Author a " +
                            "where a.name = :name",
                    Author.class);
            query.setParameter("name", name);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Author domainEntity) {
        Query query = em.createQuery("delete " +
                "from Author a " +
                "where a.id = :id");
        query.setParameter("id", domainEntity.getId());
        query.executeUpdate();
    }

}
