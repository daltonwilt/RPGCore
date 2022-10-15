package com.outcast.rpgcore.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class MongoRepository<T extends Identifiable<ID>, ID extends Serializable> implements Repository<T, ID> {

    protected Class<T> persistable;

    public MongoRepository(Class<T> persistable) {
        this.persistable = persistable;
    }

    @Override
    public Optional<T> findById(ID id) {
        T result = null;

        // find entity by id

        return Optional.ofNullable(result);
    }

    @Override
    public void saveOne(T entity) {

    }

    @Override
    public void saveAll(Collection<T> entities) {

    }

    @Override
    public void deleteOne(T entity) {

    }

    @Override
    public void deleteAll(Collection<T> entities) {

    }

    @Override
    public CompletableFuture<Void> saveOneAsync(T entity) {
        return null;
    }

    @Override
    public CompletableFuture<Void> saveAllAsync(Collection<T> entities) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteOneAsync(T entity) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteAllAsync(Collection<T> entities) {
        return null;
    }

}
