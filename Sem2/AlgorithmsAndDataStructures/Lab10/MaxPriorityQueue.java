import java.util.Comparator;

public class MaxPriorityQueue<P,E>{
    private class Element{
        private final P priority;
        private final E element;
        public Element(P priority, E element){
            this.priority = priority;
            this.element = element;
        }
        public E getElement(){
            return this.element;
        }

        public P getPriority(){
            return this.priority;
        }
    }

    private class ElementComparator implements Comparator<Element> {

        private final Comparator<? super P> comparator;

        public ElementComparator(Comparator<? super P> comparator) {
            this.comparator = comparator;
        }

        @Override
        public final int compare(Element x, Element y) {
            return comparator.compare(x.getPriority(), y.getPriority());
        }
    }

    private final MaxBinomialHeap<Element> list;

    public MaxPriorityQueue(Comparator<? super P> comparator){
        Comparator<Element> comp = new ElementComparator(comparator);
        this.list = new MaxBinomialHeap<>(comp);
    }

    public void enqueue(P priority, E element){
        this.list.insert(new Element(priority, element));
    }

    public E dequeue() throws EmptyQueueException{
        if(isEmpty()){
            throw new EmptyQueueException();
        }
        return this.list.extractMax().getElement();
    }

    public E peek() throws EmptyQueueException{
        if(isEmpty()){
            throw new EmptyQueueException();
        }
        return this.list.findMax().getElement();
    }

    public boolean isEmpty(){
        return this.list.isEmpty();
    }
}
