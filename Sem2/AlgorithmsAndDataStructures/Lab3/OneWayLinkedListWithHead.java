import java.util.Iterator;
import java.util.ListIterator;

public class OneWayLinkedListWithHead<E> implements IList<E> {
    protected class Element {
        private E value;
        private Element next;

        public E getValue() {
            return value;
        }

        public void setValue(E value) {
            this.value = value;
        }

        public Element getNext() {
            return next;
        }

        public void setNext(Element next) {
            this.next = next;
        }

        Element(E data) {
            this.value = data;
        }
    }

    Element head = null;

    public OneWayLinkedListWithHead() {
    }

    public boolean isEmpty() {
        return head == null;
    }

    protected Element getElement(int index){
        if (index<0) throw new IndexOutOfBoundsException();
        Element actElem = head;
        while(index>0 && actElem!= null){
            index--;
            actElem = actElem.getNext();
        }
        if(actElem == null){
            throw new IndexOutOfBoundsException();
        }
        return actElem;
    }

    @Override
    public void clear() {
        head = null;
    }
    @Override
    public boolean add(E e){  // dodanie elementu na koniec listy
        Element newElem = new Element(e);
        if (head == null){
            head = newElem;
            return true;
        }
        Element tail = head;
        while(tail.getNext() != null){
            tail = tail.getNext();
        }
        tail.setNext(newElem);
        return true;
    }
    @Override
    public void add(int index, E data) { // dodanie elementu na podanej pozycji
        if(index <0) throw new IndexOutOfBoundsException();
        Element newElem = new Element(data);
        if (index==0){
            newElem.setNext(head);
            head = newElem;
        }
        Element actElem = getElement(index-1);
        newElem.setNext(actElem.getNext());
        actElem.setNext(newElem);
    }
    @Override
    public boolean contains(E data) { // czy lista zawiera podany element (equals())
        return indexOf(data)>=0;
    }
    @Override
    public E get(int index) { // pobranie elementu z podanej pozycji
        Element actElem = getElement(index);
        return actElem.getValue();
    }
    @Override
    public E set(int index, E data) { // ustawienie nowej wartości na pozycji
        Element actElem = getElement(index);
        E elemData = actElem.getValue();
        actElem.setValue(data);
        return elemData;
    }
    @Override
    public int indexOf(E data) { // pozycja szukanego elementu (equals())
        int pos = 0;
        Element actElem = head;
        while(actElem!=null){
            if(actElem.getValue().equals(data)) return pos;
            pos++;
            actElem = actElem.getNext();
        }
        return -1;
    }
    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }
    @Override
    public ListIterator<E> listIterator() {
        throw new UnsupportedOperationException();
    }
    @Override
    public E remove(int index) { // usuwa element z podanej pozycji
        if(index < 0 || head == null) throw new IndexOutOfBoundsException();
        if(index == 0){
            E retValue= head.getValue();
            head=head.getNext();
            return retValue;
        }
        Element actElem = getElement(index-1);
        if(actElem.getNext()==null){
            throw new IndexOutOfBoundsException();
        }
        E retValue = actElem.getNext().getValue();
        actElem.setNext(actElem.getNext().getNext());

        return retValue;
    }
    @Override
    public boolean remove(E value) { // usuwa element (equals())
        if(head == null){
            return false;
        }
        if(head.getValue().equals(value)){
            head = head.getNext();
            return true;
        }
        Element actElem = head;
        while(actElem.getNext()!= null && !actElem.getNext().getValue().equals(value)){
            actElem = actElem.getNext();
        }
        if(actElem.getNext() == null){
            return false;
        }
        actElem.setNext(actElem.getNext().getNext());
        return true;
    }
    @Override
    public int size(){
        int pos = 0;
        Element actElem = head;
        while (actElem != null){
            pos++;
            actElem = actElem.getNext();
        }
        return pos;
    }

    public void printStructure() {
            Element elem = head;
            while (elem != null) {
                System.out.print(elem.getValue() + " -> ");
                elem = elem.getNext();
            }
            System.out.println("☒");
    }
    public void reverse(){
        int i = 0;
        int size = size();
        if(size>1){
            Element prevElem = getElement(0);
            Element nextElem = prevElem.getNext();
            prevElem.setNext(null);
            while(i+1<size){
                Element temp = nextElem.getNext();
                nextElem.setNext(prevElem);
                prevElem = nextElem;
                nextElem = temp;
                i++;
            }
            this.head = prevElem;
        }
    }
}