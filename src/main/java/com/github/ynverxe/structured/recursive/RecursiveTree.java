package com.github.ynverxe.structured.recursive;

import org.jetbrains.annotations.Nullable;

public interface RecursiveTree<V> {

    @Nullable V get(String key);

    @Nullable RecursiveTree<V> getBranch(String key);

    interface Mutable<V> extends RecursiveTree<V> {
        void set(String key, V value);

        <T extends Mutable<V>> T createBranch(String key);
    }
}