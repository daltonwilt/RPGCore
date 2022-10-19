package com.outcast.rpgcore.storage.repository;

import com.outcast.rpgcore.storage.Identifiable;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface IRepository<T extends Identifiable<ID>, ID extends Serializable> {

    /**
     * Find an entity by it's id
     *
     * @param id The id to look for
     * @return An optional containing the entity. Empty if not found.
     */
    Optional<T> findById(ID id);

    void mergeOne(T entity);

    void mergeAll(Collection<T> entities);

    void removeOne(T entity);

    void removeAll(Collection<T> entities);

    /**
     * Insert or Update an entity to the database
     *
     * @param entity The entity to persist
     */
    CompletableFuture<Void> mergeOneAsync(T entity);

    /**
     * Insert or Update multiple entities to the database
     *
     * @param entities The entities to persist
     */
    CompletableFuture<Void> mergeAllAsync(Collection<T> entities);

    /**
     * Delete an entity from the database
     *
     * @param entity The entity to delete
     */
    CompletableFuture<Void> removeOneAsync(T entity);

    /**
     * Delete multiple entities from the database
     *
     * @param entities The entities to delete
     */
    CompletableFuture<Void> removeAllAsync(Collection<T> entities);

    /**
     * Retrieve a CriteriaBuilder object from the repository
     *
     * @return A CriteriaBuilder instance
     */
    CriteriaBuilder getCriteriaBuilder();

    void execute(String jpql, Consumer<Query> setParams);

    //===========================================================================================================
    //  Query functions below for querying single multiple and execution of mongodb.
    //===========================================================================================================

    <R> void querySingle(String jpql, Class<R> result, Consumer<Query> setParams, Consumer<Optional<R>> resultConsumer);

    <R> void queryMultiple(String jpql, Class<R> result, Consumer<Query> setParams, Consumer<Collection<R>> resultConsumer);

    default void executeAsync(String jpql, Consumer<Query> setParams) {
        CompletableFuture.runAsync(() -> execute(jpql, setParams));
    }

    default <R> CompletableFuture<Void> querySingleAsync(String jpql, Class<R> result, Consumer<Query> setParams, Consumer<Optional<R>> resultConsumer) {
        return CompletableFuture.runAsync(() -> querySingle(jpql, result, setParams, resultConsumer));
    }

    default <R> CompletableFuture<Void> queryMultipleAsync(String jpql, Class<R> result,  Consumer<Query> setParams, Consumer<Collection<R>> resultConsumer) {
        return CompletableFuture.runAsync(() -> queryMultiple(jpql, result, setParams, resultConsumer));
    }

    <R> void querySingle(CriteriaQuery<R> query, Consumer<Query> setParams, Consumer<Optional<R>> resultConsumer);

    <R> void queryMultiple(CriteriaQuery<R> query, Consumer<Query> setParams, Consumer<Collection<R>> resultConsumer);

    default <R> CompletableFuture<Void> querySingleAsync(CriteriaQuery<R> query, Consumer<Query> setParams, Consumer<Optional<R>> resultConsumer) {
        return CompletableFuture.runAsync(() -> querySingle(query, setParams, resultConsumer));
    }

    default <R> CompletableFuture<Void> queryMultipleAsync(CriteriaQuery<R> query, Consumer<Query> setParams, Consumer<Collection<R>> resultConsumer) {
        return CompletableFuture.runAsync(() -> queryMultiple(query, setParams, resultConsumer));
    }

}
