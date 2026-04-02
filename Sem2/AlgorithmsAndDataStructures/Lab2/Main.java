import java.util.ArrayList;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        CollatzIterator testCollatz = new CollatzIterator(6);
        CollatzIterable testCollatz2 = new CollatzIterable(5);

        System.out.println("Test zad 1 a)");
        for(int i = 0; i<10;i++){
            System.out.println(testCollatz.next());
        }

        int i=0;
        System.out.println("\nTest zad 1 b)");
        for(int n : testCollatz2){
            System.out.println(n);
            i++;
            if(i>10){
                break;
            }
        }

        System.out.println("\nTest zad 2");
        ArrayList<Integer> testList = new ArrayList<>();
        for(int j = 1;j<=10;j++){
            testList.add(j);
        }

        SubswapIterator<Integer> testSubSwapIterator = new SubswapIterator<>(testList, 3);

        while(testSubSwapIterator.hasNext()){
            System.out.print(testSubSwapIterator.next()+" ");
        }

        System.out.println("\n\nTest zad 3 (Modyfikacja)\n");

        Object[] list = {new ArrayList<Iterator>() {},new CollatzIterator(6), new ArrayList<Integer>(), new CollatzIterator(61)};
        Integer[] list2 = {2,1,3,7};
        String[] list3 = null;
        String[] list4 = {null,null};
        RandomIterator<Object> testRandomIterator1 = new RandomIterator<>(list);

        while(testRandomIterator1.hasNext()){
            System.out.print(testRandomIterator1.next()+" ");
        }
        System.out.println();


        RandomIterator<Object> testRandomIterator2 = new RandomIterator<>(list2);
        while(testRandomIterator2.hasNext()){
            System.out.print(testRandomIterator2.next()+" ");
        }


        System.out.println();
        RandomIterator<Object> testRandomIterator3 = new RandomIterator<>(list3);
        while(testRandomIterator3.hasNext()){
            System.out.print(testRandomIterator3.next()+" ");
        }

        
        RandomIterator<Object> testRandomIterator4 = new RandomIterator<>(list4);
        while(testRandomIterator4.hasNext()){
            System.out.print(testRandomIterator4.next()+" ");
        }
    }
}
