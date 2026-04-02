import java.util.ArrayList;
import java.util.Iterator;

public class CSCAN {
    private ArrayList<Proces> queue;
    private int disk;
    private int head;
    private int time = 0;

    private double AWT = 0;
    private int initialSize;

    private double longest = -1;

    public CSCAN(int disk, ArrayList<Proces> queue){
        this.queue = queue;
        this.disk = disk;
        this.head = 0;
        this.initialSize = queue.size();
    }

    public double[] work(){
        while(!queue.isEmpty()){
            for(head = 0; head < disk; head++){
                Iterator<Proces> iterator = queue.iterator();
                while(iterator.hasNext()){
                    Proces task = iterator.next();
                    if(task.getTimeOfArrival() > time){
                        break;
                    }
                    if(task.getPosition() == head){
                        task.setWaitingTime(time - task.getTimeOfArrival());
                        AWT += task.getWaitingTime();
                        if(task.getWaitingTime() > longest){
                            longest = task.getWaitingTime();
                        }
                        iterator.remove();
                    }
                }
                time++;
            }
            time++;
        }
        AWT /= initialSize;
        return new double[] {initialSize, AWT, time, longest};
    }
}
