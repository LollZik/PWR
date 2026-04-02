import java.util.ArrayList;

public class LRU {
    private ArrayList<Integer> tasks;
    private ArrayList<Integer> frames;
    private int pageFaults = 0;
    private final int size;
    private final ThrashingTracker thrashingAnalizer;


    public LRU(ArrayList<Integer> tasks, int size){
        this.tasks = tasks;
        this.size = size;
        this.frames = new ArrayList<>();
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
                    frames.removeFirst();
                    pageFaults++;
                    thrashingAnalizer.addPageFault(true);
                    frames.addLast(task);
                }
                tasks.remove(Integer.valueOf(task));
            }
            else{
                tasks.remove(Integer.valueOf(task));
                frames.remove(Integer.valueOf(task));
                frames.add(task); // Move most recently used element to the end of array
                thrashingAnalizer.addPageFault(false);
            }
        }
        thrashingAnalizer.analyzeThrashing();
        return new int[]{pageFaults, thrashingAnalizer.getTotalThrashingCount(), thrashingAnalizer.getTotalThrashingDuration(), thrashingAnalizer.getMaxThrashingDuration()};
    }
}
