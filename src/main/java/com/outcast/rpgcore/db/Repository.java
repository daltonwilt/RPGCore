package com.outcast.rpgcore.db;

import com.outcast.rpgcore.RPGCore;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Repository<T extends Identifiable<ID>, ID extends Serializable> implements IRepository<T, ID> {

    protected SessionFactory sessionFactory;

    protected Class<T> persistable;

    public Repository(Class<T> persistable) {
        this.persistable = persistable;
        EntityManagerFactory entityManagerFactory = RPGCore.getEntityManagerFactory();

        if(entityManagerFactory instanceof SessionFactory) {
            this.sessionFactory = (SessionFactory) entityManagerFactory;
        } else {
            throw new IllegalStateException("JPA implementation is not Hibernate ( EMF is not instance of SessionFactory ).");
        }
    }

    private void merge(T entity, Session session) {
        session.merge(entity);
    }

    private void remove(T entity, Session session) {
        session.remove(entity);
    }

    protected void transactionOf(Consumer<Session> sessionConsumer) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();
                sessionConsumer.accept(session);
                session.flush();
                transaction.commit();
            } catch(Exception e) {
                if(transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    protected CompletableFuture<Void> asyncTransactionOf(Consumer<Session> sessionConsumer) {
        return CompletableFuture.runAsync(() -> transactionOf(sessionConsumer));
    }

    @Override
    public Optional<T> findById(ID id) {
        T result;
        Session session = sessionFactory.openSession();
        result = session.find(persistable, id);
        session.close();
        return Optional.ofNullable(result);
    }

    @Override
    public void mergeOne(T entity) {
        transactionOf(session -> merge(entity, session));
    }

    @Override
    public void mergeAll(Collection<T> entities) {
        transactionOf(session -> entities.forEach(entity -> merge(entity, session)));
    }

    @Override
    public void removeOne(T entity) {
        transactionOf(session -> remove(entity, session));
    }

    @Override
    public void removeAll(Collection<T> entities) {
        transactionOf(session -> entities.forEach(entity -> remove(entity, session)));
    }

    @Override
    public CompletableFuture<Void> mergeOneAsync(T entity) {
        return null;
    }

    @Override
    public CompletableFuture<Void> mergeAllAsync(Collection<T> entities) {
        return asyncTransactionOf(session -> entities.forEach(entity -> merge(entity, session)));
    }

    @Override
    public CompletableFuture<Void> removeOneAsync(T entity) {
        return asyncTransactionOf(session -> remove(entity, session));
    }

    @Override
    public CompletableFuture<Void> removeAllAsync(Collection<T> entities) {
        return asyncTransactionOf(session -> entities.forEach(entity -> remove(entity, session)));
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return sessionFactory.getCriteriaBuilder();
    }

    @Override
    public void execute(String sql, Consumer<Query> setParams) {
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery(sql);
            setParams.accept(query);
            query.executeUpdate();
        }
    }


    @Override
    public <R> void querySingle(String sql, Class<R> result, Consumer<Query> setParams, Consumer<Optional<R>> resultConsumer) {
        try (Session session = sessionFactory.openSession()) {
            Query<R> query = session.createQuery(sql, result);
            setParams.accept(query);
            R r = query.getSingleResult();
            resultConsumer.accept(Optional.ofNullable(r));
        }
    }

    @Override
    public <R> void queryMultiple(String sql, Class<R> result, Consumer<Query> setParams, Consumer<Collection<R>> resultConsumer) {
        try (Session session = sessionFactory.openSession()) {
            Query<R> query = session.createQuery(sql, result);
            setParams.accept(query);
            List<R> r = query.getResultList();
            resultConsumer.accept(r);
        }
    }

    @Override
    public <R> void querySingle(CriteriaQuery<R> query, Consumer<Query> setParams, Consumer<Optional<R>> resultConsumer) {
        try (Session session = sessionFactory.openSession()) {
            Query<R> q = session.createQuery(query);
            setParams.accept(q);
            R r = q.getSingleResult();
            resultConsumer.accept(Optional.ofNullable(r));
        }
    }

    @Override
    public <R> void queryMultiple(CriteriaQuery<R> query, Consumer<Query> setParams, Consumer<Collection<R>> resultConsumer) {
        try (Session session = sessionFactory.openSession()) {
            Query<R> q = session.createQuery(query);
            setParams.accept(q);
            List<R> r = q.getResultList();
            resultConsumer.accept(r);
        }
    }

}
