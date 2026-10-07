package com.bizengine.repository;

import com.bizengine.exception.DuplicateEntityException;
import com.bizengine.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/** Generic map-backed store; LinkedHashMap keeps insertion order. */
public class InMemoryRepository<T, ID> implements Repository<T, ID> {
    private final Map<ID, T> store = new LinkedHashMap<>();
    private final Function<T, ID> idExtractor;

    public InMemoryRepository(Function<T, ID> idExtractor) {
        this.idExtractor = idExtractor;
    }

    @Override
    public void add(T entity) throws DuplicateEntityException {
        ID id = idExtractor.apply(entity);
        if (store.containsKey(id)) throw new DuplicateEntityException(id);
        store.put(id, entity);
    }

    @Override
    public void update(T entity) throws EntityNotFoundException {
        ID id = idExtractor.apply(entity);
        if (!store.containsKey(id)) throw new EntityNotFoundException(id);
        store.put(id, entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public T getById(ID id) throws EntityNotFoundException {
        return findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean remove(ID id) {
        return store.remove(id) != null;
    }
}
