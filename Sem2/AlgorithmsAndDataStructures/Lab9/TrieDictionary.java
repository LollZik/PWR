import java.util.Stack;

public class TrieDictionary<V>{
    private static class PathStep<V> {
        char key;
        TrieElement<V> node;

        PathStep(char key, TrieElement<V> node) {
            this.key = key;
            this.node = node;
        }
    }

    private class Leaf implements TrieElement<V>{
        private V value;

        public Leaf(){
            this.value = null;
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public void setValue(V value){
            this.value = value;
        }
    }

    private class Node implements TrieElement<V>{
        private final int disctionarySize = 256;
        private V value;
        private int childrenCount = 0;

        @SuppressWarnings("unchecked")
        private final TrieElement<V>[] children = (TrieElement<V>[]) new TrieElement[disctionarySize];


        public Node(){
            this.value = null;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public void setValue(V value){
            this.value = value;
        }

        public TrieElement<V> getChild(char c) {
            return children[c];
        }

        public int getChildrenSize(){
            return this.childrenCount;
        }

        public void setChild(char c, TrieElement<V> element) {
            if(children[c] == null && element != null){
                this.childrenCount++;
            }
            children[c] = element;
        }

        public void removeKey(char c){
            if(this.children[c] != null){
                this.childrenCount--;
            }
            this.children[c] = null;
        }

        public TrieElement<V>[] getChildren(){
            return this.children;
        }
    }

    final Node root;
    final int disctionarySize;

    public TrieDictionary(int disctionarySize){
        this.root = new Node();
        this.disctionarySize = disctionarySize;
    }

    public V insert(String key, V value){
        if(value == null){
            throw new IllegalArgumentException("Null isn't a proper value.");
        }

        V temp;
        Node current = root;
        TrieElement<V> child;

        if(key.isEmpty()){
            temp = root.getValue();
            root.setValue(value);
            return temp;
        }

        for(int i = 0; i < key.length(); i++){
            char c = key.charAt(i);
            child = current.getChild(c);

            if (child == null){
                if (i == key.length()-1){
                    current.setChild(c, new Leaf());
                    child = current.getChild(c);
                    child.setValue(value);
                    return null; // New leaf, so previous value is null
                }
                else{
                    current.setChild(c, new Node());
                    current = (Node) current.getChild(c);
                }
            }
            else if(i == key.length() - 1){ // If c is the last character of key
                temp = child.getValue();
                child.setValue(value); // Swap value of the child
                return temp;
            }
            else if (child.isLeaf()){ // If leaf is found and c isn't last character of key, change leaf into a node
                temp = child.getValue();
                current.setChild(c, new Node());
                child = current.getChild(c);
                child.setValue(temp); // Copy leaf's value to the node
                current = (Node) child;
            }
            else{
                current = (Node) child;
            }
        }
        temp = current.getValue();
        current.setValue(value);
        return temp;
    }

    public V search(String key){
        if(key.isEmpty()){
            return root.getValue();
        }

        Node current = root;
        TrieElement<V> child;

        for(int i = 0; i < key.length(); i++){
            char c = key.charAt(i);
            child = current.getChild(c);

            if (child == null){
                return null;
            }
            else if (child.isLeaf()){
                if(i == key.length()-1){ // Leaf is the element we are looking for
                    return child.getValue();
                }
                // There are no children of leaf so the element doesn't exist
                return null;
            }
            else{
                current = (Node) child;
            }
        }
        return current.getValue();
    }

    public V remove(String key){
        V temp;

        if(key.isEmpty()){
            temp = root.getValue();
            root.setValue(null);
            return temp;
        }

        Node current = root;
        TrieElement<V> child;
        Stack<PathStep<V>>helper = new Stack<>();
        helper.push(new PathStep<>('\0', root));

        for(int i = 0; i < key.length(); i++){
            char c = key.charAt(i);
            child = current.getChild(c);

            if (child == null){
                return null;
            }
            else if (child.isLeaf()){
                if(i == key.length()-1){ // Leaf is the element we are looking for
                    temp = child.getValue();
                    helper.push(new PathStep<>(c, child));
                    prunePath(helper);
                    return temp;
                }
                // Leaf can't have children so the element doesn't exist
                return null;
            }
            else{
                current = (Node) child;
                helper.push(new PathStep<>(c, current));
            }
        }
        temp = current.getValue();
        prunePath(helper);
        return temp;
    }

    public String longestEmptyPath(){
        return longestPathHelper("", root, "");
    }

    private String longestPathHelper(String answer, TrieElement<V> node, String longestAns){
        TrieElement<V> child;
        String longestOutcome = "";
        String outcome;
        String tempAns;

        if(node.isLeaf()){
            if(longestAns.isEmpty()){
                return null;
            }
        }
        else{
            TrieElement<V>[] children = ((Node) node).getChildren();
            for(int i = 0; i < this.disctionarySize; i++){
                if (children[i] != null){
                    child = children[i];
                    if(child.getValue() == null){
                        tempAns = answer + (char)i;
                        if(answer.length() > longestAns.length()){
                            longestAns = answer;
                        }
                        outcome = longestPathHelper(tempAns, child, longestAns);
                    }
                    else{
                        if(answer.length() > longestAns.length()){
                            longestAns = answer;
                        }
                        outcome = longestPathHelper("", child, longestAns);
                    }
                    if(outcome != null){
                        if(outcome.length() > longestOutcome.length()){
                            longestOutcome = outcome;
                        }
                    }
                }
            }
            if(!longestOutcome.isEmpty()){
                if(longestOutcome.length() > longestAns.length()){
                    longestAns = longestOutcome;
                }
            }
        }
        return longestAns;
    }

    private void prunePath(Stack<PathStep<V>> stack){
        if(stack.isEmpty()){ return; }
        PathStep<V> pathStep = stack.pop();
        TrieElement<V> toRemove = pathStep.node;

        if(!toRemove.isLeaf()){
            // The value we want to remove isn't a leaf,
            // which means it has proper nodes as its children and can't be removed
            toRemove.setValue(null); // So we only need to change value to null
            return;
        }

        Node parent;
        char key = pathStep.key;

        while(!stack.isEmpty()){
            pathStep = stack.pop();
            parent = (Node) pathStep.node; // Leaf can't be a parent so its safe
            parent.removeKey(key); // remove key from previous .pop()

            if(parent.getChildrenSize() == 0){
                if(parent.getValue() == null){ // parent will now have to be deleted too
                    key = pathStep.key; // Set key to parent's key - it will get deleted in next iteration
                }
                else{ // Parent has value but no children, so we change it into leaf
                    if(!parent.equals(root)){
                        Leaf leaf = new Leaf();
                        leaf.setValue(parent.getValue());

                        pathStep = stack.peek();
                        Node grandparent = (Node) pathStep.node;
                        char k = pathStep.key;
                        grandparent.setChild(k, leaf);
                        break; // No need for further removals
                    }
                }
            }
            else{
                break; // No need for further removals
            }
        }
    }
}
