import java.util.ArrayList;

public class FCFS {
    private ArrayList<Proces> queue;
    private final int processesAmount;

    private double time = 0;
    private final double switchTime = 0.2;
    private int switches = 0;
    private double AWT = 0;
    private double AET = 0;
    private double longest;

    public FCFS(ArrayList<Proces> queue){
        this.queue = queue;
        this.processesAmount = queue.size();
        }
    public double[] work(){
        while(!queue.isEmpty()){
            Proces task = queue.getFirst();
            time += switchTime;
            switches++;
            task.setWaitingTime(time - task.getTimeOfArrival());
            if (task.getWaitingTime() > longest){
                longest = task.getWaitingTime();
            }

            time +=task.getTimeRequired();
            task.setexecutionTime(time-task.getWaitingTime()-task.getTimeOfArrival());
            task.work(task.getTimeRequired());

            AWT += (task.getWaitingTime());
            AET += (time-task.getWaitingTime()-task.getTimeOfArrival());

            queue.removeFirst();
        }
            AWT /= processesAmount;
        AET /= processesAmount;
        return new double[] {processesAmount,AWT,AET,time,longest,switches};
    }
}
