package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.BookDao;
import ru.otus.library.domain.Book;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Book save(Book domainEntity) {
        if (domainEntity.getId() == null) {
            em.persist(domainEntity);
            return domainEntity;
        } else {
            return em.merge(domainEntity);
        }
    }

    @Override
    public List<Book> findAll() {
        EntityGraph<?> entityGraph = em.getEntityGraph("author_genre_entity_graph");
        TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public Optional<Book> findById(BigInteger id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public Optional<Book> findByTitle(String title) {
        try {
            TypedQuery<Book> query = em.createQuery("select b " +
                            "from Book b " +
                            "where b.title = :title",
                    Book.class);
            query.setParameter("title", title);
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Book domainEntity) {
        Query query = em.createQuery("delete " +
                "from Book b " +
                "where b.id = :id");
        query.setParameter("id", domainEntity.getId());
        query.executeUpdate();
    }

}
