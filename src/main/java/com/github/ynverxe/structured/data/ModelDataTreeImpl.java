package com.github.ynverxe.structured.data;

import com.github.ynverxe.structured.exception.MissingValueException;
import com.github.ynverxe.structured.recursive.RecursiveTree;
import com.github.ynverxe.structured.exception.PathHolderException;
import com.github.ynverxe.structured.recursive.RecursiveResult;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ModelDataTreeImpl implements ModelDataTree, RecursiveTree.Mutable<ModelDataValue> {

    private final Map<String, ModelDataValue> backing = new HashMap<>();

    @Override
    public ModelDataValue getValue(String key) throws MissingValueException {
        RecursiveResult<ModelDataValue> result = RecursiveResult.of(key, this, false);

        if (result == null || result.getValue() == null) throw new MissingValueException(key);

        return result.getValue();
    }

    @Override
    public Optional<ModelDataValue> safeGet(String key) {
        try {
            return Optional.of(getValue(key));
        } catch (MissingValueException ignore) {
            return Optional.empty();
        }
    }

    @Override
    public ModelDataValue setValue(String key, Object value) {
        RecursiveResult<ModelDataValue> result = RecursiveResult.of(key, this, true);

        ModelDataTreeImpl tree = (ModelDataTreeImpl) result.getLastNode();

        ModelDataValue previous = tree.get(key);

        try {
            tree.set(result.getLastPart(), ModelDataValue.ensureSafety(value));
        } catch (PathHolderException e) {
            e.insertMissingPath(key);
            throw e;
        }

        return previous;
    }

    @Override
    public @Nullable ModelDataValue removeValue(String key) {
        RecursiveResult<ModelDataValue> result = RecursiveResult.of(key, this, false);

        if (result == null || result.getLastNode() == null) return backing.remove(key);

        return ((ModelDataTree) result.getLastNode()).removeValue(key);
    }

    @Override
    public boolean contains(String key) {
        return backing.containsKey(key);
    }

    @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(backing.keySet());
    }

    @Override
    public Collection<ModelDataValue> values() {
        return Collections.unmodifiableCollection(backing.values());
    }

    @Override
    public void forEach(BiConsumer<String, ModelDataValue> action) {
        backing.forEach(action);
    }

    @Override
    public Map<String, Object> simplify() {
        return backing.entrySet()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue().trySimplifyValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<Map.Entry<String, ModelDataValue>> entries() {
        return backing.entrySet()
                .stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public void consume(ModelDataTree another, boolean copyAll) {
        another.forEach((k, v) -> setValue(k, copyAll ? v.copy() : v));
    }

    @Override
    public void set(String key, ModelDataValue value) {
        Object internal = value.getValue();

        backing.put(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends RecursiveTree.Mutable<ModelDataValue>> T createBranch(String key) {
        ModelDataTreeImpl branch = new ModelDataTreeImpl();
        set(key, new ModelDataValue(branch)); // validate if THE CURRENT TREE (could be root or branch) SCHEME expects a ModelDataTree
        return (T) branch;
    }

    @Override
    public ModelDataValue get(String key) {
        return backing.get(key);
    }

    @Override
    public @Nullable RecursiveTree<ModelDataValue> getBranch(String key) {
        return (ModelDataTreeImpl) get(key).asTree();
    }

    @Override
    public String toString() {
        return backing.toString();
    }

    @Override
    public ModelDataTree copy() {
        ModelDataTree modelDataTree = new ModelDataTreeImpl();

        forEach((k, v) -> modelDataTree.setValue(k, v.copy()));

        return modelDataTree;
    }
}