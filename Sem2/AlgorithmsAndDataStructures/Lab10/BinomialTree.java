import java.util.Comparator;

public class BinomialTree<T>{
    private final Comparator<? super T> comparator;
    private final T value;
    private int degree;
    private BinomialTree<T> parent;
    private BinomialTree<T> child;
    private BinomialTree<T> sibling;

    public BinomialTree(Comparator<? super T> comp, T value){
        this.comparator=comp;
        this.value = value;
        this.degree = 0;
    }

    public BinomialTree<T> merge(BinomialTree<T> other){
        if(getDegree() != other.getDegree()){
            throw new IllegalArgumentException("Trees need to have the same degree.");
        }

        if(comparator.compare(getValue(), other.getValue())>=0){
            return link(other,this);
        }
        else{
            return link(this,other);
        }
    }

    public int getDegree(){
        return this.degree;
    }

    public T getValue(){
        return this.value;
    }

    public void setParent(BinomialTree<T> parent){
        this.parent = parent;
    }

    public BinomialTree<T> getParent(){
        return this.parent;
    }

    public void setChild(BinomialTree<T> child){
        this.child = child;
    }

    public BinomialTree<T> getChild(){
        return this.child;
    }

    public void setSibling(BinomialTree<T> sibling){
        this.sibling = sibling;
    }

    public BinomialTree<T> getSibling(){
        return this.sibling;
    }

    public void increaseDegree(){
        this.degree++;
    }

    private BinomialTree<T> link(BinomialTree<T> child, BinomialTree<T> root){
        child.setParent(root);
        child.setSibling(root.getChild());
        root.setChild(child);
        root.increaseDegree();
        return root;
    }
}

