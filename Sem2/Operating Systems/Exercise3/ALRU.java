import java.util.ArrayList;

public class ALRU {
    private static class Frame{
        private final int value;
        private int bit;

        public Frame(int value){
            this.value = value;
            this.bit = 1;
        }
        public void take(){
            this.bit = 0;
        }
        public void give(){
            this.bit = 1;
        }

        public int getValue(){
            return this.value;
        }

        public int getBit(){
            return this.bit;
        }
    }

    private ArrayList<Frame> frames;
    private ArrayList<Integer> tasks;
    private int pageFaults = 0;
    private final int size;
    private final ThrashingTracker thrashingAnalizer;



    public ALRU(ArrayList<Integer> tasks, int size){
        this.tasks = tasks;
        this.size = size;
        this.frames = new ArrayList<>();
        this.thrashingAnalizer = new ThrashingTracker(20, 14);
    }

    public int[] work(){
        while(!tasks.isEmpty()){
            int task = tasks.getFirst();
            if(!contains(frames, task)){
                if(frames.size() < size){
                    frames.add(new Frame(task));
                    pageFaults++;
                    thrashingAnalizer.addPageFault(true);
                }
                else{
                    frames.remove(removeElement(frames));
                    pageFaults++;
                    thrashingAnalizer.addPageFault(true);
                    frames.addLast(new Frame(task));
                }
                tasks.remove(Integer.valueOf(task));
            }
            else{
                tasks.remove(Integer.valueOf(task));
                updateBit(frames, task); // Move most recently used element to the end of array
                thrashingAnalizer.addPageFault(false);
            }
        }
        thrashingAnalizer.analyzeThrashing();
        return new int[]{pageFaults, thrashingAnalizer.getTotalThrashingCount(), thrashingAnalizer.getTotalThrashingDuration(), thrashingAnalizer.getMaxThrashingDuration()};
    }

    private Frame removeElement(ArrayList<Frame> frames){
        Frame toRemove = null;
        while(toRemove == null){
            for(Frame f : frames){
                if(f.getBit() == 0){
                    toRemove = f;
                    break;
                }
                else{
                    f.take();
                }
            }
        }
        return toRemove;
    }
    private void updateBit(ArrayList<Frame> frames, int value){
        for(int i = 0; i< frames.size();i++){
            if (frames.get(i).getValue() == value){
                frames.get(i).give();
                return;
            }
        }
    }
    private boolean contains(ArrayList<Frame> frames, int value){
        for(Frame f : frames){
            if(f.getValue() == value){
                return true;
            }
        }
        return false;
    }
}