package com.outcast.rpgcore.db;

import com.outcast.rpgcore.db.cache.Cache;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.Query;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class CachedRepository<T extends Identifiable<ID>, ID extends Serializable> extends Repository<T, ID> {

    protected Cache<T, ID> cache;

    public CachedRepository(Class<T> persistable) {
        super(persistable);
    }

    @Override
    public Optional<T> findById(ID id) {
        return cache.getById(id);
    }

    public Optional<T> findOne(Predicate<T> condition) {
        return cache.findOne(condition);
    }

    public Collection<T> findAll(Predicate<T> condition) {
        return cache.findAll(condition);
    }

    @Override
    public void mergeOne(T entity) {
        super.mergeOne(entity);
        cache.add(entity);
    }

    @Override
    public void mergeAll(Collection<T> entities) {
        super.mergeAll(entities);
        cache.addAll(entities);
    }

    @Override
    public void removeOne(T entity) {
        super.removeOne(entity);
        cache.remove(entity);
    }

    @Override
    public void removeAll(Collection<T> entities) {
        super.removeAll(entities);
        cache.removeAll(entities);
    }

    @Override
    public CompletableFuture<Void> mergeOneAsync(T entity) {
        return CompletableFuture.runAsync(() -> mergeOne(entity));
    }

    @Override
    public CompletableFuture<Void> mergeAllAsync(Collection<T> entities) {
        return CompletableFuture.runAsync(() -> mergeAll(entities));
    }

    @Override
    public CompletableFuture<Void> removeOneAsync(T entity) {
        return CompletableFuture.runAsync(() -> removeOne(entity));
    }

    @Override
    public CompletableFuture<Void> removeAllAsync(Collection<T> entities) {
        return CompletableFuture.runAsync(() -> removeAll(entities));
    }

    @Override
    public <R> void querySingle(String jpql, Class<R> result, Consumer<Query> setParams, Consumer<Optional<R>> resultConsumer) {
        throw new UnsupportedOperationException("Cannot query cached repositories. Use findAll, findOne, or extend a non-cached repository instead.");
    }

    @Override
    public <R> void queryMultiple(String jpql, Class<R> result, Consumer<Query> setParams, Consumer<Collection<R>> resultConsumer) {
        throw new UnsupportedOperationException("Cannot query cached repositories. Use findAll, findOne, or extend a non-cached repository instead.");
    }

    @Override
    public <R> void querySingle(CriteriaQuery<R> query, Consumer<Query> setParams, Consumer<Optional<R>> resultConsumer) {
        throw new UnsupportedOperationException("Cannot query cached repositories. Use findAll, findOne, or extend a non-cached repository instead.");
    }

    @Override
    public <R> void queryMultiple(CriteriaQuery<R> query, Consumer<Query> setParams, Consumer<Collection<R>> resultConsumer) {
        throw new UnsupportedOperationException("Cannot query cached repositories. Use findAll, findOne, or extend a non-cached repository instead.");
    }

    public void init() {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(persistable);
        Root<T> varRoot = query.from(persistable);

        query.select(varRoot);
        super.queryMultiple(query, (q) -> {}, entities -> entities.forEach(entity -> cache.set(entity.getId(), entity)));
    }

    public void flush() {
        super.mergeAll(cache.getAll());
    }

}
