import java.util.Random;
import java.util.ArrayList;

public class Main {
    public final static int pages = 50;
    public static void main(String[] args) {

        Serializer writer1 = new Serializer("FIFO");
        Serializer writer2 = new Serializer("RAND");
        Serializer writer3 = new Serializer("OPT");
        Serializer writer4 = new Serializer("LRU");
        Serializer writer5 = new Serializer("ALRU");
        writer1.writerInit("Initial size;frames;pageFaults;Thrashing count;Total thrashing duration;longest thrashing durarion");
        writer2.writerInit("Initial size;frames;pageFaults;Thrashing count;Total thrashing duration;longest thrashing durarion");
        writer3.writerInit("Initial size;frames;pageFaults;Thrashing count;Total thrashing duration;longest thrashing durarion");
        writer4.writerInit("Initial size;frames;pageFaults;Thrashing count;Total thrashing duration;longest thrashing durarion");
        writer5.writerInit("Initial size;frames;pageFaults;Thrashing count;Total thrashing duration;longest thrashing durarion");

        for(int k = 0; k < 50; k++){
            System.out.println(k);
            Random random = new Random();
            int initialSize = random.nextInt(12500,15000);
            int[] processes = Generator.generate(initialSize, pages);

            ArrayList<Integer> p1 = new ArrayList<>();
            ArrayList<Integer> p2 = new ArrayList<>();
            ArrayList<Integer> p3 = new ArrayList<>();
            ArrayList<Integer> p4 = new ArrayList<>();
            ArrayList<Integer> p5 = new ArrayList<>();
            int frames = 3;
            for(int i = 0 ; i < 7 ; i++){
                p1.clear();
                p2.clear();
                p3.clear();
                p4.clear();
                p5.clear();

                for(int j : processes){
                    p1.add(j);
                    p2.add(j);
                    p3.add(j);
                    p4.add(j);
                    p5.add(j);
                }
               FIFO fifo = new FIFO(p1, frames);
               RAND rand = new RAND(p2, frames);
               OPT opt = new OPT(p3, frames);
               LRU lru = new LRU(p4, frames);
               ALRU alru = new ALRU(p5, frames);

               int [] fifoData = fifo.work();
               int[] randData = rand.work();
               int[] optData = opt.work();
               int[] lruData = lru.work();
               int[] aLruData = alru.work();

               writer1.write(String.format("%d;%d;%d;%d;%d;%d",initialSize,frames,fifoData[0],fifoData[1],fifoData[2],fifoData[3]));
               writer2.write(String.format("%d;%d;%d;%d;%d;%d",initialSize,frames,randData[0],randData[1],randData[2],randData[3]));
               writer3.write(String.format("%d;%d;%d;%d;%d;%d",initialSize,frames,optData[0],optData[1],optData[2],optData[3]));
               writer4.write(String.format("%d;%d;%d;%d;%d;%d",initialSize,frames,lruData[0],lruData[1],lruData[2],lruData[3]));
               writer5.write(String.format("%d;%d;%d;%d;%d;%d",initialSize,frames,aLruData[0],aLruData[1],aLruData[2],aLruData[3]));

           frames +=2;
            }
        }
        writer1.close();
        writer2.close();
        writer3.close();
        writer4.close();
        writer5.close();
    }
}
