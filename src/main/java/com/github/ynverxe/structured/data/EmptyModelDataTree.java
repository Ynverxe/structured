package com.github.ynverxe.structured.data;

import com.github.ynverxe.structured.exception.MissingValueException;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;

public class EmptyModelDataTree implements ModelDataTree {

    EmptyModelDataTree() {}

    @Override
    public ModelDataValue getValue(String key) throws MissingValueException {
        throw new MissingValueException(key);
    }

    @Override
    public Optional<ModelDataValue> safeGet(String key) {
        return Optional.empty();
    }

    @Override
    public @Nullable ModelDataValue setValue(String key, Object value) {
        return null;
    }

    @Override
    public @Nullable ModelDataValue removeValue(String key) {
        return null;
    }

    @Override
    public boolean contains(String key) {
        return false;
    }

    @Override
    public Set<String> keys() {
        return null;
    }

    @Override
    public Collection<ModelDataValue> values() {
        return null;
    }

    @Override
    public void forEach(BiConsumer<String, ModelDataValue> action) {

    }

    @Override
    public Map<String, Object> simplify() {
        return new HashMap<>();
    }

    @Override
    public List<Map.Entry<String, ModelDataValue>> entries() {
        return new ArrayList<>();
    }

    @Override
    public void consume(ModelDataTree another, boolean copyAll) {

    }

    @Override
    public ModelDataTree copy() {
        return EMPTY;
    }
}