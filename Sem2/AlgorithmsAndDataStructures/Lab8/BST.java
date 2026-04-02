import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class BST<T> {
    private class Node{
        private T value;
        private Node left;
        private Node right;
        public Node(T obj){
            value=obj;
        }

        public T getValue(){
            return this.value;
        }

        public void setValue(T data){
            this.value = data;
        }

        public Node getLeft(){
            return this.left;
        }

        public void setLeft(Node node){
            this.left = node;
        }

        public void setRight(Node node){
            this.right = node;
        }

        public Node getRight(){
            return this.right;
        }
    }

    private final Comparator<? super T> comparator;
    private Node root;
    public BST(Comparator<? super T> comp){
        this.comparator=comp;
        this.root=null;
    }

    public void add(T elem){
        if(root == null){
            root = new Node(elem);
        }
        else{
            Node x = root;
            while(true){
                if(comparator.compare(x.getValue(), elem) > 0){
                    if(x.getLeft() == null){
                        x.setLeft(new Node(elem));
                        return;
                    }
                    else{
                        x = x.getLeft();
                    }
                }
                else{
                    if(x.getRight() == null){
                        x.setRight(new Node(elem));
                        return;
                    }
                    else{
                        x = x.getRight();
                    }
                }
            }
        }
    }

    public T search(T elem){
        Node node = find(root, elem);
        return node.value;
    }

    public boolean remove(T elem){
        if (root == null) {
            throw new NoSuchElementException("Tree is empty");
        }
        Node current = root;
        Node previous = null;

        while (current != null) {

            T currentKey = current.getValue();
            if ((currentKey == null && elem == null) ||
                    (currentKey != null && currentKey.equals(elem))) { //Element found
                break;
            }

            previous = current;
            int cmp;
            if (currentKey == null || elem == null) {
                cmp = (currentKey == null) ? 1 : -1;
            }
            else {
                cmp = comparator.compare(elem, currentKey);
            }

            if (cmp < 0)
                current = current.getLeft();
            else
                current = current.getRight();
        }

        if (current == null) {
            throw new NoSuchElementException("Element not found");
        }
        if (current.getLeft() == null || current.getRight() == null) { // max 1 child
            Node newCurrent = (current.getLeft() == null) ? current.getRight() : current.getLeft();

            if (previous == null) {
                return true;
            } else if (previous.getLeft() == current) {
                previous.setLeft(newCurrent);
            } else {
                previous.setRight(newCurrent);
            }
        }
        else { // has 2 children
            Node p = null;
            Node temp = current.getRight();

            while (temp.getLeft() != null) {
                p = temp;
                temp = temp.getLeft();
            }

            if (p != null) {
                p.setLeft(temp.getRight());
            } else {
                current.setRight(temp.getRight());
            }
            current.setValue(temp.getValue());
        }
        return true;
    }

    private Node find(Node start, T elem) {
        Node node=start;
        int cmp;
        if(node != null && (cmp = comparator.compare(elem, node.value))!=0){
            node = cmp < 0 ? find(node.getLeft(), elem) : find(node.getRight(), elem);
        }
        return node;
    }

    public T maximum(){
        Node max = maximum(this.root);
        return max.getValue();
    }

    private Node maximum(Node node){
        if(node.getRight() != null){
            return maximum(node.getRight());
        }
        return node;
    }

    public T minimum(){
        Node min = minimum(this.root);
        return min.getValue();
    }
    private Node minimum(Node node){
        if(node.getLeft() != null){
            return minimum(node.getLeft());
        }
        return node;
    }

    public T successor(T elem){
        Node ans = findSuccessor(elem);
        return (ans == null) ? null : ans.getValue();
    }

    private Node findSuccessor(T elem) {
        Node current = root;
        Node successor = null;

        while (current != null) {
            int cmp = comparator.compare(elem, current.getValue());
            if (cmp == 0) {
                if (current.getRight() != null) {
                    return minimum(current.getRight());
                }
                else {
                    return successor;
                }
            }
            else if (cmp < 0) {
                successor = current;
                current = current.getLeft();
            }
            else {
                current = current.getRight();
            }
        }

        throw new NoSuchElementException("Element not found or no successor exists");
    }

    public <R> void preOrderTraversal(IExecutor<T,R> exec){
        PreOrderHelper(root, exec);
        System.out.println(exec.getResult());
    }

    private <R> void PreOrderHelper(Node node, IExecutor<T,R> exec) {
        if(node != null){
            exec.execute(node.value);
            PreOrderHelper(node.left, exec);
            PreOrderHelper(node.right, exec);
        }
    }

    public List<T> range(T low, T high){
        if(comparator.compare(low,high) >=0){
            throw new IllegalArgumentException();
        }

        List<T> answer = new ArrayList<>();
        Node current = root;
        int cmp = comparator.compare(current.getValue(), low);
        while(cmp > 0){ // while current value is higher than low, search for lower value
            current = current.getLeft();
            cmp = comparator.compare(current.getValue(), low);
        }
        cmp = comparator.compare(current.getValue(), high);
        while(cmp < 0){ // while current is lower than high, add current to the List<T>
            if(comparator.compare(current.getValue(), low) > 0 && comparator.compare(current.getValue(), high)<0){
                answer.add(current.getValue());
            }
            current = findSuccessor(current.getValue()); // Find next smallest bigger element
            cmp = comparator.compare(current.getValue(), high);
        }
        return answer;
    }
}
