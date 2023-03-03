package com.github.ynverxe.structured.data;

import com.github.ynverxe.structured.exception.MissingValueException;
import com.github.ynverxe.structured.exception.PathHolderException;
import com.github.ynverxe.structured.exception.ValueMappingException;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface ModelDataTree extends CopyableDataValue, SimplifiableDataValue<Map<String, Object>> {

    EmptyModelDataTree EMPTY = new EmptyModelDataTree();

    ModelDataValue getValue(String key) throws MissingValueException;

    Optional<ModelDataValue> safeGet(String key);

    @Nullable ModelDataValue setValue(String key, Object value);

    @Nullable ModelDataValue removeValue(String key);

    default <T> T mapValue(String key, Function<ModelDataValue, T> mapper) {
        try {
            return mapper.apply(getValue(key));
        } catch (MissingValueException e) {
            throw e;
        } catch (Exception e) {
            if (e instanceof PathHolderException) {
                PathHolderException pathHolderException = (PathHolderException) e;

                if (pathHolderException.getPath() == null)
                    pathHolderException.setPath(key);

                throw e;
            }

            throw new ValueMappingException(e).setPath(key);
        }
    }

    default void consumeValue(String key, Consumer<ModelDataValue> consumer) {
        try {
            consumer.accept(getValue(key));
        } catch (MissingValueException e) {
            throw e;
        } catch (Exception e) {
            if (e instanceof PathHolderException) {
                PathHolderException pathHolderException = (PathHolderException) e;

                if (pathHolderException.getPath() == null)
                    pathHolderException.setPath(key);

                throw e;
            }

            throw new ValueMappingException(e).setPath(key);
        }
    }

    default ModelDataValue setValueIfAbsent(String key, Supplier<?> valueSupplier) {
        ModelDataValue value;
        try {
             value = getValue(key);
        } catch (MissingValueException e) {
            setValue(key, value = new ModelDataValue(valueSupplier.get()));
        }

        return value;
    }

    default ModelDataList setListIfAbsent(String key) {
        return setValueIfAbsent(key, ModelDataList::new).asList();
    }

    default ModelDataTree setTreeIfAbsent(String key) {
        return setValueIfAbsent(key, ModelDataTreeImpl::new).asTree();
    }

    boolean contains(String key);

    Set<String> keys();

    Collection<ModelDataValue> values();

    void forEach(BiConsumer<String, ModelDataValue> action);

    Map<String, Object> simplify();

    List<Map.Entry<String, ModelDataValue>> entries();

    void consume(ModelDataTree another, boolean copyAll);

    @Override
    ModelDataTree copy();
}