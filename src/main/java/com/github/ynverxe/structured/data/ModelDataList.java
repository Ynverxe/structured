package com.github.ynverxe.structured.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ModelDataList extends ArrayList<ModelDataValue> implements CopyableDataValue, SimplifiableDataValue<List<Object>> {

    public ModelDataList(int initialCapacity) {
        super(initialCapacity);
    }

    public ModelDataList() {
    }

    public ModelDataList(@NotNull Collection<? extends ModelDataValue> c) {
        super(c);
    }

    @Override
    public ModelDataList copy() {
        ModelDataList list = new ModelDataList();

        forEach(element -> list.add(element.copy()));

        return list;
    }

    @Override
    public List<Object> simplify() {
        return stream()
                .map(ModelDataValue::trySimplifyValue)
                .collect(Collectors.toList());
    }
}