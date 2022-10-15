package com.outcast.rpgcore.db;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Repository<T extends Identifiable<ID>, ID extends Serializable> {

    /**
     * Find an entity by it's id
     *
     * @param id The id to look for
     * @return An optional containing the entity. Empty if not found.
     */
    Optional<T> findById(ID id);

    void saveOne(T entity);

    void saveAll(Collection<T> entities);

    void deleteOne(T entity);

    void deleteAll(Collection<T> entities);

    /**
     * Insert or Update an entity to the database
     *
     * @param entity The entity to persist
     */
    CompletableFuture<Void> saveOneAsync(T entity);

    /**
     * Insert or Update multiple entities to the database
     *
     * @param entities The entities to persist
     */
    CompletableFuture<Void> saveAllAsync(Collection<T> entities);

    /**
     * Delete an entity from the database
     *
     * @param entity The entity to delete
     */
    CompletableFuture<Void> deleteOneAsync(T entity);

    /**
     * Delete multiple entities from the database
     *
     * @param entities The entities to delete
     */
    CompletableFuture<Void> deleteAllAsync(Collection<T> entities);

    //===========================================================================================================
    //  Query functions below for querying single multiple and execution of mongodb.
    //===========================================================================================================

}
