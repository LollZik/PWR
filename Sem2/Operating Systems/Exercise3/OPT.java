import java.util.ArrayList;

public class OPT {
    private ArrayList<Integer> tasks;
    private ArrayList<Integer> frames;
    private int pageFaults = 0;
    private final int size;
    private final ThrashingTracker thrashingAnalizer;


    public OPT(ArrayList<Integer> tasks, int size){
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
                    int taskToDelete = findLongestUnused();
                    frames.remove(Integer.valueOf(taskToDelete));
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

    private int findLongestUnused(){
        ArrayList<Integer> pages = new ArrayList<>(frames);
        for(Integer task : tasks){
            pages.remove(task);
            if(pages.size() == 1){
                return pages.getFirst();
            }
        }
        return pages.getFirst();
    }
}