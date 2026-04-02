public class Main{
    private static final int SIMULATIONS = 100;
    public static void main(String[] args) {
        Serializer writer1 = new Serializer("Random");
        Serializer writer2 = new Serializer("OverloadTransfer");
        Serializer writer3 = new Serializer("WorkSteal");

        writer1.writerInit("size;avgLoad;stdDev;QueryCount;MigrationCount");
        writer2.writerInit("size;avgLoad;stdDev;QueryCount;MigrationCount");
        writer3.writerInit("size;avgLoad;stdDev;QueryCount;MigrationCount");
        try{
            for(int i = 0; i < SIMULATIONS; i++){
                Algorithms alg = new Algorithms(50,75,25,40,writer1,writer2,writer3);
                alg.alg1();
                alg.alg2();
                alg.alg3();
                System.out.println(i);
            }
            writer1.close();
            writer2.close();
            writer3.close();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
