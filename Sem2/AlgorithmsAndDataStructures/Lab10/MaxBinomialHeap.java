import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

public class MaxBinomialHeap<T>{
    private final Comparator<? super T> comparator;
    private LinkedList<BinomialTree<T>> roots;

    public MaxBinomialHeap(Comparator<? super T> comp){
        this.comparator=comp;
        this.roots = new LinkedList<>();
    }
    public MaxBinomialHeap(Comparator<? super T> comp, T value){
        this(comp);
        this.roots.add(new BinomialTree<>(this.comparator, value));
    }

    public T findMax(){
        if(isEmpty()){
            return null;
        }
        T maxValue = null;
        T val;
        for(BinomialTree<T> root : roots){
            val = root.getValue();
            if(maxValue == null || comparator.compare(val, maxValue)>0){
                maxValue = val;
            }
        }
        return maxValue;
    }

    public void insert(T value){
        if(isEmpty()){
            this.roots.add(new BinomialTree<>(this.comparator, value));
        }
        else{
            MaxBinomialHeap<T> heap = new MaxBinomialHeap<>(this.comparator, value);
            this.union(heap);
        }
    }

    public void union(MaxBinomialHeap<T> other){
        if(other == null){
            return;
        }
        if(this.isEmpty()){
            this.roots = new LinkedList<>(other.roots);
            return;
        }
        LinkedList<BinomialTree<T>> merged  = mergeRootLists(this.roots, other.roots);
        this.roots = consolidate(merged);
    }

    public T extractMax(){
        if(this.isEmpty()){return null;}

        BinomialTree<T> maxRoot = null;
        for(BinomialTree<T> r: this.roots){
            if(maxRoot == null || comparator.compare(r.getValue(),maxRoot.getValue())>0){
                maxRoot = r;
            }
        }
        if(maxRoot == null){
            return null;
        }

        roots.remove(maxRoot);

        if(maxRoot.getChild() == null){
            return maxRoot.getValue();
        }

        LinkedList<BinomialTree<T>> children = new LinkedList<>();
        BinomialTree<T> child = maxRoot.getChild();
        while(child != null){
            child.setParent(null);
            BinomialTree<T> sibling = child.getSibling();
            child.setSibling(null);
            children.addFirst(child); // addFirst for reversed order
            child = sibling;
        }
        MaxBinomialHeap<T> temp = new MaxBinomialHeap<>(this.comparator);
        temp.roots = children;
        this.union(temp);
        return maxRoot.getValue();
    }

    public boolean isEmpty(){
        return this.roots.isEmpty();
    }

    private LinkedList<BinomialTree<T>> mergeRootLists(LinkedList<BinomialTree<T>> a, LinkedList<BinomialTree<T>> b){
        LinkedList<BinomialTree<T>> result = new LinkedList<>();

        Iterator<BinomialTree<T>> itA = a.iterator();
        Iterator<BinomialTree<T>> itB = b.iterator();

        BinomialTree<T> currentA = itA.hasNext() ? itA.next() : null;
        BinomialTree<T> currentB = itB.hasNext() ? itB.next() : null;

        while(currentA != null || currentB != null){
            if(currentB == null || (currentA != null && currentA.getDegree() < currentB.getDegree())){
                result.add(currentA);
                currentA = itA.hasNext() ? itA.next() : null;
            }
            else{
                result.add(currentB);
                currentB = itB.hasNext() ? itB.next() : null;
            }
        }
        return result;
    }

    private LinkedList<BinomialTree<T>> consolidate(LinkedList<BinomialTree<T>> merged){
        if (merged.isEmpty()) {
            return merged;
        }
        LinkedList<BinomialTree<T>> result = new LinkedList<>();
        Iterator<BinomialTree<T>> it = merged.iterator();

        BinomialTree<T> prev = it.next();
        BinomialTree<T> curr = it.hasNext() ? it.next():null;

        while(curr != null){
            if(prev.getDegree() != curr.getDegree()){
                result.add(prev);
                prev = curr;
                curr = it.hasNext() ? it.next():null;
            }
            else{
                // Check if third element with the same degree exist (It can exist due to previous consolidations)
                BinomialTree<T> next = it.hasNext() ? it.next() : null;

                if(next != null && next.getDegree() == prev.getDegree()){
                    result.add(prev);
                    prev = curr;
                }
                else{
                    prev = prev.merge(curr);
                    // Don't add prev to result as it might have same degree as new curr
                }
                curr = next;
            }
        }
        // Add last prev (when curr == null)
        result.add(prev);
        return result;
    }
}
