import java.util.ArrayList;

public class SSTF {
    private ArrayList<Proces> queue;
    private int disk;
    private int head;
    private int time = 0;

    private double AWT = 0;
    private int initialSize;
    private int shortestTask = 9999;
    private int shortestIndex;

    private Proces currentTask;
    private double longest = -1;
    private int starved = 0;

    public SSTF(int disk, ArrayList<Proces> queue){
        this.queue = queue;
        this.disk = disk;
        this.head = disk/2;
        this.initialSize = queue.size();
    }

    public double[] work(){

        while(!queue.isEmpty()){
            for(int i = 0; i<queue.size() && queue.get(i).getTimeOfArrival()<=time; i++){
                int timeRequired = Math.abs(queue.get(i).getPosition() - head);
                if(timeRequired < shortestTask){
                    shortestIndex = i;
                    shortestTask = timeRequired;
                }
            }
            if(shortestTask == 9999){
                time = queue.getFirst().getTimeOfArrival();
            }

            currentTask = queue.get(shortestIndex);
            int timeRequired = Math.abs(head - currentTask.getPosition());
            time += timeRequired;

            head = currentTask.getPosition();
            currentTask.setWaitingTime(time - currentTask.getTimeOfArrival());
            AWT += currentTask.getWaitingTime();


            if(currentTask.getWaitingTime() > longest){
                longest = currentTask.getWaitingTime();
            }
            if(currentTask.getWaitingTime() > 1000){
                starved++;
            }

            queue.remove(currentTask);
            shortestTask = 9999;
        }
        AWT /= initialSize;
        return new double[] {initialSize, AWT, time, longest, starved};
    }
}