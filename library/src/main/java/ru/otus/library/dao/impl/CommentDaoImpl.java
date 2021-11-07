package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.CommentDao;
import ru.otus.library.domain.Book;
import ru.otus.library.domain.Comment;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentDaoImpl implements CommentDao {

    private final EntityManager em;

    @Override
    public Comment save(Comment domainEntity) {
        if (domainEntity.getId() == null) {
            em.persist(domainEntity);
            return domainEntity;
        } else {
            return em.merge(domainEntity);
        }
    }

    @Override
    public List<Comment> findAll() {
//        EntityGraph<?> entityGraph = em.getEntityGraph("book_entity_graph");
        TypedQuery<Comment> query = em.createQuery("select c from Comment c", Comment.class);
//        query.setHint("javax.persistence.fetchgraph", entityGraph);
        return query.getResultList();
    }

    @Override
    public Optional<Comment> findById(BigInteger id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public List<Comment> findByBook(Book book) {
        TypedQuery<Comment> query = em.createQuery("select c "
                        + "from Comment c "
                        + "where c.book = :book",
                Comment.class);
        query.setParameter("book", book);
        return query.getResultList();
    }

    @Override
    public void delete(Comment domainEntity) {
        Query query = em.createQuery("delete " +
                "from Comment c " +
                "where c.id = :id");
        query.setParameter("id", domainEntity.getId());
        query.executeUpdate();
    }

}
