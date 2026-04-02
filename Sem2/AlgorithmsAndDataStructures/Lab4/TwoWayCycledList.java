public class TwoWayCycledList<E> {
    private class Element{
        private E value;
        private Element next;
        private Element prev;
        public E getValue() { return value; }
        public void setValue(E value) { this.value = value; }
        public Element getNext() {return next;}
        public void setNext(Element next) {this.next = next;}
        public Element getPrev() {return prev;}
        public void setPrev(Element prev) {this.prev = prev;}
        Element(E data){this.value=data;}
        /** elem będzie stawiony <b> za this </b>*/
        public void insertAfter(Element elem){
            elem.setNext(this.getNext());
            elem.setPrev(this);
            this.getNext().setPrev(elem);
            this.setNext(elem);}
        /** elem będzie stawiany <b> przed this </b>*/
        public void insertBefore(Element elem){
            elem.setNext(this);
            elem.setPrev(this.getPrev());
            this.getPrev().setNext(elem);
            this.setPrev(elem);}
        /** elem będzie usuwany z listy w której jest <p>
         * <b>Założenie:</b> element jest już umieszczony w liście i nie jest to sentinel */
        public void remove(){
            this.getNext().setPrev(this.getPrev());
            this.getPrev().setNext(this.getNext());
        }
    }
        Element sentinel=null;
        public TwoWayCycledList(){
            sentinel=new Element(null);
            sentinel.setNext(sentinel);
            sentinel.setPrev(sentinel);
        }
        private Element getElement(int index){
            if(index<0) throw new IndexOutOfBoundsException();
            Element elem=sentinel.getNext();
            int counter=0;
            while(elem!=sentinel && counter<index){
                counter++;
                elem=elem.getNext();}
            if(elem==sentinel)
                throw new IndexOutOfBoundsException();
            return elem;}
        private Element getElement(E value){
            Element elem=sentinel.getNext();
            while(elem!=sentinel && !value.equals(elem.getValue())){
                elem=elem.getNext();}
            if(elem==sentinel)
                return null;
            return elem;
        }

    public boolean isEmpty() {
        return sentinel.getNext()==sentinel;}
    public void clear() {
        sentinel.setNext(sentinel);
        sentinel.setPrev(sentinel);}
    public boolean contains(E value) {
        return indexOf(value)!=-1;}
    public E get(int index) {
        Element elem=getElement(index);
        return elem.getValue();}
    public E set(int index, E value) {
        Element elem=getElement(index);
        E retValue=elem.getValue();
        elem.setValue(value);
        return retValue;}
    public boolean add(E value) {
        Element newElem=new Element(value);
        sentinel.insertBefore(newElem);
        return true;}
    public boolean add(int index, E value) {
        Element newElem=new Element(value);
        if(index==0) sentinel.insertAfter(newElem);
        else{
            Element elem=getElement(index-1);
            elem.insertAfter(newElem);}
        return true;}

    public int indexOf(E value) {
        Element elem=sentinel.getNext();
        int counter=0;
        while(elem!=sentinel && !elem.getValue().equals(value)){
            counter++;
            elem=elem.getNext();}
        if(elem==sentinel)
            return -1;
        return counter;}
    public E remove(int index) {
        Element elem=getElement(index);
        elem.remove();
        return elem.getValue();}
    public boolean remove(E value) {
        Element elem=getElement(value);
        if(elem.equals(sentinel)) return false;
        elem.remove();
        return true;}
    public int size() {
        Element elem=sentinel.getNext();
        int counter=0;
        while(elem!=sentinel){
            counter++;
            elem=elem.getNext();}
        return counter;}

    public void reverse(){
        int i = 0;
        int size = size();
        if(size>1){
            Element current = getElement(0);
            Element nextElem = current.getNext();
            current.setNext(sentinel);
            current = nextElem;
            while(current != sentinel){
                nextElem = current.getNext();
                current.setNext(current.getPrev());
                current = nextElem;

            }
            nextElem = sentinel.getNext();
            sentinel.setNext(sentinel.getPrev());
            sentinel.setPrev(nextElem);
        }
    }
    public void printStructure() {
        Element elem = sentinel;
        System.out.print("sentinel");
        if(size() > 0){
            elem = elem.getNext();
        while (elem != sentinel) {
            System.out.print(" -> "+ elem.getValue());
            elem = elem.getNext();
        }
        }

    }
}


