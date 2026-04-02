public class Main {
    public static void main(String[] args) {
        IntegerToStringExec exec = new IntegerToStringExec();
        BST<Integer> test = new BST<>(Integer::compare);

        test.add(8);
        test.add(3);
        test.add(10);
        test.add(1);
        test.add(6);
        test.add(14);
        test.add(4);
        test.add(7);
        test.add(13);

        System.out.println("minimum: "+test.minimum());
        System.out.println("maximum: "+test.maximum());

        System.out.println(test.search(4));
        System.out.println(test.successor(4));

        System.out.println("Range:");
        System.out.println(test.range(5,14));

        System.out.println(test.remove(6));

        System.out.println("traversal:");
        test.preOrderTraversal(exec);
    }
}
