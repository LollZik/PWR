import MyComparator.*;
import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

public class TreeArrayBinaryHeap<T extends Comparable<T>> extends HeapPart<T> implements IList<T>{
    private class Element extends HeapPart<T> {
        private T value;
        private HeapPart<T> left = null;
        private HeapPart<T> right = null;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public HeapPart<T> getLeft() {
            return left;
        }

        public void setLeft(HeapPart<T> next) {
            this.left = next;
        }

        public HeapPart<T> getRight() {
            return right;
        }

        public void setRight(HeapPart<T> next) {
            this.right = next;
        }

        Element(T data) {
            this.value = data;
        }
    }
    private final int arrayStart;
    private final MyComparator<? super T> comparator;
    private Element maximum;
    private int size = 0;

    public TreeArrayBinaryHeap(int H){
        if (H < 0) {
            throw new InvalidParameterException("Height can't be negative");
        }
        this.arrayStart = (int)Math.pow(2,H) - 1;
        this.comparator =  new MyComparator<T>();
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index < arrayStart) {
            return getElement(index).getValue();
        }
        else{
            int indexInArray = index - arrayStart / (arrayStart + 1);
            ArrayHeapPart<T> arr = getArray(index);
            return arr.get(indexInArray);
        }
    }

    private Element getElement(int index){
        if (index < 0 || index >= size || index >= arrayStart) {
            throw new IndexOutOfBoundsException("getElement called with invalid index for tree part: " + index +
                    ", size: " + size + ", arrayStart: " + arrayStart);
        }
        if (index == 0) {
            if (maximum == null) {
                throw new NoSuchElementException("Heap is empty, cannot get root element.");
            }
            return maximum;
        }

        Element actElem = maximum;
        String binary = Integer.toBinaryString(index + 1);
        binary = binary.substring(1);

        for (int i = 0; i < binary.length(); i++) {
            if (actElem == null) {
               throw new NoSuchElementException("Navigation error: Encountered null node unexpectedly during traversal for index " + index + " at path step " + i);
            }

            char direction = binary.charAt(i);
            HeapPart<T> nextNode;

            if (direction == '1') {
                nextNode = actElem.getRight();
            } else {
                nextNode = actElem.getLeft();
            }

            if (i < binary.length() - 1) {
                if (nextNode instanceof Element) {
                    actElem = (Element) nextNode;
                }
            }
        }
        if (actElem == null){
            throw new NoSuchElementException("getElement failed to retrieve node for index " + index);
        }
        return actElem;
    }

    private void swap(int left, int right){
        if (left < 0 || left >= size || right < 0 || right >= size) {
            throw new IndexOutOfBoundsException();
        }
        if(left == right){
            return;
        }
        if(left < arrayStart && right < arrayStart){
            Element e1 = getElement(left);
            Element e2 = getElement(right);

            T temp = e1.getValue();
            e1.setValue(e2.getValue());
            e2.setValue(temp);
        }
        else if(left >= arrayStart && right >= arrayStart){
            ArrayHeapPart<T> arrLeft = getArray(left);
            ArrayHeapPart<T> arrRight = getArray(right);

            int idxL = (left-arrayStart)/(arrayStart+1);
            int idxR = (right-arrayStart)/(arrayStart+1);

            T temp = arrLeft.get(idxL);
            arrLeft.set(idxL, arrRight.get(idxR));
            arrRight.set(idxR, temp);
        }
        else{
            int arrIndex = Math.max(left, right);
            int treeIndex = Math.min(left, right);

            Element treeElem = (Element)get(treeIndex);

            int tempIdx = (arrIndex+1)%(arrayStart+1);
            Element arrParent = getElement((arrayStart+tempIdx-1)/2);
            ArrayHeapPart<T> array;
            if((arrayStart+tempIdx) % 2 == 1){
                array = (ArrayHeapPart<T>) arrParent.getLeft();
            }
            else{
                array = (ArrayHeapPart<T>) arrParent.getRight();
            }
            int arrIdx = (arrIndex-arrayStart)/(arrayStart+1);

            T temp = array.get(arrIdx);
            array.set(arrIndex, treeElem.getValue());
            treeElem.setValue(temp);
        }
    }

    private void sink(int idx){
        int idxOfBigger=2*idx+1;
        if(idxOfBigger<size){
            if(idxOfBigger+1<size &&
                    comparator.compare(get(idxOfBigger), get(idxOfBigger+1))<0)
                idxOfBigger++;
            if(comparator.compare(get(idx), get(idxOfBigger))<0){
                swap(idx,idxOfBigger);
                sink(idxOfBigger);
            }
        }
    }

    private void heapifyDown(){
        for(int i = (size-1)/2; i >= 0; i--)
            sink(i);
    }
    private void heapifyUp(int idx){
        while (idx > 0) {
            int parentIdx = (idx - 1) / 2;
            if(comparator.compare(get(idx), get(parentIdx)) > 0) {
                swap(idx, parentIdx);
            }
            idx = parentIdx;
        }
    }

    private ArrayHeapPart<T> getArray(int index){
        int arrayIndex = index - arrayStart;
        int arrayNum = arrayIndex % (arrayStart + 1);

        int arrayStartPoint = arrayStart + arrayNum;

        Element parent = getElement((arrayStartPoint-1)/2);
        ArrayHeapPart<T> arr;
        if(index%2==1){
            arr = (ArrayHeapPart<T>) parent.getLeft();
        }
        else{
            arr = (ArrayHeapPart<T>) parent.getRight();
        }
        return arr;
    }

    public void clear(){
        maximum = null;
        size = 0;
    }

    public void add(T element){
        if (size == 0){
            this.maximum = new Element(element);
        }
        else{
            Element parent;
            if (size < arrayStart){
                String binary = Integer.toBinaryString(size + 1);
                binary = binary.substring(1);

                Element current = maximum;
                parent = current;
                boolean goRight = false;

                for (int i = 0; i < binary.length(); i++) {
                    parent = current;
                    if (binary.charAt(i) == '1') {
                        current = (Element) current.getRight();
                        goRight = true;
                    } else {
                        current = (Element) current.getLeft();
                        goRight = false;
                    }
                }
                if(element == null){
                    throw new IllegalArgumentException();
                }
                Element newElement = new Element(element);
                if (goRight){
                    parent.setRight(newElement);
                }
                else{
                    parent.setLeft(newElement);
                }
                heapifyUp(size-1);
            }
            else{
                ArrayHeapPart<T> x = getArray(size-1);
                int temp = (size+1) % (arrayStart+1);

                parent = getElement((arrayStart+temp-1)/2);
                if(size % 2 == 0){
                    if(parent.getLeft() == null){
                        parent.setLeft(new ArrayHeapPart<T>(comparator));
                    }
                    x = (ArrayHeapPart<T>)parent.getLeft();
                    x.add(element);
                }
                else{
                    if(parent.getRight() == null){
                        parent.setRight(new ArrayHeapPart<T>(comparator));
                    }
                    x = (ArrayHeapPart<T>)parent.getRight();
                    x.add(element);
                }
                x.heapAdjustment(x.heapSize());
            }
        }
        size++;
    }

    public T maximum(){
        if(size == 0){
            throw new NoSuchElementException();
        }

        T max = maximum.getValue();

        if(size == 1){
            maximum = null;
            size = 0;
        }
        else{
            T lastElem = get(size-1);
            maximum.setValue(lastElem);
            size--;
            heapifyDown();
        }
        return max;
    }
}