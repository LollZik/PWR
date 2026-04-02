public interface TrieElement<V> {
    V getValue();
    void setValue(V value);
    boolean isLeaf();
}
