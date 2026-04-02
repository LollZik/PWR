import java.util.Iterator;
import java.util.ListIterator;

public class TwoWayLinkedList<E> implements IList<E> {
    protected class Element{
        private E value;
        private Element next;
        private Element prev;

        public E getValue(){
            return value;
        }
        public void setValue(E value){
            this.value = value;
        }
        public Element getNext(){
            return next;
        }
        public void setNext(Element next){
            this.next = next;
        }
        public Element getPrev() {
            return prev;
        }
        public void setPrev(Element prev){
            this.prev = prev;
        }
        public Element getActualNext(){
            if ( !this.getPrev().equals(headSentinel)){
                return this.getPrev().getNext();
            }
            else{
                if(this.getNext().equals(tailSentinel)){
                    if(this.equals(tail)){
                        return tailSentinel;
                    }
                    return tail;
                }
                return this.getNext().getPrev();
            }
        }
        Element(E data){
            this.value = data;
        }

        public void insertAfter(Element elem) {
            if(size == 0){
                head = elem;
                tail = elem;
                elem.setNext(tailSentinel);
                elem.setPrev(headSentinel);
                head = elem;
                tail = elem;
                return;
            }
            if(size == 1){
                tailSentinel.setPrev(elem);
                tail = elem;
                elem.setPrev(this);
                elem.setNext(tailSentinel);
                return;
            }

            elem.setPrev(this);
            this.getPrev().getPrev().setNext(elem);
            if(this.equals(tail)){
                elem = tail;
                elem.setNext(tailSentinel);
            }
            else{
                this.getPrev().getNext().setPrev(elem);
            }
        }

        public void insertBefore(Element elem){
            if(size == 0){
                throw new IndexOutOfBoundsException();
            }

            elem.setPrev(this.getPrev());
            this.setPrev(elem);
            this.getPrev().setNext(this);
            if(this.equals(tail)){
                elem.setNext(tailSentinel);
            }
            else{
                elem.setNext(this.getNext().getPrev());
            }
        }

        public void remove(){
            if(this.equals(headSentinel) && this.equals(tailSentinel)){
                throw new RuntimeException("Próba usunięcia sentinela");
            }
            if (this == tail) {
                tail = this.getPrev();
                tailSentinel.setPrev(tail);
                return;
            }
            this.getPrev().getNext().setPrev(this.getPrev());
            this.getPrev().setNext(this.getNext());
        }
    }


    private final Element headSentinel;
    private final Element tailSentinel;
    private Element head;
    private Element tail;
    private int size;

    public TwoWayLinkedList() {
        headSentinel = new Element(null);
        tailSentinel = new Element(null);
        head = null;
        tail = null;
        size = 0;

        headSentinel.setNext(tailSentinel);
        headSentinel.setPrev(headSentinel);
        tailSentinel.setPrev(headSentinel);
        tailSentinel.setNext(tailSentinel);
    }

    public void addFirst(E value) {
        Element newElement = new Element(value);

        if (isEmpty()) {
            headSentinel.insertAfter(newElement);
            head = newElement;
            tail = newElement;
        } else {
            head.insertBefore(newElement);
            head = newElement;
        }

        size++;
    }


    public void addLast(E value) {
        Element newElement = new Element(value);

        if(isEmpty()){
            headSentinel.insertAfter(newElement);
            head = newElement;
        }
        else{
            tail.insertAfter(newElement);
        }
        tail = newElement;

        size++;
    }
    public void add(int index, E value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == 0) {
            addFirst(value);
        } else if (index == size) {
            addLast(value);
        } else {
            Element current = getElement(index);
            Element newElement = new Element(value);
            current.insertBefore(newElement);

            size++;
        }
    }

    public E removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot remove from an empty list");
        }

        Element removed = head;
        E value = removed.getValue();

        if (head == tail) {
            head.remove();
            head = null;
            tail = null;
        } else {
            head = head.getNext().getPrev();
            removed.remove();
        }

        size--;
        return value;
    }


    public E removeLast() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot remove from an empty list");
        }

        Element removed = tail;
        E value = removed.getValue();

        if (head == tail) {
            tail.remove();
            head = null;
            tail = null;
        } else {
            tail = tail.getPrev();
            removed.remove();
        }
        size--;
        return value;
    }

    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if(index == 0){
            return removeFirst();
        }
        else if(index == size - 1){
            return removeLast();
        }
        else{
            Element current = getElement(index);
            E value = current.getValue();
            current.remove();

            size--;
            return value;
        }
    }

    @Override
    public boolean remove(E element) {
        int index = indexOf(element);
        if (index == -1){
            return false;
        }
        remove(index);
        return true;
    }

    public E getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return head.getValue();
    }

    public E getLast() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return tail.getValue();
    }

    public E get(int index){
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Element elem = getElement(index);
        return elem.getValue();
    }

    public E set(int index, E data){
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Element elem = getElement(index);

        elem.setValue(data);
        return elem.getValue();
    }
    public int indexOf(E data){
        if(isEmpty()){
            return -1;
        }
        Element current = head;
        int index = 0;

        while(current != tailSentinel){
            if((data == null && current.getValue() == null) || (data != null && data.equals(current.getValue()))){
                return index;
            }
            current = current.getActualNext();
            index++;
        }
        return -1;
    }

    public Iterator<E> iterator(){
        throw new UnsupportedOperationException();
    }

    public ListIterator<E> listIterator(){
        throw new UnsupportedOperationException();
    }

    protected Element getElement(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Element current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.getActualNext();
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.getPrev();
            }
        }
        return current;
    }

    public boolean contains(E value) {
        return indexOf(value) != -1;
    }

    public int size() {
        return size;
    }


    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {

        headSentinel.setNext(tailSentinel);
        tailSentinel.setPrev(headSentinel);
        head = null;
        tail = null;
        size = 0;
    }
}