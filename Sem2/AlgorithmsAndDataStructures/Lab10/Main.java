public class Main {
    public static void main(String[] args) {
        MaxBinomialHeap<Integer> test = new MaxBinomialHeap<>(Integer::compare);
        for(int i = 1; i <=10; i++){
            test.insert(i);
        }
        System.out.println( test.findMax());
        System.out.println( test.extractMax());
        System.out.println( test.findMax());

        MaxBinomialHeap<Integer> test2 = new MaxBinomialHeap<>(Integer::compare);
        for(int i = 1; i <=10; i++){
            test.insert(2*i);
        }
        test.union(test2);
        System.out.println(test.findMax());
        System.out.println(test.extractMax());
        System.out.println(test.extractMax());
        System.out.println(test.extractMax());
        System.out.println(test.extractMax());
        System.out.println(test.extractMax());
        System.out.println(test.findMax());


        MaxPriorityQueue<Integer, String> test3 = new MaxPriorityQueue<>(Integer::compare);
        test3.enqueue(2,"a");
        test3.enqueue(3,"b");
        test3.enqueue(1,"Kojtyła ");
        test3.enqueue(3,"Warol");
        test3.enqueue(0,"Jan");
        test3.enqueue(4,"Drugi");
        test3.enqueue(4,"Pierwszy");

        System.out.println("\nTest 3:");
        System.out.println(test3.isEmpty());
        System.out.println(test3.peek());
        System.out.println(test3.dequeue());
        System.out.println(test3.dequeue());
        System.out.println(test3.dequeue());
        System.out.println(test3.dequeue());
        System.out.println(test3.dequeue());
        System.out.println(test3.dequeue());
        System.out.println(test3.dequeue());
        System.out.println(test3.isEmpty());


    }
}
