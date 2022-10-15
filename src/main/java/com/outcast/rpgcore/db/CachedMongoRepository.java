package com.outcast.rpgcore.db;

import com.outcast.rpgcore.db.cache.Cache;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public class CachedMongoRepository<T extends Identifiable<ID>, ID extends Serializable> extends MongoRepository<T, ID> {

    protected Cache<T, ID> cache;

    public CachedMongoRepository(Class<T> persistable) {
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
    public void saveOne(T entity) {
        super.saveOne(entity);
        cache.add(entity);
    }

    @Override
    public void saveAll(Collection<T> entities) {
        super.saveAll(entities);
        cache.addAll(entities);
    }

    @Override
    public void deleteOne(T entity) {
        super.deleteOne(entity);
        cache.remove(entity);
    }

    @Override
    public void deleteAll(Collection<T> entities) {
        super.deleteAll(entities);
        cache.removeAll(entities);
    }

    @Override
    public CompletableFuture<Void> saveOneAsync(T entity) {
        return CompletableFuture.runAsync(() -> saveOne(entity));
    }

    @Override
    public CompletableFuture<Void> saveAllAsync(Collection<T> entities) {
        return CompletableFuture.runAsync(() -> saveAll(entities));
    }

    @Override
    public CompletableFuture<Void> deleteOneAsync(T entity) {
        return CompletableFuture.runAsync(() -> deleteOne(entity));
    }

    @Override
    public CompletableFuture<Void> deleteAllAsync(Collection<T> entities) {
        return CompletableFuture.runAsync(() -> deleteAll(entities));
    }

    public void init() {}

    public void flush() {
        super.saveAll(cache.getAll());
    }

}
