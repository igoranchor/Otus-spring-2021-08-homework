package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.CommentDao;
import ru.otus.library.domain.Comment;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Component
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
        TypedQuery<Comment> query = em.createQuery("select c from Comment c", Comment.class);
        return query.getResultList();
    }

    @Override
    public Optional<Comment> findById(BigInteger id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public void delete(Comment domainEntity) {
        em.remove(em.contains(domainEntity) ? domainEntity : em.merge(domainEntity));
    }

}
