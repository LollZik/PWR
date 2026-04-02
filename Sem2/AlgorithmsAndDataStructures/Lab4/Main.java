public class Main {
    public static void main(String[] args) {
        TwoWayLinkedList<Double> test1 = new TwoWayLinkedList<>();
        TwoWayLinkedList<Object> test2 = new TwoWayLinkedList<>();
        TwoWayCycledList<Integer> reverseTest = new TwoWayCycledList<>();

        System.out.println("\nTest 1\n");
        test1.addFirst(5.0);
        test1.addLast(3.3);
        test1.addLast(2.1);
        test1.addLast(0.1);
        System.out.println(test1.getFirst());
        test1.remove(1);
        System.out.println(test1.get(1));
        System.out.println(test1.get(2));
        test1.remove(0);
        System.out.println(test1.getFirst());

        System.out.println("\nTest 2\n");
        test2.addFirst( null);
        test2.add(1, null);
        test2.add(2, null);
        System.out.println(test2.getFirst());
        System.out.println(test2.getLast());

        System.out.println("\nModyfikacja:");
        reverseTest.add(1);
        reverseTest.add(2);
        reverseTest.add(3);
        reverseTest.add(4);
        reverseTest.add(5);
        reverseTest.add(6);
        reverseTest.printStructure();
        reverseTest.reverse();
        System.out.println();
        reverseTest.printStructure();
    }
}
