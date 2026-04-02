public class Main {
    static final int H = 3;
    public static void main(String[] args) {
        TreeArrayBinaryHeap test = new TreeArrayBinaryHeap<>(H);
        for(int i = 0; i < 18; i++){
            if(i==7){
                System.out.println();
            }
            test.add(i);
        }
    }
}
