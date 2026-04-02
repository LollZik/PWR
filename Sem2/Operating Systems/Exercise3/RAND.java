import java.util.ArrayList;
import java.util.Random;

public class RAND {
    private ArrayList<Integer> tasks;
    private ArrayList<Integer> frames;
    private int pageFaults = 0;
    private final int size;
    Random rand;
    private final ThrashingTracker thrashingAnalizer;


    public RAND(ArrayList<Integer> tasks, int size){
        this.tasks = tasks;
        this.size = size;
        this.frames = new ArrayList<>();
        rand = new Random();
        this.thrashingAnalizer = new ThrashingTracker(20, 14);

    }

    public int[] work(){
        while(!tasks.isEmpty()){
            int task = tasks.getFirst();
            if(!frames.contains(task)){
                if(frames.size() < size){
                    frames.add(task);
                    pageFaults++;
                    thrashingAnalizer.addPageFault(true);

                }
                else{
                    frames.remove(rand.nextInt(size));
                    pageFaults++;
                    thrashingAnalizer.addPageFault(true);
                    frames.addLast(task);
                }
                tasks.remove(Integer.valueOf(task));
            }
            else{
                tasks.removeFirst();
                thrashingAnalizer.addPageFault(false);

            }
        }
        thrashingAnalizer.analyzeThrashing();
        return new int[]{pageFaults, thrashingAnalizer.getTotalThrashingCount(), thrashingAnalizer.getTotalThrashingDuration(), thrashingAnalizer.getMaxThrashingDuration()};
    }
}
