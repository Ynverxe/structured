package com.github.ynverxe.structured.data;

import com.github.ynverxe.structured.exception.UnexpectedValueStateException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class ModelDataValue implements CopyableDataValue {

    private static final List<Class<?>> ACCEPTABLE_TYPES = Arrays.asList(
            ModelDataTree.class, ModelDataList.class, Character.TYPE,
            String.class, Number.class, Boolean.class
    );

    private final Object value;

    public ModelDataValue(@NotNull Object value) {
        for (Class<?> acceptableType : ACCEPTABLE_TYPES) {
            if (acceptableType.isInstance(value)) {
                this.value = value;
                return;
            }
        }

        if (value instanceof ModelDataHolder) {
            value = ((ModelDataHolder) value).toModelData();
        }

        if (value instanceof ModelDataValue) {
            this.value = ((ModelDataValue) value).value;
            return;
        }

        throw new IllegalArgumentException("Unacceptable type: " + value.getClass());
    }

    public ModelDataTree asTree() {
        return expect(ModelDataTree.class);
    }

    public boolean isTree() {
        return value instanceof ModelDataTree;
    }

    public ModelDataList asList() {
        return expect(ModelDataList.class);
    }

    public boolean isList() {
        return value instanceof List;
    }

    public Character asCharacter() {
        return expect(Character.class);
    }

    public boolean isCharacter() {
        return value instanceof Character;
    }

    public String asString() {
        return expect(String.class);
    }

    public boolean isString() {
        return value instanceof String;
    }

    public Number asNumber() {
        return expect(Number.class);
    }

    public int asInt() {
        return asNumber().intValue();
    }

    public short asShort() {
        return asNumber().shortValue();
    }

    public byte asByte() {
        return asNumber().byteValue();
    }

    public double asDouble() {
        return asNumber().doubleValue();
    }

    public float asFloat() {
        return asNumber().floatValue();
    }

    public long asLong() {
        return asNumber().longValue();
    }

    public boolean isNumber() {
        return value instanceof Number;
    }

    public Boolean asBoolean() {
        return expect(Boolean.class);
    }

    public boolean isBoolean() {
        return value instanceof Boolean;
    }

    public Object getValue() {
        return value;
    }

    public Object trySimplifyValue() {
        return value instanceof SimplifiableDataValue ? ((SimplifiableDataValue<?>) value).simplify() : value;
    }

    public <T> T expect(Class<T> expected) {
        if (!expected.isInstance(value))
            throw new UnexpectedValueStateException("Expected: " + expected + ", but found: " + value.getClass());

        return expected.cast(value);
    }

    @Override
    public String toString() {
        return Objects.toString(value);
    }

    @Override
    public ModelDataValue copy() {
        return new ModelDataValue(value instanceof CopyableDataValue ? ((CopyableDataValue) value).copy() : value);
    }

    @SuppressWarnings("unchecked")
    public static @NotNull ModelDataValue ensureSafety(Object value) {
        if (value instanceof List) {
            ModelDataList list = new ModelDataList();

            ((List<?>) value).forEach(element -> list.add(ensureSafety(element)));
            return new ModelDataValue(list);
        }

        if (value instanceof Map) {
            ModelDataTree tree = new ModelDataTreeImpl();

            ((Map<String, Object>) value).forEach(tree::setValue);
            return new ModelDataValue(tree);
        }

        return new ModelDataValue(value);
    }
}