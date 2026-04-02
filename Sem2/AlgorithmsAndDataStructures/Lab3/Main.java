public class Main {
    public static void main(String[] args) {
        OneWaySplittingList<Integer> test1 = new OneWaySplittingList<>();
        OneWaySplittingList<Integer> test2 = new OneWaySplittingList<>();
        OneWaySplittingList<String> test3 = new OneWaySplittingList<>();
        OneWaySplittingList<OneWayLinkedListWithHead<Double>> test4 = new OneWaySplittingList<>();
        OneWaySplittingList<Object> test5 = new OneWaySplittingList<>();

        OneWayLinkedListWithHead<Integer> h = new OneWayLinkedListWithHead<>();


        test1.printStructure();

        System.out.println("\nTest 2\n");
        test2.add(2);
        test2.printStructure();

        System.out.println("\nTest 4\n");
        test4.add(new OneWayLinkedListWithHead<Double>());
        test4.add(new OneWayLinkedListWithHead<Double>());
        test4.add(new OneWayLinkedListWithHead<Double>());
        test4.add(new OneWayLinkedListWithHead<Double>());
        test4.add(new OneWayLinkedListWithHead<Double>());
        test4.get(2);
        test4.get(3);
        test4.printStructure();
        test4.remove(0);
        test4.printStructure();

        System.out.println("\nTest 3\n");
        test3.add("A");
        test3.add("B");
        test3.add("C");
        test3.add("D");
        test3.add("E");
        test3.add("F");
        test3.add("G");
        test3.add("H");
        test3.add("I");
        test3.get(2);
        test3.get(5);
        test3.reverse();
        test3.printStructure();

        System.out.println("\nTest 5\n");
        test5.add(0, null);
        test5.add(1, null);
        test5.get(1);
        test5.reverse();
        test5.printStructure();
    }
}