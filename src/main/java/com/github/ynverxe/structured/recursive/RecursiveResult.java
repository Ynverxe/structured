package com.github.ynverxe.structured.recursive;

public class RecursiveResult<V> {

    private final RecursiveTree<V> lastNode;
    private final String lastPart;
    private final V value;

    public RecursiveResult(RecursiveTree<V> lastNode, String lastPart, V value) {
        this.lastNode = lastNode;
        this.lastPart = lastPart;
        this.value = value;
    }

    public String getLastPart() {
        return lastPart;
    }

    public V getValue() {
        return value;
    }

    public RecursiveTree<V> getLastNode() {
        return lastNode;
    }

    public static <V> RecursiveResult<V> of(String path, RecursiveTree<V> tree, boolean createPath) {
        String[] separatted = path.split("\\.");

        if (separatted.length == 1) {
            return new RecursiveResult<>(tree, path, tree.get(path));
        } else {
            String lastPart = separatted[separatted.length - 1];

            RecursiveTree<V> current = tree;
            for (String s : separatted) {
                if (lastPart.equals(s)) continue;

                RecursiveTree.Mutable<V> temp = null;

                try {
                    temp = (RecursiveTree.Mutable<V>) current.getBranch(s);
                } catch (Exception ignore) {}

                if (temp == null && createPath) {
                    if (!(current instanceof RecursiveTree.Mutable))
                        return null;

                    temp = ((RecursiveTree.Mutable<V>) current).createBranch(s);
                }

                if (temp == null) return null;

                current = temp;
            }

            return new RecursiveResult<>(current, lastPart, current.get(lastPart));
        }
    }
}