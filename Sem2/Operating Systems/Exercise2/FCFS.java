import java.util.ArrayList;

public class FCFS {
    private ArrayList<Proces> queue;
    private int disk;
    private int head;

    private int time = 0;
    private double longest = 0;

    private double AWT = 0;
    private int initialSize;

    public FCFS(int disk, ArrayList<Proces> queue){
        this.queue = queue;
        this.disk = disk;
        this.head = disk/2;
        this.initialSize = queue.size();
    }

    public double[] work(){
        while(!queue.isEmpty()){
            if(queue.getFirst().getTimeOfArrival() <= time){
                Proces currentTask = queue.getFirst();
                int distance = Math.abs(head - currentTask.getPosition());
                time += distance;
                head = currentTask.getPosition();
                currentTask.setWaitingTime(time- currentTask.getTimeOfArrival());
                AWT += currentTask.getWaitingTime();
                if (currentTask.getWaitingTime() > longest){
                    longest = currentTask.getWaitingTime();
                }
                queue.remove(currentTask);
            }
            if(!queue.isEmpty() && queue.getFirst().getTimeOfArrival() > time){
                time = queue.getFirst().getTimeOfArrival();
            }
        }
        AWT /= initialSize;
        return new double[] {initialSize,AWT,time, longest};
    }
}