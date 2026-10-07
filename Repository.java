package com.bizengine.repository;

import com.bizengine.exception.DuplicateEntityException;
import com.bizengine.exception.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public interface Repository<T, ID> {
    void add(T entity) throws DuplicateEntityException;
    void update(T entity) throws EntityNotFoundException;
    Optional<T> findById(ID id);
    T getById(ID id) throws EntityNotFoundException;
    List<T> findAll();
    boolean remove(ID id);
}
